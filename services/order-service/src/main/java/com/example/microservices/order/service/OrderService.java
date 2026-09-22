package com.example.microservices.order.service;

import com.example.microservices.order.dto.OrderResponse;
import com.example.microservices.order.dto.OrderCreateRequest;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request, String idempotencyKey);
    OrderResponse getOrder(Long orderId);
}
