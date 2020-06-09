package com.example.authresource.config.voter;

import com.example.authresource.client.AuthClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author zyy
 * @version 1.0.0
 * @date 2020/6/9 10:46
 */
@AllArgsConstructor
public class MyRoleVoter implements MyVoter<Object> {

    private AuthClient authClient;

    /**
     * role 支持的条件，角色必须包含ROLE_前缀
     */
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * role 认证逻辑
     */
    @Override
    public int vote(Authentication authentication, Object object,
                    Collection<ConfigAttribute> attributes) {
        if (null == attributes || 0 >= attributes.size()) {
            return ACCESS_DENIED;
        } else {
            FilterInvocation filterInvocation;
            if (!(object instanceof FilterInvocation)) {
                throw new AccessDeniedException("You can't convert Object to FilterInvocation");
            }
            filterInvocation = (FilterInvocation) object;
            String authorization = filterInvocation.getHttpRequest().getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String authToken = authorization.substring(7);
                checkRole(attributes, authToken);
                return ACCESS_GRANTED;
            }
            return ACCESS_DENIED;
        }
    }

    private void checkRole(Collection<ConfigAttribute> needRoles, String token) {
        Map map = authClient.checkToken(token);
        if ((Boolean) map.get("active")) {
            Object authorities = map.get("authorities");
            if (authorities instanceof List) {
                String needRole;
                List role = (List) authorities;
                for (ConfigAttribute needRole1 : needRoles) {
                    needRole = needRole1.getAttribute();
                    for (Object s : role) {
                        if (s instanceof String) {
                            if (needRole.trim().equals(((String) s).trim())) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
