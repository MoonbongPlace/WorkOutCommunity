package com.community.global.openai;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(OpenAiProperties.class)
@Configuration
public class OpenAiConfig {}