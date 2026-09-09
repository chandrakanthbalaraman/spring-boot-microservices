package com.example.microservices.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.microservices.product.dto.ProductCreateRequest;
import com.example.microservices.product.dto.ProductResponse;
import com.example.microservices.product.dto.ProductUpdateRequest;
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
    @Transactional
    public void deleteProduct(Long id) {
        Product product = requireProduct(id);
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productMapper.toResponse(requireProduct(id));
    }

    @Override
    public ProductResponse getProductBySku(String sku) {
        return productMapper.toResponse(productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product with sku: " + sku + " not found")));
    }

    private Product requireProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = requireProduct(id);
        product.update(request.getName(), request.getPrice());
        return productMapper.toResponse(productRepository.saveAndFlush(product));
    }
}
