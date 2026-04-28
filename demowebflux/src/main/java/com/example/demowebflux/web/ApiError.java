package com.example.demowebflux.web;

import java.time.Instant;
import java.util.List;

public record ApiError(int status, String error, List<String> messages, Instant timestamp) {
}
