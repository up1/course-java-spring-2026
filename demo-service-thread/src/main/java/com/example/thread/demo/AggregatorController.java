package com.example.thread.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AggregatorController {

    private final AggregatorService service;

    public AggregatorController(AggregatorService service) {
        this.service = service;
    }

    @GetMapping("/aggregate")
    public AggregatedResponse aggregate() throws Exception {
        log.info("Controller running on {}", Thread.currentThread());
        return service.aggregate();
    }
}