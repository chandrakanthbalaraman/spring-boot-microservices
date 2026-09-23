package com.example.microservices.order.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.microservices.order.client.dto.InventoryResponse;
import com.example.microservices.order.config.InventoryFeignConfig;

/**
 * Declarative inventory client. No {@code url}: {@code name} is the Eureka
 * application id, resolved by Spring Cloud LoadBalancer.
 *
 * <p>Timeouts and the error decoder live in {@link InventoryFeignConfig}, not on this interface.
 */
@FeignClient(name = "inventory-service", configuration = InventoryFeignConfig.class)
public interface InventoryFeignApi {
    
    @GetMapping("/api/v1/inventories/{productId}")
    InventoryResponse getInventoryByProductId(@PathVariable Long productId);

    @PostMapping("/api/v1/inventories/{productId}/reserve-stock")
    InventoryResponse reserveStockByProductId(@PathVariable Long productId, @RequestParam int quantity);

    @PostMapping("/api/v1/inventories/{productId}/release-stock")
    InventoryResponse releaseStockByProductId(@PathVariable Long productId, @RequestParam int quantity);
}
