package com.example.microservices.order.client.webclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.example.microservices.order.client.ProductClient;
import com.example.microservices.order.client.dto.ProductResponse;
import com.example.microservices.order.exception.DownstreamServiceException;
import com.example.microservices.order.exception.DownstreamServiceUnavailableException;
import com.example.microservices.order.exception.ProductNotFoundException;

import reactor.core.publisher.Mono;

/**
 * Slice D adapter. Component bean name {@code webProductClient} must not collide
 * with the {@code productWebClient} {@link WebClient} bean in {@code WebClientConfig}.
 */
@Component("webProductClient")
@Primary
public class ProductWebClient implements ProductClient {

    private static final String SERVICE = "product-service";

    private final WebClient webClient;

    public ProductWebClient(@Qualifier("productWebClient") WebClient productWebClient) {
        this.webClient = productWebClient;
    }

    @Override
    public ProductResponse getProductByProductId(Long productId) {
        try {
        return webClient.get()
                .uri("/api/v1/products/{productId}", productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> 
                    response.bodyToMono(String.class)
                    .flatMap(body -> {
                        if (response.statusCode().value() == 404) {
                            return Mono.error(new ProductNotFoundException("Product not found: " + productId));
                        }
                        return Mono.error(new DownstreamServiceException(SERVICE, SERVICE + " returned an unexpected error: " + body));
                    }))
                .onStatus(HttpStatusCode::is5xxServerError, response -> 
                    response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new DownstreamServiceUnavailableException(SERVICE, SERVICE + " returned an unexpected error: " + body))))
                .bodyToMono(ProductResponse.class)
                .block();
        } catch (WebClientRequestException e) {
            throw new DownstreamServiceUnavailableException(SERVICE, SERVICE + " unavailable: " + e.getMessage(), e);
        }
    }
}
