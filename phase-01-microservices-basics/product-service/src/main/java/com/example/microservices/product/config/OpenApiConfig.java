package com.example.microservices.product.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "product-service",
                version = "v1",
                description = "Product catalog — Phase 01. Import into Postman from /v3/api-docs."
        )
)
public class OpenApiConfig {
}
