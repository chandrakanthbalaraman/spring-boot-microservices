package com.example.microservices.order.client;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.example.microservices.order.client.dto.InventoryResponse;
import com.example.microservices.order.exception.DownstreamServiceException;
import com.example.microservices.order.exception.DownstreamServiceUnavailableException;
import com.example.microservices.order.exception.InsufficientInventoryException;
import com.example.microservices.order.exception.InventoryNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryClient {

    private static final String SERVICE = "inventory-service";

    private final RestClient restClient;

    public InventoryClient(@Qualifier("inventoryRestClient") RestClient restClient) {
        log.info("Creating inventory rest client");
        this.restClient = restClient;
    }

    public InventoryResponse getInventoryByProductId(Long productId) {
        return execute(() -> restClient.get()
                .uri("/api/v1/inventories/{productId}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
                    throw new InventoryNotFoundException("Inventory not found: " + productId);
                })
                .body(InventoryResponse.class));
    }

    public InventoryResponse reserveStockByProductId(Long productId, int quantity) {
        return execute(() -> restClient.post()
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
                .body(InventoryResponse.class));
    }

    public InventoryResponse releaseStockByProductId(Long productId, int quantity) {
        return execute(() -> restClient.post()
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
                .body(InventoryResponse.class));
    }

    /**
     * Transport failures only. Domain exceptions from onStatus (404 / 400) skip these catches.
     */
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
