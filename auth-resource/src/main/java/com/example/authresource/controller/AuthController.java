package com.example.authresource.controller;

import com.example.authresource.client.AuthClient;
import com.example.authresource.dto.User;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("oauth")
@AllArgsConstructor
public class AuthController {

    AuthClient authClient;

    @PostMapping("login")
    public OAuth2AccessToken login(@RequestBody User user) {
        System.out.println(user);
        OAuth2AccessToken token = authClient.createToken(user);
        Object authorities = token.getAdditionalInformation().get("authorities");
        System.out.println(authorities);
        return token;
    }

    @PostMapping("refresh")
    public OAuth2AccessToken refreshToken(String refreshToken) {
        return authClient.refreshToken(refreshToken);
    }

    @GetMapping("check/{token}")
    public Map checkToken(@PathVariable("token") String token) {
        return authClient.checkToken(token);
    }


}
