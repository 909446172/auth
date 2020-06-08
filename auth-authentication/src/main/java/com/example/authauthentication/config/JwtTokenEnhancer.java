package com.example.authauthentication.config;

import com.example.authauthentication.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Jwt内容增强器
 * Created by macro on 2019/10/8.
 */
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        List<String> collect = authentication.getUserAuthentication().getAuthorities().stream().map(o -> {
            Role role = (Role) o;
            return role.getAuthority();
        }).collect(Collectors.toList());
        Map<String, Object> info = new HashMap<>();
        info.put("enhance", "enhance info");
        info.put("authorities", collect);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}