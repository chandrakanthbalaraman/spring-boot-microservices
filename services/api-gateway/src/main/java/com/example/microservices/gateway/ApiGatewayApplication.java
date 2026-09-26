package com.example.microservices.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.microservices.gateway.config.RateLimitCacheProperties;

/**
 * Phase 05 — Spring Cloud Gateway entrypoint.
 * Routes live in {@code application.yml}; cross-cutting filters under {@code filters}.
 */
@SpringBootApplication
@EnableConfigurationProperties(RateLimitCacheProperties.class)
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
