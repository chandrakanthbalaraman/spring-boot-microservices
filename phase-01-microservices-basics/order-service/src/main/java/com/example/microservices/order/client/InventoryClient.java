package com.example.microservices.order.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.microservices.order.client.dto.InventoryResponse;
import com.example.microservices.order.exception.InsufficientInventoryException;
import com.example.microservices.order.exception.InventoryNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(@Qualifier("inventoryRestClient") RestClient restClient) {
        log.info("Creating inventory rest client");
        this.restClient = restClient;
    }

    public InventoryResponse getInventoryByProductId(Long productId) {
        return restClient.get()
                .uri("/api/v1/inventories/{productId}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
                    throw new InventoryNotFoundException("Inventory not found: " + productId);
                })
                .body(InventoryResponse.class);
    }

    public InventoryResponse reserveStockByProductId(Long productId, int quantity) {
        return restClient.post()
                .uri("/api/v1/inventories/{productId}/reserve-stock?quantity={quantity}",
                        productId, quantity)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
                    throw new InventoryNotFoundException("Inventory not found: " + productId);
                })
                .onStatus(HttpStatus.BAD_REQUEST::equals, (request, response) -> {
                    throw new InsufficientInventoryException(
                            "Insufficient inventory for product: " + productId);
                })
                .body(InventoryResponse.class);
    }

    public InventoryResponse releaseStockByProductId(Long productId, int quantity) {
        return restClient.post()
                .uri("/api/v1/inventories/{productId}/release-stock?quantity={quantity}",
                        productId, quantity)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
                    throw new InventoryNotFoundException("Inventory not found: " + productId);
                })
                .onStatus(HttpStatus.BAD_REQUEST::equals, (request, response) -> {
                    throw new InsufficientInventoryException(
                            "Insufficient inventory for product: " + productId);
                })
                .body(InventoryResponse.class);
    }
}
