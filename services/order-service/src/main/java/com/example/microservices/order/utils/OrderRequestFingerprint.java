package com.example.microservices.order.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;

import com.example.microservices.order.dto.OrderCreateRequest;
import com.example.microservices.order.dto.OrderItemRequest;

public final class OrderRequestFingerprint {

    private OrderRequestFingerprint() {
    }

    public static String fingerprint(OrderCreateRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("customerId=").append(request.getCustomerId());

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            request.getItems().stream()
                    .sorted(Comparator.comparing(OrderItemRequest::getProductId)
                            .thenComparing(OrderItemRequest::getQuantity))
                    .forEach(item -> sb.append('|')
                            .append(item.getProductId())
                            .append(':')
                            .append(item.getQuantity()));
        }

        return sha256Hex(sb.toString());
    }

    private static String sha256Hex(String normalized) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(normalized.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
