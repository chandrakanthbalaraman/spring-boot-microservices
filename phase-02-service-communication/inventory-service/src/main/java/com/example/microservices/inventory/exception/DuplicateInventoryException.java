package com.example.microservices.inventory.exception;

public class DuplicateInventoryException extends RuntimeException {
    public DuplicateInventoryException(Long productId) {
        super("Inventory already exists for product ID: " + productId);
    }

    public DuplicateInventoryException(String message) {
        super(message);
    }
}
