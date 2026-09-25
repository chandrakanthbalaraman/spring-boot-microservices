package com.example.microservices.order.client.feign.inventory;

import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;

import com.example.microservices.order.config.InventoryClientProperties;

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
     * Timeouts come from {@link InventoryClientProperties}
     * ({@code clients.inventory-service} in YAML).
     */
    @Bean
    public Request.Options inventoryFeignOptions(InventoryClientProperties properties) {
        return new Request.Options(
                properties.connectTimeout().toMillis(), TimeUnit.MILLISECONDS,
                properties.readTimeout().toMillis(), TimeUnit.MILLISECONDS,
                true);
    }

    /**
     * Apache HC5 keeps the per-service pool. Wrapping it in
     * {@link FeignBlockingLoadBalancerClient} is what turns
     * {@code inventory-service} into a Eureka lookup. A bare {@link ApacheHttp5Client}
     * would treat that name as a DNS host.
     * {@code disableAutomaticRetries()} — retries are Phase 7; POST + retry can double-reserve.
     */
    @Bean
    public Client inventoryFeignClient(
            InventoryClientProperties properties,
            LoadBalancerClient loadBalancerClient,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setMaxConnTotal(properties.maxConnections())
                        .setMaxConnPerRoute(properties.maxConnectionsPerRoute())
                        .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .disableAutomaticRetries()
                .build();

        return new FeignBlockingLoadBalancerClient(
                new ApacheHttp5Client(httpClient),
                loadBalancerClient,
                loadBalancerClientFactory);
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
