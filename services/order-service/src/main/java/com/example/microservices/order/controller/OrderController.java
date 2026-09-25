package com.example.microservices.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservices.order.client.feign.inventory.InventoryInstancePorts;
import com.example.microservices.order.dto.OrderCreateRequest;
import com.example.microservices.order.dto.OrderResponse;
import com.example.microservices.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final InventoryInstancePorts inventoryInstancePorts;

    @PostMapping
    @Operation(summary = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "409", description = "Idempotency key conflict")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @Parameter(name = "Idempotency-Key", description = "Idempotency key")
    @Tag(name = "Order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest, 
        @RequestHeader("Idempotency-Key") String idempotencyKey) {
        OrderResponse orderResponse = orderService.createOrder(orderCreateRequest, idempotencyKey);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.CREATED);
        for (String port : inventoryInstancePorts.ports()) {
            builder.header(InventoryInstancePorts.HEADER, port);
        }
        return builder.body(orderResponse);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get an order by ID")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found") 
    @Tag(name = "Order")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
}
