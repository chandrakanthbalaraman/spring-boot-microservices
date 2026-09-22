package com.example.microservices.order.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;

import com.example.microservices.order.client.feign.InventoryErrorDecoder;

import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;

/**
 * Per-client Feign overrides. Not {@code @Configuration} on purpose: a component-scanned
 * {@code @Configuration} listed on {@code @FeignClient(configuration=...)} becomes global
 * for every Feign client. Spring Cloud instantiates this class in the inventory-service
 * child context instead.
 */
public class InventoryFeignConfig {

    /**
     * Same 500ms / 2s budget as {@code inventoryRestClient}. One YAML source
     * ({@link InventoryClientProperties}); Feign does not get a second millisecond copy.
     */
    @Bean
    public Request.Options inventoryFeignOptions(InventoryClientProperties properties) {
        return new Request.Options(
                properties.connectTimeout().toMillis(), TimeUnit.MILLISECONDS,
                properties.readTimeout().toMillis(), TimeUnit.MILLISECONDS,
                true);
    }

    @Bean
    public ErrorDecoder inventoryErrorDecoder() {
        return new InventoryErrorDecoder();
    }

    @Bean
    public Logger.Level inventoryFeignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
