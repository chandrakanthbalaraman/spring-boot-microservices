# Phase 3 — Service Discovery Infographics

Project-specific one-page explainers generated from the runnable SB-MS services.

| Topic | Image |
|---|---|
| Service registry | [`01-service-registry.png`](./01-service-registry.png) |
| Service discovery | [`02-service-discovery.png`](./02-service-discovery.png) |
| Discovery intervals | [`03-service-discovery-intervals.png`](./03-service-discovery-intervals.png) |

## Implementation anchors

- Eureka server: `discovery-server` on `localhost:8761`
- Eureka clients: `product-service`, `inventory-service`, and `order-service`
- Name-based calls: `@FeignClient(name = "product-service")` and `@FeignClient(name = "inventory-service")`
- Instance selection: Spring Cloud LoadBalancer using the client's local Eureka registry cache
- Phase 04 scale-out example: `inventory-service:8091` and `inventory-service:8092`

## Interval accuracy note

SB-MS does not currently override the timing properties shown in the interval infographic. The values are Spring Cloud Netflix 4.3.3 defaults used by this build:

- registry fetch: 30 seconds
- lease renewal / heartbeat: 30 seconds
- lease expiration: 90 seconds

Expiration does not promise removal at exactly 90 seconds. Server-side eviction and the next client registry fetch can add delay, so discovery converges rather than changing instantaneously.

Phase pack: [`../discovery/`](../discovery/) · Phase index: [`../`](../)
