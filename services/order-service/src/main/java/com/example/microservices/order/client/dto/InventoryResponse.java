package com.example.microservices.order.client.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InventoryResponse {
    private Long id;
    private Long productId;
    private int availableQuantity;
    private int reservedQuantity;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long version;
}