package com.example.microservices.inventory.component;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.example.microservices.inventory.config.ResilienceLearningProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryDelaySimulator {
    private final ResilienceLearningProperties properties;

    public void beforeInventoryRead() {
        Duration delay = properties.inventoryGetDelay();
        
        if (delay == null || delay.isZero() || delay.isNegative()) {
            return;
        }

        // Sleep for the configured duration.
        // If interrupted, restore the interrupt flag.
        try {
            Thread.sleep(delay.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
