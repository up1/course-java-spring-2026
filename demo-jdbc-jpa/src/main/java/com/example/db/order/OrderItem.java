package com.example.db.order;

import java.math.BigDecimal;

public record OrderItem(
        Long id,
        Long orderId,
        String productName,
        int quantity,
        BigDecimal unitPrice
) {
    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
