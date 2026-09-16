package com.example.microservices.order.mapper;

import org.springframework.stereotype.Component;

import com.example.microservices.order.dto.OrderItemResponse;
import com.example.microservices.order.entity.OrderItem;

@Component
public class OrderItemMapper {

    public static OrderItemResponse toResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .subtotal(orderItem.getSubtotal())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }
}
