package com.example.microservices.gateway.config;

import java.net.InetSocketAddress;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.caffeine.CaffeineProxyManager;
import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import io.github.bucket4j.distributed.remote.RemoteBucketState;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    AsyncProxyManager<String> caffeineProxyManager(RateLimitCacheProperties properties) {
        Caffeine<String, RemoteBucketState> cache = (Caffeine) Caffeine.newBuilder()
            .maximumSize(properties.maximumSize());

        return new CaffeineProxyManager<>(cache, properties.keepAfterRefill()).asAsync();
    }

    @Bean
    KeyResolver clientIpKeyResolver() {
        return exchange -> {
            InetSocketAddress address = exchange.getRequest().getRemoteAddress();
            String key = address != null && address.getAddress() != null
                ? address.getAddress().getHostAddress()
                : "unknown";

            return Mono.just(key);
        };
    }
}
