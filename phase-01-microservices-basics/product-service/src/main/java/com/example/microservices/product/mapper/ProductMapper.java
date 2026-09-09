package com.example.microservices.product.mapper;

import org.springframework.stereotype.Component;

import com.example.microservices.product.dto.ProductCreateRequest;
import com.example.microservices.product.dto.ProductResponse;
import com.example.microservices.product.entity.Product;

/**
 * Manual entity ↔ DTO mapping. Controllers and services must not return {@link Product} as JSON.
 */
@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .sku(product.getSku())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public Product toEntity(ProductCreateRequest request) {
        return Product.create(request.getName(), request.getPrice(), request.getSku());
    }
}
