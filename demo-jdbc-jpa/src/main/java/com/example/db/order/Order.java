package com.example.db.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Order(
        Long id,
        String customerName,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}
