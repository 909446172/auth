package com.example.authresource.client;


import com.example.authresource.config.AuthClientConfig;
import com.example.authresource.dto.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author zyy
 * @version 1.0.0
 * @date 2020/6/9 10:46
 */
@Component
@AllArgsConstructor
public class AuthClient {

    RestTemplate restTemplate;
    AuthClientConfig authClientConfig;

    private HttpHeaders getHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setBasicAuth(authClientConfig.getId(), authClientConfig.getSecret());
        return headers;
    }

    public OAuth2AccessToken createToken(User user) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", user.getUser());
        body.add("password", user.getPassword());
        body.add("grant_type", "password");
        body.add("scope", "all");
        HttpEntity entity = new HttpEntity<MultiValueMap<String, String>>(body, getHeader());
        return restTemplate.postForObject(String.format("http://%s%s", authClientConfig.getUrl(), "/oauth/token"), entity, OAuth2AccessToken.class);
    }

    public OAuth2AccessToken refreshToken(String refreshToken) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        HttpEntity entity = new HttpEntity<MultiValueMap<String, String>>(body, getHeader());
        return restTemplate.postForObject(String.format("http://%s%s", authClientConfig.getUrl(), "/oauth/token"), entity, OAuth2AccessToken.class);
    }


    public Map checkToken(String token) {
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<>(getHeader());
        return restTemplate.exchange(String.format("http://%s%s?%s", authClientConfig.getUrl(), "oauth/check_token", "token={token}"), HttpMethod.GET, multiValueMapHttpEntity, Map.class, token)
                .getBody();

    }

}
