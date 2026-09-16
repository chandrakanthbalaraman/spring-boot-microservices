package com.example.microservices.order.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.microservices.order.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /** Opaque customer id — not a FK; there is no customer-service in Phase 1. */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal orderAmount;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    private Long version;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    public static Order create(Long customerId) {
        return Order.builder()
            .customerId(customerId)
            .status(OrderStatus.PENDING)
            .orderAmount(BigDecimal.ZERO)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
    }

    public void addItem(
        Long productId,
        int quantity,
        BigDecimal unitPrice
    ) {
        OrderItem item = OrderItem.create(productId, quantity, unitPrice);
        item.setOrder(this);
        items.add(item);
        if (orderAmount == null) {
            orderAmount = BigDecimal.ZERO;
        }
        orderAmount = orderAmount.add(item.getSubtotal());
        touch();
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not in pending state");
        }
        status = OrderStatus.CONFIRMED;
        touch();
    }

    public void cancel() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not in pending state");
        }
        status = OrderStatus.CANCELLED;
        touch();
    }

    public void ship() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order is not in confirmed state");
        }
        status = OrderStatus.SHIPPED;
        touch();
    }

    public void deliver() {
        if (status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order is not in shipped state");
        }
        status = OrderStatus.DELIVERED;
        touch();
    }

    public void complete() {
        if (status != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Order is not in delivered state");
        }
        status = OrderStatus.COMPLETED;
        touch();
    }

    private void touch() {
        updatedAt = OffsetDateTime.now();
    }
}
