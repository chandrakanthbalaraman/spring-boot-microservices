package com.example.microservices.order.service;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import com.example.microservices.order.client.dto.InventoryResponse;
import com.example.microservices.order.client.feign.inventory.InventoryClient;
import com.example.microservices.order.exception.DownstreamServiceException;
import com.example.microservices.order.exception.DownstreamServiceUnavailableException;
import com.example.microservices.order.exception.InsufficientInventoryException;
import com.example.microservices.order.exception.InventoryNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryAvailabilityService {
    private static final String BACKEND = "inventoryAvailability";
    private final InventoryClient inventoryClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    
    public InventoryResponse getInventoryAvailability(Long productId) {
        return circuitBreakerFactory.create(BACKEND)
            .run(
                () -> inventoryClient.getInventoryByProductId(productId), 
                failure -> mapFailure(productId, failure)
        );
    }

    private InventoryResponse mapFailure(Long productId, Throwable failure) {
        log.error("Failed to check inventory availability for product ID: {}", productId, failure);

        // TimeLimiter and the executor wrap the Feign exception. Walk the cause
        // chain so a 404 stays a 404 after Future.get().
        InventoryNotFoundException notFound = findCause(failure, InventoryNotFoundException.class);
        if (notFound != null) {
            throw notFound;
        }

        InsufficientInventoryException insufficient = findCause(failure, InsufficientInventoryException.class);
        if (insufficient != null) {
            throw insufficient;
        }

        DownstreamServiceException downstream = findCause(failure, DownstreamServiceException.class);
        if (downstream != null) {
            throw downstream;
        }

        throw new DownstreamServiceUnavailableException(
                "inventory-service",
                "inventory-service unavailable",
                failure);
    }

    private static <T extends Throwable> T findCause(Throwable failure, Class<T> type) {
        Throwable current = failure;
        while (current != null) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getCause();
        }
        return null;
    }
}
