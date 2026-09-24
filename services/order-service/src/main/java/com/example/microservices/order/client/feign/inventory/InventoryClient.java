package com.example.microservices.order.client.feign.inventory;

import com.example.microservices.order.client.dto.InventoryResponse;

public interface InventoryClient {

    InventoryResponse getInventoryByProductId(Long productId);

    InventoryResponse reserveStockByProductId(Long productId, int quantity);

    InventoryResponse releaseStockByProductId(Long productId, int quantity);
}
