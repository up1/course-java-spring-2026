package com.example.thread.demo;

public class AggregatedResponse {
    private final String user;
    private final String orders;
    private final String payment;

    public AggregatedResponse(String user, String orders, String payment) {
        this.user = user;
        this.orders = orders;
        this.payment = payment;
    }
}
