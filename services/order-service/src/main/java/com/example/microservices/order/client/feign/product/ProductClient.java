package com.example.microservices.order.client.feign.product;

import com.example.microservices.order.client.dto.ProductResponse;

public interface ProductClient {

    ProductResponse getProductByProductId(Long productId);
}
