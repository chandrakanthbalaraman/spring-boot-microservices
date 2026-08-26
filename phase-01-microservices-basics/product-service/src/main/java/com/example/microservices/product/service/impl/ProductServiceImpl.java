package com.example.microservices.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.microservices.product.dto.ProductCreateRequest;
import com.example.microservices.product.dto.ProductResponse;
import com.example.microservices.product.entity.Product;
import com.example.microservices.product.exception.ProductAlreadyExistsException;
import com.example.microservices.product.exception.ProductNotFoundException;
import com.example.microservices.product.mapper.ProductMapper;
import com.example.microservices.product.repository.ProductRepository;
import com.example.microservices.product.service.ProductService;

import jakarta.transaction.Transactional;
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
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new ProductAlreadyExistsException("Product with sku: " + request.getSku() + " already exists");
        }
        Product product = Product.create(request.getName(), request.getPrice(), request.getSku());
        return productMapper.toResponse(productRepository.saveAndFlush(product));
    }

    @Override
    public void deleteProduct(String sku) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProduct'");
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productMapper.toResponse(productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found")));
    }

    @Override
    public ProductResponse getProductBySku(String sku) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductBySku'");
    }
}
