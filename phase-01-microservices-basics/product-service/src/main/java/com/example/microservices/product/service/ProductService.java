package com.example.microservices.product.service;

import java.util.List;

import com.example.microservices.product.dto.ProductCreateRequest;
import com.example.microservices.product.dto.ProductResponse;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse createProduct(ProductCreateRequest request);

    ProductResponse getProductBySku(String sku);

    void deleteProduct(String sku);
}
