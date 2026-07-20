package com.example.db.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateOrderItemRequest(
        @NotBlank
        String productName,

        @Positive
        int quantity,

        @NotNull
        @Positive
        BigDecimal unitPrice
) {
}

