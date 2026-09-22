package com.example.microservices.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservices.inventory.dto.InventoryCreateRequest;
import com.example.microservices.inventory.dto.InventoryResponse;
import com.example.microservices.inventory.service.InventoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory( @Valid @RequestBody InventoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createInventory(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }
    
    @PostMapping("/{productId}/add-stock")
    public ResponseEntity<InventoryResponse> addStock(@PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.addStock(productId, quantity));
    }

    @PostMapping("/{productId}/reserve-stock")
    public ResponseEntity<InventoryResponse> reserveStock(@PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.reserveStock(productId, quantity));
    }

    @PostMapping("/{productId}/release-stock")
    public ResponseEntity<InventoryResponse> releaseStock(@PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.releaseStock(productId, quantity));
    }
}
