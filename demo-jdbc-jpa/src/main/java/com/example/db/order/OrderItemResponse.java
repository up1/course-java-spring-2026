package com.example.db.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
}
