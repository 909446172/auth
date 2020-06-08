package com.example.authresource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("leve.auth.client")
@Data
public class AuthClientConfig {

    private String id;
    private String secret;
    private String url;

}
