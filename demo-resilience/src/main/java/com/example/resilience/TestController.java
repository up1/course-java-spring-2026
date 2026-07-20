package com.example.resilience;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestService testService;

    public TestController(final TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/testing/retry")
    public String callRetry() {
        return testService.callRemoteAPIWithRetry();
    }

    @GetMapping("/testing/limit")
    public String callLimit() {
        return testService.callRemoteAPIWithLimit();
    }

}
