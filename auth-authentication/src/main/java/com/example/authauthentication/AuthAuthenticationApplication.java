package com.example.authauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
@EnableWebSecurity
//@EnableDiscoveryClient
public class AuthAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthAuthenticationApplication.class, args);
    }

}
