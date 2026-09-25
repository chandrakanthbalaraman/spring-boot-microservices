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

```bash
# From services/
mvn spring-boot:run -pl api-gateway
```

Happy path (Eureka + target service already up):

```bash
curl -i http://localhost:8080/api/v1/orders/1
curl -i -H 'X-Correlation-ID: demo-trace-1' http://localhost:8080/api/v1/products
```

Out of scope here: CORS, rate limits, authn/authz (later phases). Automated filter tests intentionally deferred.
