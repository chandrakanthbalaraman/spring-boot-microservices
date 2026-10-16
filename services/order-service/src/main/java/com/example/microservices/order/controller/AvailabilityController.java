package com.example.microservices.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservices.order.client.dto.InventoryResponse;
import com.example.microservices.order.service.InventoryAvailabilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/availability")
@RequiredArgsConstructor
@Slf4j
public class AvailabilityController {
    private final InventoryAvailabilityService inventoryAvailabilityService;

    @Operation(summary = "Get inventory availability by product ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory availability found", content = @Content(schema = @Schema(implementation = InventoryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Inventory availability not found"),
        @ApiResponse(responseCode = "503", description = "Inventory service unavailable"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryAvailability(
            @Parameter(name = "productId", description = "Product ID", example = "1")
            @PathVariable Long productId) {
        return ResponseEntity.ok(inventoryAvailabilityService.getInventoryAvailability(productId));
    }
}
