package com.example.microservices.order.exception;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InsufficientInventoryException.class)
    public ProblemDetail handleConflict(InsufficientInventoryException ex, HttpServletRequest request) {
        return problem(HttpStatus.CONFLICT, "Insufficient inventory", ex.getMessage(), "https://example.com/problems/insufficient-inventory", request);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ProblemDetail handleNotFound(InventoryNotFoundException ex, HttpServletRequest request) {
        return problem(HttpStatus.NOT_FOUND, "Inventory not found", ex.getMessage(), "https://example.com/problems/inventory-not-found", request);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleNotFound(OrderNotFoundException ex, HttpServletRequest request) {
        return problem(HttpStatus.NOT_FOUND, "Order not found", ex.getMessage(), "https://example.com/problems/order-not-found", request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleNotFound(ProductNotFoundException ex, HttpServletRequest request) {
        return problem(HttpStatus.NOT_FOUND, "Product not found", ex.getMessage(), "https://example.com/problems/product-not-found", request);
    }

    @ExceptionHandler(RestClientException.class)
    public ProblemDetail handleRestClientException(RestClientException ex, HttpServletRequest request) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex.getMessage(), "https://example.com/problems/internal-server-error", request);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLock(
            ObjectOptimisticLockingFailureException ex,
            HttpServletRequest request) {
        return problem(
                HttpStatus.CONFLICT,
                "Concurrent Modification",
                "Inventory was modified by another transaction. Please retry.",
                "concurrent-modification",
                request);
    }

    @ExceptionHandler(DownstreamServiceUnavailableException.class)
    public ProblemDetail handleDownstreamServiceUnavailable(DownstreamServiceUnavailableException ex, HttpServletRequest request) {
        return problem(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable", ex.getMessage(), "https://example.com/problems/service-unavailable", request);
    }

    @ExceptionHandler(DownstreamServiceException.class)
    public ProblemDetail handleDownstreamServiceException(DownstreamServiceException ex, HttpServletRequest request) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), "https://example.com/problems/internal-server-error", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        return problem(
                HttpStatus.CONFLICT,
                "Data Integrity Violation",
                "The requested operation violates a database constraint.",
                "data-integrity-violation",
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        ProblemDetail problem = problem(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "One or more request fields are invalid.",
                "validation-failed",
                servletRequest);

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (first, second) -> first)); 

        problem.setProperty("errors", errors);

        return ResponseEntity.status(status).headers(headers).body(problem);
    }


    private ProblemDetail problem(
        HttpStatus status,
        String title,
        String detail,
        String type,
        HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

    problem.setTitle(title);
    problem.setInstance(
            URI.create(request.getRequestURI())
    );
    problem.setType(URI.create(type));

    return problem;
}
}
