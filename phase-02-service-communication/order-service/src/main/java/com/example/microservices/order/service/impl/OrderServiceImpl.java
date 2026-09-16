package com.example.microservices.order.service.impl;

import com.example.microservices.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.microservices.order.repository.OrderRepository;
import com.example.microservices.order.entity.Order;
import com.example.microservices.order.dto.OrderResponse;

import java.util.ArrayList;
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


@Slf4j(topic = "OrderServiceImpl")
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        log.info("Creating order for customer: {}", request.getCustomerId());
        // Create Order
        Order order = Order.create(request.getCustomerId()); 
        log.info("Order created: {}", order.getId());
        //Compensate Failure
        List<OrderItemRequest> processedItems = new ArrayList<>();
        
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
                log.info("Item added to order: {}", itemRequest.getProductId());
                
            }
            // Confirm Order
            order.confirm();

            log.info("Order confirmed: {}", order.getId());
            // Save Order
            Order savedOrder = orderRepository.save(order);
            log.info("Order saved: {}", savedOrder.getId());
            return OrderMapper.toResponse(savedOrder);
        } catch (Exception createException) {
            log.error("Compensating failure for order: {}", order.getId(), createException);
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
        Order order = orderRepository.findWithItemsById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        return OrderMapper.toResponse(order);
    }
}
