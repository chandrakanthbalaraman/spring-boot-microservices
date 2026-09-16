package com.example.microservices.order.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.microservices.order.client.dto.ProductResponse;
import com.example.microservices.order.exception.ProductNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(@Qualifier("productRestClient") RestClient restClient) {
        log.info("Creating product rest client");
        this.restClient = restClient;
    }

    public ProductResponse getProductByProductId(Long productId) {
        return restClient.get()
                .uri("/api/v1/products/{productId}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
                    throw new ProductNotFoundException("Product not found: " + productId);
                })
                .body(ProductResponse.class);
    }
}
