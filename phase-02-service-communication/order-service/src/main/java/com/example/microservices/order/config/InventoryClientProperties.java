package com.example.microservices.order.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clients.inventory-service")
public record InventoryClientProperties(
    String baseUrl,
    Duration connectTimeout,
    Duration readTimeout
) {
}
