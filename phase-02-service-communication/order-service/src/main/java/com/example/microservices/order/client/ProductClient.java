package com.example.microservices.order.client;

import com.example.microservices.order.client.dto.ProductResponse;

public interface ProductClient {

    ProductResponse getProductByProductId(Long productId);
}
