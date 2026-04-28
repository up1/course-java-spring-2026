package com.example.demowebflux.product;

import java.time.Instant;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demowebflux.web.ApiError;

import reactor.core.publisher.Mono;

@Order(0)
@RestControllerAdvice(assignableTypes = ProductController.class)
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public Mono<ResponseEntity<ApiError>> handleNotFound(ProductNotFoundException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(HttpStatus.NOT_FOUND.value(), "Not Found",
                        List.of(ex.getMessage()), Instant.now())));
    }
}
