package com.example.microservices.inventory.service.impl;

import org.springframework.stereotype.Service;

import com.example.microservices.inventory.dto.InventoryCreateRequest;
import com.example.microservices.inventory.dto.InventoryResponse;
import com.example.microservices.inventory.entity.Inventory;
import com.example.microservices.inventory.exception.DuplicateInventoryException;
import com.example.microservices.inventory.exception.InventoryNotFoundException;
import com.example.microservices.inventory.mapper.InventoryMapper;
import com.example.microservices.inventory.repository.InventoryRepository;
import com.example.microservices.inventory.service.InventoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public InventoryResponse createInventory(InventoryCreateRequest request) {
        if (inventoryRepository.existsByProductId(request.getProductId())) {
            throw new DuplicateInventoryException(request.getProductId());
        }

        Inventory inventory = Inventory.create(request.getProductId(), request.getAvailableQuantity());
        return inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }

    @Override
    public InventoryResponse getInventoryByProductId(Long productId) {
        Inventory inventory = getInventory(productId);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse addStock(Long productId, int quantity) {
        Inventory inventory = getInventory(productId);
        inventory.addStock(quantity);
        return inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }

    @Override
    @Transactional
    public InventoryResponse reserveStock(Long productId, int quantity) {
        Inventory inventory = getInventory(productId);
        inventory.reserve(quantity);
        return inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }

    @Override
    @Transactional
    public InventoryResponse releaseStock(Long productId, int quantity) {
        Inventory inventory = getInventory(productId);
        inventory.release(quantity);
        return inventoryMapper.toResponse(inventoryRepository.save(inventory));
    }
    
    private Inventory getInventory(Long productId) {
        return inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(productId));
    }
}
