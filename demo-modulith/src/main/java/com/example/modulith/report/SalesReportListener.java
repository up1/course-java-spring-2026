package com.example.modulith.report;

import com.example.modulith.order.OrderPlacedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class SalesReportListener {

    private final OrderAnalytics analytics;

    public SalesReportListener(OrderAnalytics analytics) {
        this.analytics = analytics;
    }

    // Listens to the event
    // Executed in an isolated thread, completely independent of the payment outcome
    @ApplicationModuleListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        System.out.println("[Report] Received event for order: " + event.orderId());
        analytics.recordSale(event.orderId(), event.amount());
    }
}
