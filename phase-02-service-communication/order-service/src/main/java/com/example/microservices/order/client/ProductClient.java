package com.example.microservices.order.client;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.example.microservices.order.client.dto.ProductResponse;
import com.example.microservices.order.exception.DownstreamServiceException;
import com.example.microservices.order.exception.DownstreamServiceUnavailableException;
import com.example.microservices.order.exception.ProductNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductClient {

    private static final String SERVICE = "product-service";

    private final RestClient restClient;

    public ProductClient(@Qualifier("productRestClient") RestClient restClient) {
        log.info("Creating product rest client");
        this.restClient = restClient;
    }

    public ProductResponse getProductByProductId(Long productId) {
        return execute(() -> restClient.get()
                .uri("/api/v1/products/{productId}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
                    throw new ProductNotFoundException("Product not found: " + productId);
                })
                .body(ProductResponse.class));
    }

    private <T> T execute(Supplier<T> call) {
        try {
            return call.get();
        } catch (ResourceAccessException e) {
            log.warn("{} unreachable", SERVICE, e);
            throw new DownstreamServiceUnavailableException(SERVICE, SERVICE + " unavailable", e);
        } catch (RestClientResponseException e) {
            log.warn("{} unexpected HTTP {}", SERVICE, e.getStatusCode(), e);
            throw new DownstreamServiceException(SERVICE, SERVICE + " returned an unexpected error", e);
        }
    }
}
