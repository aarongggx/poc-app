package com.example.greeting.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Validated
public class GreetingController {

    @GetMapping("/greetings")
    public ResponseEntity<GreetingResponse> greeting(
            @RequestParam
            @NotBlank(message = "name must not be blank")
            @Size(max = 100, message = "name must not exceed 100 characters")
            String name, @RequestHeader(value="X-Correlation-ID", required=false) String id) {

        String correlationId = UUID.randomUUID().toString();

        GreetingResponse response = new GreetingResponse(
                "Hello, " + name + "!",
                Instant.now(),
                id == null ? UUID.randomUUID().toString() : id
        );

        return ResponseEntity.ok(response);
    }
}
