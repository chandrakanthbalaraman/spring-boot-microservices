package com.example.microservices.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.example.microservices.order.client.feign.InventoryFeignApi;
import com.example.microservices.order.client.feign.ProductFeignApi;

@SpringBootApplication
@EnableFeignClients(clients = {InventoryFeignApi.class, ProductFeignApi.class})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
