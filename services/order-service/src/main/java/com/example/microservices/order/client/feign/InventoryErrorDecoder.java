package com.example.microservices.order.client.feign;

import feign.Response;
import feign.codec.ErrorDecoder;

import com.example.microservices.order.exception.DownstreamServiceException;
import com.example.microservices.order.exception.InsufficientInventoryException;
import com.example.microservices.order.exception.InventoryNotFoundException;

/**
 * Feign analog of RestClient {@code onStatus} + the HTTP branch of {@code execute()}.
 * Connect/read failures never arrive here — those throw {@code feign.RetryableException}.
 */
public class InventoryErrorDecoder implements ErrorDecoder {

    private static final String SERVICE = "inventory-service";

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404:
                return new InventoryNotFoundException(SERVICE + " returned a 404");
            case 400:
                return new InsufficientInventoryException(SERVICE + " returned a 400");
            default:
                return new DownstreamServiceException(SERVICE, SERVICE + " returned an unexpected error");
        }
    }
}
