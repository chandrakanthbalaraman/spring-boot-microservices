package com.example.microservices.order.service.impl;

import com.example.microservices.order.service.OrderService;

import lombok.RequiredArgsConstructor;

import com.example.microservices.order.repository.OrderRepository;
import com.example.microservices.order.entity.Order;
import com.example.microservices.order.dto.OrderResponse;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.microservices.order.client.InventoryClient;
import com.example.microservices.order.client.ProductClient;
import com.example.microservices.order.client.dto.InventoryResponse;
import com.example.microservices.order.client.dto.ProductResponse;
import com.example.microservices.order.dto.OrderCreateRequest;
import com.example.microservices.order.dto.OrderItemRequest;
import com.example.microservices.order.mapper.OrderMapper;
import com.example.microservices.order.exception.OrderNotFoundException;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        // Create Order
        Order order = Order.create(request.getCustomerId()); 
        
        //Compensate Failure
        List<OrderItemRequest> processedItems = request.getItems();

        try {
            // Add Items to Order
            for (OrderItemRequest itemRequest : request.getItems()) {
                // Get Product from Product Service
                ProductResponse product = productClient.getProductByProductId(itemRequest.getProductId());
                // Reserve Stock from Inventory Service
                inventoryClient.reserveStockByProductId(itemRequest.getProductId(), itemRequest.getQuantity());
                // Add Item to Processed Items
                processedItems.add(itemRequest);
                // Add Item to Order
                order.addItem(
                    itemRequest.getProductId(),
                    itemRequest.getQuantity(),
                    product.getPrice()
                );

                // Confirm Order
                order.confirm();

                // Save Order
                Order savedOrder = orderRepository.save(order);
                return OrderMapper.toResponse(savedOrder);
            }
        } catch (Exception createException) {
            // Compensate Failure
            for (OrderItemRequest itemRequest : processedItems) {
                try {
                    inventoryClient.releaseStockByProductId(itemRequest.getProductId(), itemRequest.getQuantity());
                } catch (Exception releaseException) {
                    // Compensate Failure
                    throw releaseException;
                }
            }   
            throw createException;
        }
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        return OrderMapper.toResponse(order);
    }
}
