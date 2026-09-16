package com.example.microservices.order.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    /**
     * Opaque customer identifier. There is no customer-service in Phase 1,
     * and this is not a database foreign key (database-per-service).
     */
    @NotNull
    @Positive
    private Long customerId;

    @NotEmpty
    private List<@Valid OrderItemRequest> items;
}
