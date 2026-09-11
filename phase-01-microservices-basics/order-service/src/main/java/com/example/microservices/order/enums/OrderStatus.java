package com.example.microservices.order.enums;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED;

    public static OrderStatus fromString(String status) {
        return OrderStatus.valueOf(status.toUpperCase());
    }
}
