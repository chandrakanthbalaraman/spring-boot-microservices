package com.example.microservices.order.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private Long id;

    private Long productId;

    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal subtotal;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
