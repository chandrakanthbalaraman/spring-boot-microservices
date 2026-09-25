# Phase 5 — API Gateway Carousel

Ten portrait slides for a social-media carousel. Every slide is `1122 × 1402`, effectively a **4:5** feed ratio, with consistent safe margins and footer numbering.

| Slide | Topic | File |
|---:|---|---|
| 1 | Cover — API Gateway design pattern | [`01-api-gateway-cover.png`](./01-api-gateway-cover.png) |
| 2 | Why the Gateway exists | [`02-why-api-gateway.png`](./02-why-api-gateway.png) |
| 3 | Architecture and responsibility boundaries | [`03-architecture-boundaries.png`](./03-architecture-boundaries.png) |
| 4 | End-to-end request lifecycle | [`04-request-lifecycle.png`](./04-request-lifecycle.png) |
| 5 | Routes, predicates, and `lb://` | [`05-routes-predicates.png`](./05-routes-predicates.png) |
| 6 | Filters and correlation IDs | [`06-filters-correlation-ids.png`](./06-filters-correlation-ids.png) |
| 7 | Gateway implementation patterns and tradeoffs | [`07-gateway-patterns.png`](./07-gateway-patterns.png) |
| 8 | Rate-limiting algorithms and tradeoffs | [`08-rate-limiting-algorithms.png`](./08-rate-limiting-algorithms.png) |
| 9 | Security, failures, and observability | [`09-operations-failures-observability.png`](./09-operations-failures-observability.png) |
| 10 | Quick revision and interview checklist | [`10-quick-revision.png`](./10-quick-revision.png) |

## Accuracy boundaries

- Implemented in current SB-MS: three `lb://` routes, path predicates, `CorrelationIdFilter`, access logs, Actuator health, and `spring.cloud.gateway.requests` metrics.
- Next Phase 05 slice: CORS and rate-limit awareness.
- Conceptual or later work: BFF, gateway aggregation, domain gateways, distributed rate limiting, authentication, authorization, and production resilience.
- Client traffic goes through the Gateway. Eureka supplies discovery data; it does not proxy business traffic.
- Internal Feign calls remain direct service-to-service calls.

Overview: [`../infographics/00-api-gateway-overview.png`](../infographics/00-api-gateway-overview.png) · Phase index: [`../`](../)
