package com.community.global;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="jwt")
public record JWTProperties (
        String secret,
        long accessExpSeconds,
        long refreshExpSeconds
){}
