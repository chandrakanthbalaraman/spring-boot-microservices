package com.example.microservices.inventory.mapper;

import org.springframework.stereotype.Component;

import com.example.microservices.inventory.dto.InventoryCreateRequest;
import com.example.microservices.inventory.dto.InventoryResponse;
import com.example.microservices.inventory.entity.Inventory;

@Component
public class InventoryMapper {

    public InventoryResponse toResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .version(inventory.getVersion())
                .build();
    }

    public Inventory toEntity(InventoryCreateRequest request) {
        return Inventory.create(request.getProductId(), request.getAvailableQuantity());
    }
}
