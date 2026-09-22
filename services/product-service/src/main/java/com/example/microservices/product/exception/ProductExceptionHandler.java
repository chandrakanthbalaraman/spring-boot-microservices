package com.example.microservices.product.exception;

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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

/**
 * HTTP adapter for product-service domain exceptions.
 * Services keep throwing; this class chooses status codes and hides internals.
 * Extends {@link ResponseEntityExceptionHandler} so Boot's Problem Details
 * advice backs off and validation errors can include a field map.
 */
@RestControllerAdvice
public class ProductExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleNotFound(ProductNotFoundException ex, HttpServletRequest request) {
        return problem(
                HttpStatus.NOT_FOUND,
                "Product not found",
                ex.getMessage(),
                "product-not-found",
                request);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ProblemDetail handleConflict(ProductAlreadyExistsException ex, HttpServletRequest request) {
        return problem(
            HttpStatus.CONFLICT, 
            "Product already exists", 
            ex.getMessage(), 
            "product-already-exists",
            request);
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
