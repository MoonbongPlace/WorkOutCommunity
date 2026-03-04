package com.community.global.component;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.profile")
public record ProfileProperties(String defaultImageUrl) {}
