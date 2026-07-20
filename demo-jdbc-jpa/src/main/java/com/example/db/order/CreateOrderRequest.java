package com.example.db.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank
        String customerName,

        @NotEmpty
        List<@Valid CreateOrderItemRequest> items
) {
}
