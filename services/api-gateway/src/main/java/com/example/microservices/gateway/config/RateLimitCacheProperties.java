package com.example.microservices.gateway.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Tunables for the in-memory (Caffeine) Bucket4j store used by gateway rate limiting.
 */
@ConfigurationProperties(prefix = "gateway.rate-limit.cache")
public record RateLimitCacheProperties(
    /** Max distinct rate-limit keys (e.g. client IPs) kept in memory. */
    long maximumSize,
    /** How long an idle bucket entry may stay in the cache. */
    Duration keepAfterRefill
) {
}
