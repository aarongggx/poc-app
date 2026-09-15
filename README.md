# Greeting Service

Spring Boot REST service implementing:

- `GET /api/v1/greetings?name=Jayesh`
- JSON response with greeting, UTC timestamp and correlation ID
- Kubernetes-style readiness/liveness health probes
- Prometheus metrics
- Bean Validation
- Standardised error payload
- Correlation ID via `X-Correlation-Id`
- No sensitive request/parameter logging

## Requirements

- Java 17+
- Maven 3.9+

## Run

```bash
mvn clean test
mvn spring-boot:run
```

Or package and run:

```bash
mvn clean package
java -jar target/poc-app-1.0.0.jar
```

## API

```bash
curl "http://localhost:8080/api/v1/greetings?name=Jayesh"
```

Example:

```json
{
  "greeting": "Hello, Jayesh!",
  "timestamp": "2026-09-15T00:00:00Z",
  "correlationId": "7d7a7a42-..."
}
```

If a caller supplies a correlation ID:

```bash
curl -H "X-Correlation-Id: demo-123"   "http://localhost:8080/api/v1/greetings?name=Jayesh"
```

The response will contain the same correlation ID.

## Operational endpoints

```text
GET /actuator/health/readiness
GET /actuator/health/liveness
GET /actuator/prometheus
```

Health details are deliberately hidden.

## Validation

`name` must:

- be present
- not be blank
- be no longer than 100 characters

Example:

```bash
curl -i "http://localhost:8080/api/v1/greetings?name="
```

Returns a standardised payload similar to:

```json
{
  "timestamp": "2026-09-15T00:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/greetings",
  "correlationId": "7d7a7a42-...",
  "fieldErrors": [
    {
      "field": "greeting",
      "message": "name must not be blank"
    }
  ]
}
```

## Security / logging

The application does not log request parameters or sensitive headers. Unexpected exceptions are intentionally returned as a generic message rather than exposing internal exception details.

For production, place the actuator endpoints behind the platform's management/security controls as appropriate.
