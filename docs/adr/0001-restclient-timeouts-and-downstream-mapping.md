# ADR 0001 — RestClient timeouts and typed downstream errors

- **Status:** Accepted (Phase 2, Slices A + B)
- **Date:** 2026-09-16
- **Phase:** `services/`

## Context

Phase 1 `order-service` called product and inventory with Spring `RestClient` and **no request-factory timeouts**. A stopped neighbor often became HTTP 500 `RestClientException`. A black-holed host could hang the caller thread. Clients of order-service could not tell “our bug” from “inventory is down.”

Phase 2 still uses **synchronous REST** and **hard-coded localhost URLs**. Feign, WebClient, discovery, and Resilience4j are later slices/phases.

## Decision

1. Give each neighbor its **own** `RestClient` bean (`productRestClient`, `inventoryRestClient`) with `@ConfigurationProperties` for `base-url`, `connect-timeout`, `read-timeout`.
2. Apply timeouts with `ClientHttpRequestFactoryBuilder.detect()` + `ClientHttpRequestFactorySettings` (Spring Boot 3.4+). First values: **500ms connect**, **2s read**.
3. Split **domain** HTTP (`onStatus` 404 / 400 → existing domain exceptions) from **transport** (`ResourceAccessException` → `DownstreamServiceUnavailableException` → **503**; unmapped `RestClientResponseException` → `DownstreamServiceException` → **500**).
4. Keep RFC 7807 `ProblemDetail`. Do not return stack traces or raw RestClient messages as the public contract.

## Consequences

- Order-service fails fast when a neighbor is unreachable; 503 is honest to the caller.
- Domain 404/409 stay 404/409 — a missing SKU is not “service unavailable.”
- Two beans mean two timeout budgets; a slow inventory does not share a client with product by accident.
- `localhost` remains until Phase 3. Timeouts do not replace a circuit breaker (Phase 7).
- Unmapped downstream 5xx currently surface as **500**, not 502. Revisit if we want Bad Gateway for “they answered badly.”
- Explicit connection pooling and idempotency are **not** decided here (Slices +pooling and E).
