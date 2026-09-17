package com.example.microservices.order.exception;

public class DownstreamServiceUnavailableException extends DownstreamServiceException {

    public DownstreamServiceUnavailableException(String serviceName, String message) {
        super(serviceName, message);
    }

    public DownstreamServiceUnavailableException(String serviceName, String message, Throwable cause) {
        super(serviceName, message, cause);
    }
}
