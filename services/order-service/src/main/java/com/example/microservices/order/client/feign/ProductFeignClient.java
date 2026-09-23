package com.example.microservices.order.client.feign;

import org.springframework.stereotype.Component;

import com.example.microservices.order.client.ProductClient;
import com.example.microservices.order.client.dto.ProductResponse;
import com.example.microservices.order.exception.DownstreamServiceUnavailableException;

import feign.RetryableException;
import lombok.RequiredArgsConstructor;

/**
 * OrderServiceImpl depends on {@link ProductClient}. This adapter is the only
 * implementation, and it delegates to the Eureka-backed Feign API.
 */
@Component
@RequiredArgsConstructor
public class ProductFeignClient implements ProductClient {

    private final ProductFeignApi productFeignApi;

    @Override
    public ProductResponse getProductByProductId(Long productId) {
        try {
            return productFeignApi.getProductByProductId(productId);
        } catch (RetryableException e) {
            throw new DownstreamServiceUnavailableException("product-service", e.getMessage(), e);
        }
    }
}
