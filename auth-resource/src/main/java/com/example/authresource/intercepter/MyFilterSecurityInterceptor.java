package com.example.authresource.intercepter;

import com.example.authresource.client.AuthClient;
import com.example.authresource.config.voter.MyRoleVoter;
import com.example.authresource.config.voter.MyVoter;
import lombok.AllArgsConstructor;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private AuthClient authClient;

    @Autowired
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    public void setMyAccessDecisionManager() {
        super.setAccessDecisionManager(new UnanimousBased(initVoter(authClient)));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {

        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }


    private List<AccessDecisionVoter<?>> initVoter(AuthClient authClient) {
        Reflections reflections = new Reflections("com.example.authresource.config.voter");
        Set<Class<? extends MyVoter>> subTypes = reflections.getSubTypesOf(MyVoter.class);
        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
        subTypes.forEach(aClass -> {
            try {
                accessDecisionVoters.add(aClass.getConstructor(AuthClient.class).newInstance(authClient));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
        return accessDecisionVoters;
    }


}