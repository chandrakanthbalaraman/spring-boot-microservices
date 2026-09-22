# ADR 0002 — OpenFeign for inventory only

- **Status:** Accepted (Phase 2, Slice C)
- **Date:** 2026-09-16
- **Phase:** `services/`

## Context

Slices A+B made RestClient deliberate: named beans, YAML timeouts, typed 502/503. Phase 2 also asks us to **compare** a declarative client on the same inventory calls without changing the HTTP contract or leaving `localhost`.

Putting Feign on both neighbors would hide the comparison. Putting Feign on product and RestClient on inventory would work, but inventory is the mutating, failure-exercise neighbor (reserve/release, stop-the-process).

## Decision

1. Add Spring Cloud **2025.0.3** (Northfields, Boot 3.5) BOM on the phase parent; add `spring-cloud-starter-openfeign` **only** to `order-service`.
2. Inventory calls move to `@FeignClient(name = "inventory-service", url = "${clients.inventory-service.base-url}")`. Product stays RestClient.
3. Reuse `clients.inventory-service` Durations via `Request.Options` — do not duplicate millisecond keys under `spring.cloud.openfeign.client.config`.
4. HTTP errors: `ErrorDecoder`. Transport (connect/read/refused): `RetryableException` → **503**. Unmapped HTTP: `DownstreamServiceException` → **502**. Domain 404/400 stay 404/409.
5. No Eureka, no load balancer, no Resilience4j. Drop the `url` attribute in Phase 3.

## Consequences

- Two HTTP client styles in one service — that is the lesson, not an accident to "clean up" this phase.
- Feign `name` is already the logical service id Phase 3 will resolve.
- A component-scanned `@Configuration` on the Feign config class would leak to every future Feign client; keep `InventoryFeignConfig` as a plain class referenced only from `@FeignClient(configuration=...)`.
- Vanilla Feign retries IOExceptions; Spring Cloud OpenFeign defaults to `Retryer.NEVER_RETRY`. Do not add retries here (Phase 7).
- `inventoryRestClient` becomes unused after `OrderServiceImpl` switches; delete it once the Feign path is proven.

## Notes

Same paths as today: `GET /api/v1/inventories/{productId}`, `POST .../reserve-stock`, `POST .../release-stock`.
