package com.example.microservices.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.example.microservices.order.client.feign.inventory.InventoryFeignApi;
import com.example.microservices.order.client.feign.product.ProductFeignApi;
import com.example.microservices.order.config.InventoryClientProperties;
import com.example.microservices.order.config.ProductClientProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        InventoryClientProperties.class,
        ProductClientProperties.class
})
@EnableFeignClients(clients = {InventoryFeignApi.class, ProductFeignApi.class})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
