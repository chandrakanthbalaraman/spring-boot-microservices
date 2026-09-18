package com.example.microservices.order.config;

import java.time.Duration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties({
    ProductClientProperties.class,
    InventoryClientProperties.class
})
public class RestClientConfig {

    @Bean(name = "productRestClient")
    public RestClient productRestClient(ProductClientProperties properties) {
        return restClient("product-service", properties.baseUrl(),
                properties.connectTimeout(), properties.readTimeout());
    }

    /**
     * Slice C keeps this bean so RestClient inventory still compiles. After
     * {@code OrderServiceImpl} switches to {@code InventoryFeignClient}, this
     * client is unused — leave it as the comparison artifact, then delete it.
     */
    @Bean(name = "inventoryRestClient")
    public RestClient inventoryRestClient(InventoryClientProperties properties) {
        return restClient("inventory-service", properties.baseUrl(),
                properties.connectTimeout(), properties.readTimeout());
    }

    private RestClient restClient(String name, String baseUrl, Duration connect, Duration read) {
        log.info("Creating {} rest client with base URL: {}", name, baseUrl);
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(timeoutFactory(connect, read))
                .build();
    }

    /**
     * Phase 2 first exercise: fail fast when a neighbor is down or hung.
     * Values live in application.yml under clients.*.connect-timeout / read-timeout.
     */
    private ClientHttpRequestFactory timeoutFactory(Duration connectTimeout, Duration readTimeout) {
        return ClientHttpRequestFactoryBuilder.detect()
            .build(ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(connectTimeout)
                .withReadTimeout(readTimeout));
    }
}
