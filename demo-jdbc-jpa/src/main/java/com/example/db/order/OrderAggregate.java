package com.example.db.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderAggregate(
        Long id,
        String customerName,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<OrderItem> items
) {
}
