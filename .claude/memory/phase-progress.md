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
- 2026-09-21 | — | STRUCTURE | Branch-based layout: `phase-02-service-communication/` → `services/`; removed duplicate `phase-01-microservices-basics/` (replay Phase 1 via tag `phase-01-complete` when created). Docs + roadmap updated.
- 2026-09-21 | — | Trello+Notion | Phase 01/02/03 cards + SB-MS Second Brain / Settled decisions / Board snapshot / Rebuild / Phase 01–03 pages updated for `services/` + branch layout.
- 2026-09-15 | 1 | DONE | Phase 1 complete (now under `services/` history). Basic integration testing **N/A (intentional)**. Tag `phase-01-complete` recommended on `main`.
- 2026-09-16 | 2 | PARTIAL | Phase 2 work in `services/`; parent artifactId `services`. Slices A–E largely done; pooling open.
- 2026-09-16 | 1+2 | Trello | Phase 01 card → Done; Phase 02 card → In Progress. Descs match repo (P1 DONE tests N/A; P2 PARTIAL folder + yml keys).
- 2026-09-16 | 1+2 | Notion | SB-MS Second Brain + Phase 01/02 + Board snapshot + T6 N/A + T7 DONE. Orbit Second Brain not touched.
- 2026-09-16 | 2 | PARTIAL | Slices A+B DONE (timeouts + 503/502). Slice C scaffolded: Cloud BOM 2025.0.3, OpenFeign on order-service, `InventoryFeignClient` empty interface, `Request.Options` from YAML Durations, ErrorDecoder stub, `RetryableException` → 503. Gaps: Feign method mappings, 404/400 decoder, `OrderServiceImpl` switch; D WebClient; E idempotency
- 2026-09-16 | 2 | PARTIAL | Slice A: `RestClientConfig.timeoutFactory` via `ClientHttpRequestFactoryBuilder.detect()` + 500ms/2s YAML. Slice B: `DownstreamServiceUnavailableException` → 503, `DownstreamServiceException` → 500, `onStatus` keeps 404/400 as domain. Docs: `docs/phases/phase-2/overview/`, `docs/phases/posters/phase-2.png`, ADR 0001. Gaps: Feign (C), WebClient (D), idempotency (E), explicit pooling.
- 2026-09-17 | 2 | Trello+Notion | Phase 02 card desc: A+B DONE, C scaffold NOW (lists unchanged). Notion Second Brain / Phase 02 / T3 DONE / T2+T4 created / ADR-0001+0002 on Settled decisions.
- 2026-09-17 | 2 | PARTIAL | Slice C DONE. Feign inventory (`InventoryFeignApi` + decoder 404→404, 400→409, else 502; adapter `RetryableException`→503). Product RestClient. Evidence: inventory down → 503; product 2 no stock row → 404; qty 999 → 409; POST product 5 → 201. Gaps: D WebClient, E idempotency, pooling; 503 detail still leaks Feign `Connection refused executing POST …`
- 2026-09-17 | 2 | PARTIAL | Slice D DONE. Product GET via WebClient (`WebClientConfig` + `ProductWebClient`, `.block()` in MVC); inventory stays Feign. Learner verified. Gaps: E idempotency, pooling. Phase still IN PROGRESS.
- 2026-09-17 | 2 | Trello+Notion | Phase 02 card → Now E. Notion Second Brain + Phase 02 + T2 DONE + T7 WebClient created + Board snapshot.
- 2026-09-17 | 2 | PARTIAL | Slice E DONE. `Idempotency-Key` required on `POST /api/v1/orders`; Flyway `V002` unique `idempotency_key` + `request_fingerprint`; replay same body (201, same id, no second reserve); mismatch → 409; missing header → 400. Uncommitted in order-service. Gaps: explicit connection pooling. Phase still IN PROGRESS — do not start Phase 3.
- 2026-09-22 | 2 | DONE (slices) | Slice + pooling DONE on `services/order-service/`: Feign inventory HC5 pool (`InventoryFeignConfig` + `feign-hc5`); product WebClient `ConnectionProvider.maxConnections`; YAML max-connections. Happy path 201; inventory down → 503. Phase 02 slice checklist complete. Next: `feature/phase-03-service-discovery` (same `services/` tree).
- 2026-09-23 | 3 | PARTIAL | Committed Eureka server (`discovery-server`), Eureka clients on all three business services, and name-only Feign clients for both order-service neighbors. Reactor `mvn test` succeeds. Runtime registration/dashboard, order happy path, metadata, and break-it evidence remain open.
- 2026-09-23 | 3 | PARTIAL | Learner confirmed Eureka dashboard, all three registrations, name-based Feign order flow, metadata, and break-it runtime exercise. Remaining topic: compare Eureka client-side discovery with Kubernetes Service DNS; Phase 3 is not complete yet.
- 2026-09-23 | 3 | PARTIAL | Phase 03 learning scope complete: Slices A–D committed and learner-verified; four-module `mvn test` reverified green. Slice E is N/A (intentional) in Phase 03 and moved to Phase 18 for a real Kubernetes DNS/Service/EndpointSlice comparison. Gap: review/merge/tag `phase-03-complete`; next learning topic is Phase 04 Slice A.
- 2026-09-23 | 3 | DONE | Reviewed, merged to `main`, tagged `phase-03-complete`. Slices A–D verified; Slice E deferred to Phase 18. No new ADR (decision is in `decisions.md`). Next: Phase 04 Slice A.
- 2026-09-23 | 4 | PARTIAL | Slice A DONE on `feature/phase-04-load-balancing` (uncommitted): explicit port-based Eureka instance IDs; `:8091` and `:8092` both `UP`; both returned the same `inventory_db` record with HTTP 200; duplicate `:8091` startup failed as expected and the healthy instance re-registered; full reactor `mvn test` green. Gaps: Slice B traffic distribution, C unhealthy skip; Slice D formal sticky proof optional.
- 2026-09-24 | 4 | PARTIAL | Slice B DONE and reviewed PASS WITH NOTES: direct headers matched each inventory port; four fresh orders through updated order-service returned 201 and alternated `8092 → 8091 → 8092 → 8091`; four-module `mvn test` green. Follow-ups: focused propagation tests; keep `X-Instance-Port` diagnostic/internal. Next: Slice C unhealthy skip. Slice D sticky proof still optional/PARTIAL.
- 2026-09-24 | 4 | PARTIAL | Slice C DONE (`06b6c55`): inventory leases 5s/15s; order `registry-fetch-interval-seconds: 5` + LoadBalancer cache TTL 5s; kill/stop experiments showed brief 503 then survivor-only 201. Next: Slice D anti-sticky / stateless formalization.
- 2026-09-24 | 4 | PARTIAL | Slice D DONE from learner-verified runtime evidence: cross-instance reads and mutations matched, versioned shared state remained correct under load-balanced orders, and inventory state survived instance stop/restart without sticky routing. All Phase 04 slices are DONE; review/merge/tag `phase-04-complete` remains before Phase 05.
- 2026-09-24 | 4 | Trello+Notion | Phase 04 card desc → A–D DONE (still In Progress for review/merge/tag). Notion Second Brain / Phase 04 / topic / Board snapshot / Spring Boot parent updated. Infographics shipped under `docs/phases/phase-4/infographics/` + poster `docs/phases/posters/phase-4.png`.
- 2026-09-25 | 4 | DONE | Review **PASS WITH NOTES**. Slices A–D evidenced (code + learner runtime + `06b6c55` + green `mvn test`). Weighted + server-side LB marked N/A (intentional). Notes: `X-Instance-Port` diagnostic; demo Eureka/LB clocks; algorithms poster reference-only; PNGs `01`–`04` missing on disk.
- 2026-09-25 | 4 | DONE | Merged `feature/phase-04-load-balancing` → `main` and tagged `phase-04-complete`. Phase 04 docs pack (`00`/`05` infographics + poster) committed with close-out.
- 2026-09-25 | 5 | NEXT | Current phase advanced to Phase 05 Slice A (scaffold Spring Cloud Gateway). Branch `feature/phase-05-api-gateway` not created yet. Trello/Notion not updated this sync.
