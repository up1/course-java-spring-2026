package com.example.modulith.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/api/order/{amount}")
    public String createOrder(@PathVariable BigDecimal amount) {
        orderService.createOrder(amount);
        return "OK";
    }

}
