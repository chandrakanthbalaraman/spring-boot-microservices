package com.example.microservices.inventory.service;

import com.example.microservices.inventory.dto.InventoryCreateRequest;
import com.example.microservices.inventory.dto.InventoryResponse;

public interface InventoryService {
    InventoryResponse createInventory(InventoryCreateRequest request);

    InventoryResponse getInventoryByProductId(Long productId);

    InventoryResponse addStock(Long productId, int quantity);

    InventoryResponse reserveStock(Long productId, int quantity);

    InventoryResponse releaseStock(Long productId, int quantity);
}
