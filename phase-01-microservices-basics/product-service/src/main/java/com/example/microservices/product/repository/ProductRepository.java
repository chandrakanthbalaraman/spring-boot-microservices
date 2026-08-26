package com.example.microservices.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
