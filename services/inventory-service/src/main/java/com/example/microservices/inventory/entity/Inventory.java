package com.example.microservices.inventory.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

import com.example.microservices.inventory.exception.InsufficientInventoryException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "inventories", uniqueConstraints = @UniqueConstraint(name = "uk_inventories_product_id", columnNames = "product_id"))
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    private Long version;

    public static Inventory create(Long productId, int availableQuantity) {
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("Available quantity must be greater than 0");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required");
        }
        return Inventory.builder()
                .productId(productId)
                .availableQuantity(availableQuantity)
                .reservedQuantity(0)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public void addStock(int quantity) {
        validatePositive(quantity);
        this.availableQuantity += quantity;
        touch();
    }

    public void removeStock(int quantity) {
        validatePositive(quantity);
        this.availableQuantity -= quantity;
        touch();
    }

    public void update(int availableQuantity) {
        validatePositive(availableQuantity);
        this.availableQuantity = availableQuantity;
        touch();
    }

    public void reserve(int quantity) {
        validatePositive(quantity);
        if (quantity > this.availableQuantity) {
            throw new InsufficientInventoryException(
                this.productId, quantity, this.availableQuantity
            );
        }
        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
        touch();
    }

    public void release(int quantity) {
        validatePositive(quantity);
        if (quantity > this.reservedQuantity) {
            throw new IllegalArgumentException("Not enough stock reserved");
        }
        this.reservedQuantity -= quantity;
        this.availableQuantity += quantity;
        touch();
    }

    private void validatePositive(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }
    }

    
    private void touch() {
        this.updatedAt = OffsetDateTime.now();
    }
    
}
