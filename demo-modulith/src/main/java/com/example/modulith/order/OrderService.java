package com.example.modulith.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void createOrder(BigDecimal amount) {
        String orderId = UUID.randomUUID().toString();
        System.out.println("[Order] Created order: " + orderId);

        // Publish event. Spring Modulith's Event Publication Registry
        // intercepts this to guarantee delivery even if the app crashes.
        eventPublisher.publishEvent(new OrderPlacedEvent(orderId, amount));
    }
}

