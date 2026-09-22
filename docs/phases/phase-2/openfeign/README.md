# Phase 2 topic — OpenFeign

Standalone interview pack. Pair with the comparison set [`../clients/`](../clients/). Does **not** replace Phase 2 overview carousels.

Style: [`../../reference/api-gateway/`](../../reference/api-gateway/) · 1:1 · footer `SB-MS · Phase 02`.

**Source of truth:** `InventoryFeignApi` + `InventoryFeignConfig` + `InventoryErrorDecoder` under [`services/order-service`](../../../../services/order-service/) · ADR [`0002`](../../../adr/0002-openfeign-inventory-only.md).

Facts to lock:

- `@FeignClient(name = "inventory-service", url = "${clients.inventory-service.base-url}", configuration = InventoryFeignConfig.class)`
- `name` is the future Eureka id; `url` is still `http://localhost:8082`
- Timeouts: `Request.Options` from the **same** YAML `connect-timeout: 500ms` / `read-timeout: 2s` — do not duplicate under `spring.cloud.openfeign.client.config`
- `InventoryFeignConfig` is **not** `@Configuration` (would become global)
- HTTP errors → `ErrorDecoder` (404 / 400 stay domain; else 502)
- Connect/read/refused → `feign.RetryableException` → **503** (never hits the decoder)
- Spring Cloud OpenFeign default `Retryer.NEVER_RETRY` — retries are Phase 7
- Default Feign `Client` is `HttpURLConnection` (**no pool**). Explicit HC5 pooling is the Phase 2 pooling slice, not live until that slice lands.

| # | File | Page |
|---|------|------|
| 01 | `01-cover.png` | Hook — declarative inventory client |
| 02 | `02-scenario.png` | Imperative RestClient vs Feign interface |
| 03 | `03-what.png` | What OpenFeign is + proxy diagram |
| 04 | `04-flow.png` | Call path: proxy → encoder → client → decoder |
| 05 | `05-spring.png` | `@EnableFeignClients` + interface code |
| 06 | `06-timeouts.png` | `Request.Options` from YAML Durations |
| 07 | `07-errors.png` | ErrorDecoder vs RetryableException |
| 08 | `08-pooling.png` | HttpURLConnection vs HC5 pool; no auto-retry |
| 09 | `09-traps.png` | Interview traps |
| 10 | `10-revision.png` | Night-before recap |

---

## Caption seed

> OpenFeign is a typed HTTP interface — not magic, not a circuit breaker.
>
> SB-MS Phase 2: inventory only. `name=inventory-service` (Eureka later) + `url=localhost:8082` (now). Same 500ms/2s budget as RestClient via `Request.Options`. ErrorDecoder for HTTP; RetryableException for transport → 503.
>
> #SpringBoot #OpenFeign #Microservices #Java

## Out of this pack

Eureka wiring (P3) · LoadBalancer (P4) · Resilience4j retry/CB (P7).
