package com.example.thread.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
@Slf4j
public class AggregatorService {

    public AggregatedResponse aggregate() throws Exception {
        log.info("Service aggregate() running on {}", Thread.currentThread());

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futureUser = executor.submit(this::fetchUser);
            var futureOrders = executor.submit(this::fetchOrders);
            var futurePay = executor.submit(this::fetchPayments);

            String user = futureUser.get();   // virtual thread blocks, carrier freed
            String orders = futureOrders.get();
            String payment = futurePay.get();

            return new AggregatedResponse(user, orders, payment);
        }
    }

    // Simulate slow api
    private String fetchUser() throws InterruptedException {
        log.info("fetchUser running on {}", Thread.currentThread());
        Thread.sleep(2000);  // simulates a slow REST call
        return "User: User01";
    }

    private String fetchOrders() throws InterruptedException {
        log.info("fetchOrders running on {}", Thread.currentThread());
        Thread.sleep(2000); // simulates a slow REST call
        return "Orders: [Laptop, Notebook]";
    }

    private String fetchPayments() throws InterruptedException {
        log.info("fetchPayments running on {}", Thread.currentThread());
        Thread.sleep(2000); // simulates a slow REST call
        return "Payment: 1250";
    }
}
