package com.example.greeting.api;

import java.time.Instant;

public record GreetingResponse(
        String greeting,
        Instant timestamp,
        String correlationId
) {}
