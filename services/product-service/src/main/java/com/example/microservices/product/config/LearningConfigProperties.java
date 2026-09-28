package com.example.microservices.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@RefreshScope
@ConfigurationProperties(prefix = "learning")
public class LearningConfigProperties {
    private String serviceMessage;
}
