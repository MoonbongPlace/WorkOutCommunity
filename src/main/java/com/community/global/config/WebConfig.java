package com.community.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.profile-dir}")
    private String profileDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String abs = Paths.get(profileDir).toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/profile/**")
                .addResourceLocations(abs);
    }
}