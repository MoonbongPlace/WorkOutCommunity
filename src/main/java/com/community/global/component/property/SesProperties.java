package com.community.global.component.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "aws.ses")
public class SesProperties {
    private String region;
    private String fromAddress;
}
