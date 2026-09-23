package com.example.microservices.order.config;

import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;

import com.example.microservices.order.client.feign.ProductErrorDecoder;

import feign.Client;
import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import feign.hc5.ApacheHttp5Client;

/**
 * Per-client Feign overrides for product-service. Not {@code @Configuration}:
 * a scanned configuration class on {@code @FeignClient(configuration=...)}
 * would apply to every Feign client. Spring Cloud builds a child context
 * for this class alone.
 */
public class ProductFeignConfig {

    @Bean
    public Request.Options productFeignOptions(ProductClientProperties properties) {
        return new Request.Options(
                properties.connectTimeout().toMillis(), TimeUnit.MILLISECONDS,
                properties.readTimeout().toMillis(), TimeUnit.MILLISECONDS,
                true);
    }

    /**
     * Apache HC5 keeps the per-service pool. Wrapping it in
     * {@link FeignBlockingLoadBalancerClient} is what turns
     * {@code product-service} into a Eureka lookup. A bare {@link ApacheHttp5Client}
     * would treat that name as a DNS host.
     */
    @Bean
    public Client productFeignHttpClient(
            ProductClientProperties properties,
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
    public ErrorDecoder productErrorDecoder() {
        return new ProductErrorDecoder();
    }

    @Bean
    public Logger.Level productFeignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
