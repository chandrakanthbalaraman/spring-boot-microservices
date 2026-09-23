package com.example.microservices.order.client.feign;

import org.springframework.stereotype.Component;

import com.example.microservices.order.client.InventoryClient;
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

    @Override
    public InventoryResponse getInventoryByProductId(Long productId) {
        try {
            return inventoryFeignApi.getInventoryByProductId(productId);
        } catch (RetryableException e) {
            throw new DownstreamServiceUnavailableException("inventory-service", e.getMessage(), e);
        }
    }

    @Override
    public InventoryResponse reserveStockByProductId(Long productId, int quantity) {
        try {
            return inventoryFeignApi.reserveStockByProductId(productId, quantity);
        } catch (RetryableException e) {
            throw new DownstreamServiceUnavailableException("inventory-service", e.getMessage(), e);
        }
    }

    @Override
    public InventoryResponse releaseStockByProductId(Long productId, int quantity) {
        try {
            return inventoryFeignApi.releaseStockByProductId(productId, quantity);
        } catch (RetryableException e) {
            throw new DownstreamServiceUnavailableException("inventory-service", e.getMessage(), e);
        }
    }
}
