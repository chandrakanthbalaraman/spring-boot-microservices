package com.example.microservices.gateway.filters;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

@Component
@Slf4j
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    static final String HEADER = "X-Correlation-ID";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startedAt = System.nanoTime();
        AtomicBoolean logged = new AtomicBoolean(false);

        String correlationId = resolveCorrelationId(
                exchange.getRequest().getHeaders().getFirst(HEADER));

        // Copy the request so the downstream service receives this header.
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header(HEADER, correlationId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request)
                .build();

        // Correlation header + access log with final status (runs after error handler too).
        mutatedExchange.getResponse().beforeCommit(() -> {
            mutatedExchange.getResponse()
                    .getHeaders()
                    .set(HEADER, correlationId);
            // signal=null → response committed; HTTP status is the source of truth
            // (including 5xx written by ErrorWebExceptionHandler).
            logGatewayRequest(mutatedExchange, correlationId, startedAt, null, logged);
            return Mono.empty();
        });

        return chain.filter(mutatedExchange)
                .doFinally(signal -> {
                    // ON_ERROR: do not log here — status is often still null; beforeCommit
                    // runs after ErrorWebExceptionHandler assigns the final HTTP status.
                    // CANCEL: response may never commit, so log with the Reactor signal.
                    if (signal == SignalType.CANCEL) {
                        logGatewayRequest(mutatedExchange, correlationId, startedAt, signal, logged);
                    }
                });
    }

    private void logGatewayRequest(
            ServerWebExchange exchange,
            String correlationId,
            long startedAt,
            SignalType signal,
            AtomicBoolean logged) {
        if (!logged.compareAndSet(false, true)) {
            return;
        }
        long durationMs = (System.nanoTime() - startedAt) / 1_000_000;
        Object routeAttribute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String routeId = routeAttribute instanceof Route route ? route.getId() : "unknown";
        log.info(
                "gateway_request correlationId={} method={} path={} routeId={} status={} signal={} durationMs={}",
                correlationId,
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI().getPath(),
                routeId,
                exchange.getResponse().getStatusCode(),
                signal != null ? signal : "COMMITTED",
                durationMs);
    }

    private String resolveCorrelationId(String correlationId) {
        boolean usable = correlationId != null && !correlationId.isEmpty() && !correlationId.isBlank()
                && correlationId.length() <= 128 && correlationId.chars().noneMatch(Character::isISOControl);

        return usable ? correlationId : generateCorrelationId();
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
