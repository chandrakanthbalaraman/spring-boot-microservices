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
