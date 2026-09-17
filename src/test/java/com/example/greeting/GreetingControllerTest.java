package com.example.greeting;

import com.example.greeting.api.GreetingController;
import com.example.greeting.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GreetingController.class)
@Import(SecurityConfig.class) // Import custom SecurityConfig into the test context
class GreetingControllerTest {

    @Autowired
    MockMvc mockMvc;

    // Provide a mock JwtDecoder to satisfy SecurityFilterChain bean creation
    @MockitoBean // Use @MockBean if on Spring Boot 3.3 or earlier
    JwtDecoder jwtDecoder;

    @Test
    void returnsGreeting() throws Exception {
        mockMvc.perform(get("/api/v1/greetings")
                        .param("name", "Jayesh")
                        .with(jwt())) // Simulate a request authenticated with a valid JWT token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.greeting").value("Hello, Jayesh!"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.correlationId").exists())
                .andExpect(header().exists("X-Correlation-Id"));
    }

    @Test
    void rejectsBlankName() throws Exception {
        mockMvc.perform(get("/api/v1/greetings")
                        .param("name", " ")
                        .with(jwt())) // Provide JWT authentication to bypass 401 and reach validation logic
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void rejectsTooLongName() throws Exception {
        String name = "a".repeat(101);

        mockMvc.perform(get("/api/v1/greetings")
                        .param("name", name)
                        .with(jwt()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void rejectsUnauthenticatedRequest() throws Exception {
        // Verify that requests without a JWT token are rejected with 401 Unauthorized
        mockMvc.perform(get("/api/v1/greetings").param("name", "Jayesh"))
                .andExpect(status().isUnauthorized());
    }
}