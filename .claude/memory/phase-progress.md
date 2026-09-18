---
name: phase-progress
description: >-
  Dated log of SB-MS phase progress syncs. Load when checking what was marked DONE/PARTIAL or when running /sync-phase-status.
role: Memory — phase progress log
model: inherit
color: cyan
tools: none
user-invocable: false
---

**Role:** Memory — phase progress log

# Phase progress

Format: `YYYY-MM-DD | Phase | STATUS | note — evidence`

## Entries

- 2026-08-25 | 0 | DONE | GitHub `spring-boot-microservices` + Trello board `Cjb5ESUA` (26 phase cards); Phase 00 card moved to Done; Phase 1 Java still MISSING
- 2026-08-25 | 1 | PARTIAL | Phase Maven parent + 3 modules; product-service layered packages, mapper, GET `/api/products`. Gaps: POST create, Flyway (still `ddl-auto`), inventory/order APIs, Docker Postgres, phase README
- 2026-08-26 | 1 | PARTIAL | product-service GET list, GET `/{id}`, POST create (`@Valid`); Flyway `V001__create_products_table.sql` + `ddl-auto: validate`; Docker Postgres `infrastructure/docker/postgres/` (`product_db` :5433); OpenAPI. Gaps: PUT/DELETE by id; GET/DELETE by SKU stubs; `@RestControllerAdvice`; inventory/order APIs; phase README; tests
- 2026-09-08 | 1 | PARTIAL | product-service also GET `/sku/{sku}`; DELETE controller exists but service throws `UnsupportedOperationException`. inventory-service (uncommitted): POST create, GET by productId, add/reserve/release stock; Flyway `V001__create_inventories_table.sql`; Docker Postgres `inventory_db` :5434. Path is `/api/v1/inventories` not roadmap `/api/inventory`. Gaps: PUT product; DELETE impl; `@RestControllerAdvice`; order-service APIs/DB; service-to-service REST; phase README; tests
- 2026-09-09 | 1 | PARTIAL | product CRUD + GET by SKU (`/api/v1/products`) + `ProductExceptionHandler` (ProblemDetail). inventory APIs + `InventoryExceptionHandler`. Docker Postgres now includes `order_db` :5435. order-service Flyway `V001__create_orders_and_order_items.sql` + `Order`/`OrderItem` entities + `:8083`. Gaps: POST /orders, RestClient to product/inventory, order advice, tests, phase README
- 2026-09-15 | 1 | PARTIAL | order-service POST `/api/v1/orders` + GET `/{id}`; RestClient to product GET + inventory reserve/release; compensate reserved lines on failure; `OrderExceptionHandler`; JOIN FETCH `findWithItemsById` (lazy items). Postman Create/Get order. Gaps: tests, phase README, break-it (inventory DOWN)
- 2026-09-15 | 1 | PARTIAL | Break-it: learner stopped product-service and inventory-service; order-service surfaced RestClientException via ProblemDetail. Gaps: basic integration tests, phase README
- 2026-09-15 | 1 | DONE | Phase README at `phase-01-microservices-basics/README.md`. Basic integration testing **N/A (intentional)** — skipped by learner. Next: Phase 2 (do not create folder until the slice starts).
- 2026-09-16 | 2 | PARTIAL | Folder `phase-02-service-communication/` copied from Phase 1; parent POM retargeted. Timeout keys in order-service `application.yml`; `RestClientConfig` still has no request factory. Gaps: timeouts, 502/503 mapping, Feign, WebClient, idempotency
- 2026-09-16 | 1+2 | Trello | Phase 01 card → Done; Phase 02 card → In Progress. Descs match repo (P1 DONE tests N/A; P2 PARTIAL folder + yml keys).
- 2026-09-16 | 1+2 | Notion | SB-MS Second Brain + Phase 01/02 + Board snapshot + T6 N/A + T7 DONE. Orbit Second Brain not touched.
- 2026-09-16 | 2 | PARTIAL | Slices A+B DONE (timeouts + 503/502). Slice C scaffolded: Cloud BOM 2025.0.3, OpenFeign on order-service, `InventoryFeignClient` empty interface, `Request.Options` from YAML Durations, ErrorDecoder stub, `RetryableException` → 503. Gaps: Feign method mappings, 404/400 decoder, `OrderServiceImpl` switch; D WebClient; E idempotency
- 2026-09-16 | 2 | PARTIAL | Slice A: `RestClientConfig.timeoutFactory` via `ClientHttpRequestFactoryBuilder.detect()` + 500ms/2s YAML. Slice B: `DownstreamServiceUnavailableException` → 503, `DownstreamServiceException` → 500, `onStatus` keeps 404/400 as domain. Docs: `docs/phases/phase-2/`, `docs/phases/phase-2.png`, ADR 0001. Gaps: Feign (C), WebClient (D), idempotency (E), explicit pooling.
- 2026-09-17 | 2 | Trello+Notion | Phase 02 card desc: A+B DONE, C scaffold NOW (lists unchanged). Notion Second Brain / Phase 02 / T3 DONE / T2+T4 created / ADR-0001+0002 on Settled decisions.
- 2026-09-17 | 2 | PARTIAL | Slice C DONE. Feign inventory (`InventoryFeignApi` + decoder 404→404, 400→409, else 502; adapter `RetryableException`→503). Product RestClient. Evidence: inventory down → 503; product 2 no stock row → 404; qty 999 → 409; POST product 5 → 201. Gaps: D WebClient, E idempotency, pooling; 503 detail still leaks Feign `Connection refused executing POST …`
- 2026-09-17 | 2 | PARTIAL | Slice D DONE. Product GET via WebClient (`WebClientConfig` + `ProductWebClient`, `.block()` in MVC); inventory stays Feign. Learner verified. Gaps: E idempotency, pooling. Phase still IN PROGRESS.
- 2026-09-17 | 2 | Trello+Notion | Phase 02 card → Now E. Notion Second Brain + Phase 02 + T2 DONE + T7 WebClient created + Board snapshot.
