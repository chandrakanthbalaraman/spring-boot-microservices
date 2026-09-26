# api-gateway

Phase 05 — Spring Cloud Gateway. Sole **external** entry for clients; service-to-service Feign stays as-is.

| | |
|--|--|
| Port | `8080` |
| Discovery | Eureka client (`:8761`) + Spring Cloud LoadBalancer (`lb://`) |

## Routes (Slices A–B)

| Slice | Predicate | Upstream |
|-------|-----------|----------|
| A | `Path=/api/v1/orders/**` | `lb://order-service` |
| B | `Path=/api/v1/products/**` | `lb://product-service` |
| B | `Path=/api/v1/inventories/**` | `lb://inventory-service` |

## Correlation IDs + basic observability (Slice C)

- Global filter `CorrelationIdFilter` accepts or generates `X-Correlation-ID`, forwards it downstream, and echoes it on the response.
- Structured access log: `gateway_request correlationId=… method=… path=… routeId=… status=… signal=… durationMs=…`
- Actuator: `health`, `info`, `metrics` (Gateway WebFlux metrics enabled in YAML).

## CORS + rate limiting (Slice D)

- Global CORS (`/**`): allowlist origin via `GATEWAY_CORS_ALLOWED_ORIGIN` (default `http://localhost:3000`); `allowCredentials: false`.
- Browser-allowed/exposed headers stay app-facing (`Content-Type`, `Authorization`, `X-Correlation-ID`, `Idempotency-Key`, rate-limit headers). `X-Forwarded-*` is not browser-facing here — proxies set those hop-by-hop.
- Product route uses Gateway `RequestRateLimiter` with a local Caffeine/Bucket4j bucket (3 tokens / 10s per client IP). Single-instance only; Redis-backed limits come later.

```bash
# From services/
mvn spring-boot:run -pl api-gateway
```

Happy path (Eureka + target service already up):

```bash
curl -i http://localhost:8080/api/v1/orders/1
curl -i -H 'X-Correlation-ID: demo-trace-1' http://localhost:8080/api/v1/products
```

CORS preflight / rate-limit smoke:

```bash
curl -i -X OPTIONS http://localhost:8080/api/v1/products \
  -H 'Origin: http://localhost:3000' \
  -H 'Access-Control-Request-Method: GET'

# Burst product GETs — expect 200 then 429 once the local bucket empties
for i in 1 2 3 4 5; do curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/api/v1/products; done
```

Out of scope here: authn/authz and API versioning (later phases). Automated filter tests intentionally deferred.
