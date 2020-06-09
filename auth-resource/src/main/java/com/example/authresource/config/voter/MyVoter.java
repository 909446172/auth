package com.example.authresource.config.voter;

import org.springframework.security.access.AccessDecisionVoter;

/**
 * @author zyy
 * @version 1.0.0
 * @date 2020/6/9 11:37
 */
public interface MyVoter<T> extends AccessDecisionVoter<T> {

}
