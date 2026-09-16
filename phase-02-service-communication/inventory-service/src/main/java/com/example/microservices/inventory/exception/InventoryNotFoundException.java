package com.example.microservices.inventory.exception;

/**
 * InventoryNotFoundException
 */
public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(Long productId) {
        super("Inventory not found for product ID: " + productId);
    }

    public InventoryNotFoundException(String message) {
        super(message);
    }
}