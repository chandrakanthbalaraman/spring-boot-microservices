package com.example.microservices.order.service.impl;

import com.example.microservices.order.service.OrderService;
import com.example.microservices.order.utils.OrderRequestFingerprint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.microservices.order.repository.OrderRepository;
import com.example.microservices.order.entity.Order;
import com.example.microservices.order.dto.OrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.microservices.order.client.InventoryClient;
import com.example.microservices.order.client.ProductClient;
import com.example.microservices.order.client.dto.ProductResponse;
import com.example.microservices.order.client.webclient.ProductWebClient;
import com.example.microservices.order.dto.OrderCreateRequest;
import com.example.microservices.order.dto.OrderItemRequest;
import com.example.microservices.order.mapper.OrderMapper;
import com.example.microservices.order.exception.IdempotencyConflictException;
import com.example.microservices.order.exception.OrderNotFoundException;


@Slf4j(topic = "OrderServiceImpl")
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final ProductWebClient productWebClient;

    @Override
    public OrderResponse createOrder(OrderCreateRequest request, String idempotencyKey) {
        String fingerprint = OrderRequestFingerprint.fingerprint(request); // sha-256 of canonical payload
        log.info("Fingerprint: {}", fingerprint);
        log.info("Idempotency key: {}", idempotencyKey);
        return orderRepository.findByIdempotencyKey(idempotencyKey)
            .map(existing -> replayOrConflict(existing, fingerprint))
            .orElseGet(() -> createNew(request, idempotencyKey, fingerprint));
    }

    private OrderResponse replayOrConflict(Order existingOrder, String fingerprint) {
        if (!fingerprint.equals(existingOrder.getRequestFingerprint())) {
            log.error("Idempotency key conflict: payload does not match previous request.");
            throw new IdempotencyConflictException("Idempotency key conflict: payload does not match previous request.");
        }
        log.info("Idempotent order request: returning existing order {}", existingOrder.getId());
        return OrderMapper.toResponse(existingOrder);
    }

    private OrderResponse createNew(OrderCreateRequest request, String idempotencyKey, String fingerprint) {
        log.info("Creating new order for customer: {}", request.getCustomerId());
        Order order = Order.create(request.getCustomerId());
        order.setIdempotencyKey(idempotencyKey);
        order.setRequestFingerprint(fingerprint);

        List<OrderItemRequest> processedItems = new ArrayList<>();

        try {
            for (OrderItemRequest itemRequest : request.getItems()) {
                ProductResponse product = productWebClient.getProductByProductId(itemRequest.getProductId());
                inventoryClient.reserveStockByProductId(itemRequest.getProductId(), itemRequest.getQuantity());
                processedItems.add(itemRequest);
                order.addItem(
                    itemRequest.getProductId(),
                    itemRequest.getQuantity(),
                    product.getPrice()
                );
                log.info("Item added to order: {}", itemRequest.getProductId());
            }
            order.confirm();
            log.info("Order confirmed: {}", order.getId());
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
