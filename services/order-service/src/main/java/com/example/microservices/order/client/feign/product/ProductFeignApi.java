package com.example.microservices.order.client.feign.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.microservices.order.client.dto.ProductResponse;

/**
 * Declarative product client. No {@code url}: {@code name} is the Eureka
 * application id, resolved by Spring Cloud LoadBalancer.
 */
@FeignClient(name = "product-service", configuration = ProductFeignConfig.class)
public interface ProductFeignApi {

    @GetMapping("/api/v1/products/{productId}")
    ProductResponse getProductByProductId(@PathVariable Long productId);
}
