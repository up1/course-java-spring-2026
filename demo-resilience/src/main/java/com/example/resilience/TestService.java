package com.example.resilience;

import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TestService {
    private AtomicInteger counter = new AtomicInteger(1);

    @Retryable(
            includes = {RuntimeException.class},
            maxRetries = 4,
            delayString = "1000ms",
            multiplier = 1.5,
            maxDelay = 3000
    )
    public String callRemoteAPIWithRetry() {
        System.out.println("Start callRemote() in test service " + counter.getAndIncrement());
        throw new RuntimeException("Remote API failure !!");
    }

    @ConcurrencyLimit(
            limit = 5,      // Only 5 threads
            policy = ConcurrencyLimit.ThrottlePolicy.REJECT
    )
    public String callRemoteAPIWithLimit() {
        return "OK";
    }
}