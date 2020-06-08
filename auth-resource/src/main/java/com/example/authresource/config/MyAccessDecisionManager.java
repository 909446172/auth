package com.example.authresource.config;

import com.example.authresource.client.AuthClient;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * 决策器
 */
@Component
@AllArgsConstructor
public class MyAccessDecisionManager implements AccessDecisionManager {

    private final static Logger logger = LoggerFactory.getLogger(MyAccessDecisionManager.class);
    private AuthClient authClient;

    /**
     * 通过传递的参数来决定用户是否有访问对应受保护对象的权限
     *
     * @param authentication   包含了当前的用户信息，包括拥有的权限。这里的权限来源就是前面登录时UserDetailsService中设置的authorities。
     * @param object           就是FilterInvocation对象，可以得到request等web资源
     * @param configAttributes configAttributes是本次访问需要的权限
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (null == configAttributes || 0 >= configAttributes.size()) {
            return;
        } else {
            FilterInvocation filterInvocation;
            if (!(object instanceof FilterInvocation)) {
                throw new AccessDeniedException("You can't convert Object to FilterInvocation");
            }
            filterInvocation = (FilterInvocation) object;
            String authorization = filterInvocation.getHttpRequest().getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String authToken = authorization.substring(7);
                checkRole(configAttributes, authToken);
                return;
            }
            throw new AccessDeniedException("当前访问没有权限");
        }

    }

    /**
     * 表示此AccessDecisionManager是否能够处理传递的ConfigAttribute呈现的授权请求
     */
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    /**
     * 表示当前AccessDecisionManager实现是否能够为指定的安全对象（方法调用或Web请求）提供访问控制决策
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
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