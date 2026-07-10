package com.example.modulith.report;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
class OrderAnalytics {

    private final AtomicInteger totalOrders = new AtomicInteger(0);
    private final ConcurrentHashMap<String, BigDecimal> salesLog = new ConcurrentHashMap<>();

    public void recordSale(String orderId, BigDecimal amount) {
        totalOrders.incrementAndGet();
        salesLog.put(orderId, amount);

        System.out.println("[Report] Metrics updated! Total Orders: " + totalOrders.get()
                + " | Latest revenue added: $" + amount);
    }
}

