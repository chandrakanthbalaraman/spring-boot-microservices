package com.example.microservices.order.client.feign.product;

import feign.Response;
import feign.codec.ErrorDecoder;

import com.example.microservices.order.exception.DownstreamServiceException;
import com.example.microservices.order.exception.DownstreamServiceUnavailableException;
import com.example.microservices.order.exception.ProductNotFoundException;

/**
 * Same status map the product WebClient used: 404 stays a domain miss,
 * other 4xx is an unexpected downstream error, 5xx is unavailable.
 * Connect and read failures never arrive here — those are {@code RetryableException}.
 */
public class ProductErrorDecoder implements ErrorDecoder {

    private static final String SERVICE = "product-service";

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        if (status == 404) {
            return new ProductNotFoundException(SERVICE + " returned a 404");
        }
        if (status >= 500) {
            return new DownstreamServiceUnavailableException(
                    SERVICE, SERVICE + " returned an unexpected error");
        }
        return new DownstreamServiceException(SERVICE, SERVICE + " returned an unexpected error");
    }
}
