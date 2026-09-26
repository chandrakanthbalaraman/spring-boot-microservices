# Phase 5 — API Gateway Infographics

One portrait overview for the client entry, routing model, filters, patterns, policy algorithms, and gateway operations. The original landscape asset is preserved for comparison.

| Topic | Image |
|---|---|
| API Gateway — complete Phase 05 overview (4:5 portrait) | [`00-api-gateway-overview.png`](./00-api-gateway-overview.png) |
| Previous overview (16:9 landscape) | [`00-api-gateway-overview-landscape-v1.png`](./00-api-gateway-overview-landscape-v1.png) |

## Implementation anchors

- Module: `services/api-gateway` (WebFlux). Port `:8080`. No database.
- Routes: `/api/v1/orders/**` → `lb://order-service`, `/api/v1/products/**` → `lb://product-service`, `/api/v1/inventories/**` → `lb://inventory-service`
- Downstream ports: product `:8081`, inventory `:8082`, order `:8083`, Eureka `:8761`
- Filter: `CorrelationIdFilter` (`X-Correlation-ID`, highest precedence)
- Observe: `/actuator/health`, metric `spring.cloud.gateway.requests`
- Dependency: `spring-cloud-starter-gateway-server-webflux` (do not add MVC `spring-boot-starter-web`)

## Accuracy notes

- A path with no route is Gateway `404`. A matched route with no registered instance becomes `503` after Eureka converges. A stale registration can briefly surface `500`.
- Service-to-service Feign does not pass through the gateway.
- CORS, rate limiting, authentication, and API versioning are later slices/phases. This picture does not include Redis or OpenTelemetry.

Carousel: [`../carousel/`](../carousel/) · Poster: [`../../posters/phase-5.png`](../../posters/phase-5.png) · Phase index: [`../`](../)
