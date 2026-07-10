package com.example.modulith.order;

import java.math.BigDecimal;

public record OrderPlacedEvent(String orderId, BigDecimal amount) {}
