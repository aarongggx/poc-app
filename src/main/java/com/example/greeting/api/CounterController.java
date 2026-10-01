package com.example.greeting.api;

import com.example.greeting.service.CounterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/counter")
public class CounterController {

    private final CounterService service;

    public CounterController(CounterService service) {
        this.service = service;
    }

    @PostMapping
    public Map<String, Long> increment() {
        return Map.of("value", service.increment());
    }

    @GetMapping
    public Map<String, Long> get() {
        return Map.of("value", service.get());
    }
}
