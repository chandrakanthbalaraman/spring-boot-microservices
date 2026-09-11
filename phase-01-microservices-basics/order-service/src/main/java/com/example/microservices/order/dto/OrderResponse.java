package com.example.microservices.order.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.microservices.order.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private Long customerId;

    private OrderStatus status;

    private BigDecimal orderAmount;

    @Builder.Default
    private List<OrderItemResponse> items = new ArrayList<>();

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private Long version;
}
