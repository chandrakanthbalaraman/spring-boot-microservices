package com.example.microservices.order.config;

import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.context.annotation.Bean;

import com.example.microservices.order.client.feign.InventoryErrorDecoder;

import feign.Client;
import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import feign.hc5.ApacheHttp5Client;

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

    /**
     * Slice +: replace Feign's default {@code HttpURLConnection} client (no pool)
     * with Apache HC5 + named max connections from {@code clients.inventory-service}.
     * {@code disableAutomaticRetries()} — retries are Phase 7; POST + retry can double-reserve.
     */
    @Bean
    public Client inventoryFeignClient(InventoryClientProperties properties) {
        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setMaxConnTotal(properties.maxConnections())
                        .setMaxConnPerRoute(properties.maxConnectionsPerRoute())
                        .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .disableAutomaticRetries()
                .build();

        return new ApacheHttp5Client(httpClient);
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
