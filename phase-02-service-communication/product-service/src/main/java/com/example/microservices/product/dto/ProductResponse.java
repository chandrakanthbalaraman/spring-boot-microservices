package com.example.microservices.product.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.example.microservices.product.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private String sku;

    private ProductStatus status;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long version;
}
