# api-gateway

Phase 05 — Spring Cloud Gateway. Sole **external** entry for clients; service-to-service Feign stays as-is.

## Slice A (this scaffold)

| | |
|--|--|
| Port | `8080` |
| Route | `Path=/api/v1/orders/**` → `lb://order-service` |
| Discovery | Eureka client (`:8761`) + Spring Cloud LoadBalancer |

```bash
# From services/
mvn spring-boot:run -pl api-gateway
```

Happy path (order-service + Eureka already up):

```bash
curl -i http://localhost:8080/api/v1/orders/1
```

Out of scope here: product/inventory routes, CORS, rate limits, correlation IDs (later slices).
