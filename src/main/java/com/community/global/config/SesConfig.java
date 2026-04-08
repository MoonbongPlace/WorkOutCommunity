package com.community.global.config;

import com.community.global.component.property.SesProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
@EnableConfigurationProperties(SesProperties.class)
public class SesConfig {

    @Bean
    public SesV2Client sesV2Client(SesProperties sesProperties){
        return SesV2Client.builder()
                .region(software.amazon.awssdk.regions.Region.of(sesProperties.getRegion()))
                .build();
    }
}
