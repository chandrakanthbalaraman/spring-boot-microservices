package com.example.microservices.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.microservices.product.dto.ProductCreateRequest;
import com.example.microservices.product.dto.ProductResponse;
import com.example.microservices.product.mapper.ProductMapper;
import com.example.microservices.product.repository.ProductRepository;
import com.example.microservices.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        // TODO: map request → entity, save, return toResponse
        throw new UnsupportedOperationException("Not implemented");
    }
}
