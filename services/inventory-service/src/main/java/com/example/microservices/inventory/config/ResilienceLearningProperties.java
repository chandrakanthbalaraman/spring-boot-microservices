package com.example.microservices.inventory.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "learning.resilience")
public record ResilienceLearningProperties(
    Duration inventoryGetDelay
) {
    
}
