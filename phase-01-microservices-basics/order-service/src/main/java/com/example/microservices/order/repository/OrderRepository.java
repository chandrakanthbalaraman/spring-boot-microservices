package com.example.microservices.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.microservices.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.microservices.order.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
