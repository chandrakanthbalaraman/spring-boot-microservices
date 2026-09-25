package com.example.microservices.order.client.feign.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Ports of the inventory instances that served this HTTP request.
 * Feign drops response headers when the method returns a body DTO, so the
 * client records {@code X-Instance-Port} here and the order controller copies it out.
 */
@Component
@RequestScope
public class InventoryInstancePorts {
    public static final String HEADER = "X-Instance-Port";

    private final List<String> ports = new ArrayList<>();

    public void add(String port) {
        if (port != null && !port.isBlank()) {
            ports.add(port);
        }
    }

    public List<String> ports() {
        return List.copyOf(ports);
    }
}
