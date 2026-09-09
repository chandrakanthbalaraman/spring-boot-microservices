package com.example.microservices.inventory.exception;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(Long productId, int requestedQuantity, int availableQuantity) {
        super(
            "Insufficient inventory for product %d. Requested: %d, available: %d"
                    .formatted(productId, requestedQuantity, availableQuantity)
            );
    }
}
