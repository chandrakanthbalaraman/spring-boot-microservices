package com.example.microservices.order.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.microservices.order.dto.OrderCreateRequest;
import com.example.microservices.order.dto.OrderResponse;
import com.example.microservices.order.entity.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public static OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .orderAmount(order.getOrderAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .version(order.getVersion())
                .items(order.getItems() == null
                        ? List.of()
                        : order.getItems().stream()
                                .map(OrderItemMapper::toResponse)
                                .toList())
                .build();
    }

    public static Order toEntity(OrderCreateRequest request) {
        return Order.create(request.getCustomerId());
    }
}
