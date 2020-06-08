package com.example.authresource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class AuthResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthResourceApplication.class, args);
    }

}
