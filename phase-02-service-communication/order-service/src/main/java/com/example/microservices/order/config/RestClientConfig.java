package com.example.microservices.order.config;

import org.springframework.web.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RestClientConfig {

    @Bean(name = "productRestClient")
    public RestClient productRestClient(
        @Value("${clients.product-service.base-url}") String baseUrl
    ) {
        log.info("Creating product rest client with base URL: {}", baseUrl);
        return RestClient.builder()
            .baseUrl(baseUrl)
            // TODO(phase-02): .requestFactory(timeoutFactory(connect, read))
            .build();
    }

    @Bean(name = "inventoryRestClient")
    public RestClient inventoryRestClient(
        @Value("${clients.inventory-service.base-url}") String baseUrl
    ) {
        log.info("Creating inventory rest client with base URL: {}", baseUrl);
        return RestClient.builder()
            .baseUrl(baseUrl)
            // TODO(phase-02): .requestFactory(timeoutFactory(connect, read))
            .build();
    }

    /**
     * Phase 2 first exercise: fail fast when a neighbor is down or hung.
     * Values live in application.yml under clients.*.connect-timeout / read-timeout.
     *
     * Hint (Boot 3.4+): ClientHttpRequestFactoryBuilder.detect()
     *   .build(ClientHttpRequestFactorySettings.defaults()
     *       .withConnectTimeout(connect)
     *       .withReadTimeout(read));
     */
    // TODO(phase-02): implement timeoutFactory(Duration connect, Duration read)
}
