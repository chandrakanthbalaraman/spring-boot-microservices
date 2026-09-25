package com.example.microservices.order.client.feign.inventory;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.microservices.order.client.dto.InventoryResponse;
import com.example.microservices.order.exception.DownstreamServiceUnavailableException;

import feign.RetryableException;
import lombok.RequiredArgsConstructor;

/**
 * OrderServiceImpl depends on {@link InventoryClient}. This adapter delegates
 * to the Eureka-backed Feign API.
 */
@Component
@RequiredArgsConstructor
public class InventoryFeignClient implements InventoryClient {

    private final InventoryFeignApi inventoryFeignApi;
    private final InventoryInstancePorts inventoryInstancePorts;

    @Override
    public InventoryResponse getInventoryByProductId(Long productId) {
        try {
            return body(inventoryFeignApi.getInventoryByProductId(productId));
        } catch (RetryableException e) {
            throw new DownstreamServiceUnavailableException("inventory-service", e.getMessage(), e);
        }
    }

    @Override
    public InventoryResponse reserveStockByProductId(Long productId, int quantity) {
        try {
            return body(inventoryFeignApi.reserveStockByProductId(productId, quantity));
        } catch (RetryableException e) {
            throw new DownstreamServiceUnavailableException("inventory-service", e.getMessage(), e);
        }
    }

    @Override
    public InventoryResponse releaseStockByProductId(Long productId, int quantity) {
        try {
            return body(inventoryFeignApi.releaseStockByProductId(productId, quantity));
        } catch (RetryableException e) {
            throw new DownstreamServiceUnavailableException("inventory-service", e.getMessage(), e);
        }
    }

    private InventoryResponse body(ResponseEntity<InventoryResponse> response) {
        inventoryInstancePorts.add(response.getHeaders().getFirst(InventoryInstancePorts.HEADER));
        return response.getBody();
    }
}
