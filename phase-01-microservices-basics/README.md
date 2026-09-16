# Phase 01 — Microservices Architecture Fundamentals

Three independently runnable Spring Boot services, **database-per-service**, and a synchronous REST order flow.

Parent checklist: [`sb-roadmap.md`](../sb-roadmap.md) · Phase card: [Phase 01](https://trello.com/c/Ogs7VdO1)

---

## Objective

Understand **why** microservices exist by building the smallest commerce slice that still has real service boundaries:

- Catalog lives in `product-service`
- Stock lives in `inventory-service`
- Checkout orchestration lives in `order-service`

`POST /api/v1/orders` is the first cross-service use case: validate the product, reserve stock, persist the order. Failures on that path are the motivation for later phases (timeouts, discovery, resilience, sagas, Kafka).

---

## Architecture

```
                Client
                   │
                   ▼
             Order Service :8083
              /          \
             /            \
            ▼              ▼
     Product Service   Inventory Service
          :8081              :8082
            │                  │
            ▼                  ▼
      product_db :5433   inventory_db :5434

             Order Service ──► order_db :5435
```

**Why three processes, not one app with three packages**

| Style | What it is | This phase |
|-------|------------|------------|
| Monolith | One deployable, one database, in-process calls | Not this repo |
| Modular monolith | One deployable, modules + package boundaries, often still one DB | Orbit-style; rejected for SB-MS |
| Microservices | Independent deployables, **database-per-service**, network calls | Phase 1 |

`customerId` and `productId` on an order are **opaque references**, not foreign keys. There is no shared schema to join across.

**Sync vs async.** Phase 1 is request/response REST. Kafka / eventual consistency as a *pattern* is Phase 10. Compensation in `OrderServiceImpl` (release reserved lines after a later item fails) is **not** a distributed transaction — it is a best-effort undo. If inventory never answers, nothing was reserved, so there is nothing to release.

---

## Services

| Service | Port | Owns | Calls |
|---------|------|------|--------|
| `product-service` | 8081 | Product catalog (`id`, `name`, `price`, `sku`) | — |
| `inventory-service` | 8082 | Stock (`productId`, `availableQuantity`, `reservedQuantity`) | — |
| `order-service` | 8083 | Order + line items (`customerId`, `status`, `orderAmount`) | Product GET, inventory reserve/release via `RestClient` |

Package layout inside each service is **package-by-layer** (`controller` / `service` / `repository` / `entity` / `dto` / `mapper` / `exception` / `config`).

---

## Prerequisites

| Tool | Why |
|------|-----|
| JDK 21 | Runtime |
| Maven 3.9+ | Each service is a module under this phase parent (`mvn spring-boot:run`) |
| Docker + Compose | Three Postgres instances + pgAdmin |
| curl or Postman | Manual API + break-it |

Local DB passwords are Compose defaults only — never commit real secrets.

---

## Project Structure

```text
phase-01-microservices-basics/
├── pom.xml                 # phase Maven parent (not a repo-root reactor)
├── README.md               # this file
├── product-service/
├── inventory-service/
└── order-service/

infrastructure/docker/postgres/   # Compose: product_db, inventory_db, order_db, pgAdmin
```

Java base package: `com.example.microservices.{product|inventory|order}`.

---

## How to Run

Start databases once, then **three terminals** (order-service needs the other two).

```bash
# 1. Postgres (from repo root)
cd infrastructure/docker/postgres
docker compose up -d

# 2. Product
cd phase-01-microservices-basics/product-service
mvn spring-boot:run

# 3. Inventory
cd phase-01-microservices-basics/inventory-service
mvn spring-boot:run

# 4. Order
cd phase-01-microservices-basics/order-service
mvn spring-boot:run
```

pgAdmin: [http://localhost:5050](http://localhost:5050) — see the root [`README.md`](../README.md) for login and server names.

OpenAPI UI (springdoc):

- Product: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- Order: [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)

Hard-coded client bases in `order-service` `application.yml`:

```yaml
clients:
  product-service:
    base-url: http://localhost:8081
  inventory-service:
    base-url: http://localhost:8082
```

---

## API Endpoints

Public paths are `/api/v1/...` (roadmap sometimes shows `/api/products` — same resources, versioned prefix).

### product-service (`:8081`)

| Method | Path | Notes |
|--------|------|--------|
| `GET` | `/api/v1/products` | List |
| `POST` | `/api/v1/products` | `@Valid` create → `201` |
| `GET` | `/api/v1/products/{id}` | |
| `GET` | `/api/v1/products/sku/{sku}` | |
| `PUT` | `/api/v1/products/{id}` | Name + price |
| `DELETE` | `/api/v1/products/{id}` | `204` |

```bash
curl -s -X POST http://localhost:8081/api/v1/products \
  -H 'Content-Type: application/json' \
  -d '{"name":"Keyboard","price":49.99,"sku":"SKU-KB-001"}'
```

### inventory-service (`:8082`)

| Method | Path | Notes |
|--------|------|--------|
| `POST` | `/api/v1/inventories` | Create row for a `productId` |
| `GET` | `/api/v1/inventories/{productId}` | |
| `POST` | `/api/v1/inventories/{productId}/add-stock?quantity=` | |
| `POST` | `/api/v1/inventories/{productId}/reserve-stock?quantity=` | Used by order-service |
| `POST` | `/api/v1/inventories/{productId}/release-stock?quantity=` | Compensation |

```bash
curl -s -X POST http://localhost:8082/api/v1/inventories \
  -H 'Content-Type: application/json' \
  -d '{"productId":1,"availableQuantity":10}'
```

### order-service (`:8083`)

| Method | Path | Notes |
|--------|------|--------|
| `POST` | `/api/v1/orders` | Orchestrates product + inventory |
| `GET` | `/api/v1/orders/{orderId}` | Loads items (`JOIN FETCH`) |

```bash
curl -s -X POST http://localhost:8083/api/v1/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerId":42,"items":[{"productId":1,"quantity":2}]}'
```

Mutating APIs validate with `@Valid`. Errors are RFC 7807 **ProblemDetail** via `@RestControllerAdvice` (not raw stacks).

---

## Request Flow

Happy path for `POST /api/v1/orders`:

```
1. Create in-memory Order (PENDING)
2. For each line:
   a. ProductClient GET /api/v1/products/{id}     → unit price
   b. InventoryClient POST .../reserve-stock      → stock held
   c. order.addItem(...)
3. order.confirm() → CONFIRMED
4. Persist Order + OrderItems (cascade)
```

If a later line fails, already-reserved lines are **released**, then the original exception is rethrown. The order row is not saved. That is manual compensation, not 2PC / saga (Phase 9).

---

## Database

Flyway owns schema. `ddl-auto: validate` only.

| Service | Host port | Database | Migration |
|---------|-----------|----------|-----------|
| product | 5433 | `product_db` | `V001__create_products_table.sql` (+ price check `V002`) |
| inventory | 5434 | `inventory_db` | `V001__create_inventories_table.sql` |
| order | 5435 | `order_db` | `V001__create_orders_and_order_items.sql` |

`product_id` on inventory/order_items is **not** an FK to `product_db`. Same for `customer_id`.

---

## Testing

**JUnit / Testcontainers: skipped for this phase** (intentional). Coverage returns in later phases (Phase 2 client tests, Phase 21 pyramid).

What *was* exercised:

- Manual create/get via curl / Postman
- Break-it: stop `product-service` or `inventory-service`, then `POST /api/v1/orders` (see below)

---

## Failure Scenarios

```
inventory-service DOWN → order-service RestClient → ResourceAccessException
                      → OrderExceptionHandler → 500 ProblemDetail
```

Same path if **product-service** is down (order calls product first).

What to watch:

1. HTTP status from order-service (currently **500** — “our error”, not 502/503 “dependency down”).
2. Wait time: connection **refused** is fast; a black-holed host with **no connect/read timeout** on `RestClient` hangs. Timeouts are Phase 2 / 7.
3. Compensation: only runs for lines already reserved. If the first remote call never returns, there is nothing to release.

```bash
# With inventory-service stopped:
curl -i -X POST http://localhost:8083/api/v1/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerId":42,"items":[{"productId":1,"quantity":1}]}'
```

---

## What We Learned

- Service boundary = **process + database + API**, not a Java package.
- Sync REST makes the caller as available as its slowest dependency (**failure propagation**).
- DTOs keep HTTP contracts off JPA entities.
- Database-per-service forbids cross-DB joins; IDs are copied, not shared FKs.
- “Distributed transaction” in Phase 1 is a **problem statement**. Compensation is incomplete (inventory down, process crash mid-loop, release also failing).
- Eventual consistency (stock vs order truth after a partial failure) is not solved here — that is saga/outbox/Kafka later.

---

## Exercises

- [x] Stop inventory-service (and product-service); observe order-service ProblemDetail
- [ ] Create two order lines; force the second product to 404; confirm the first reserve was released
- [ ] Hit `POST /api/v1/orders` with an empty `items` array — expect `400` validation ProblemDetail
- [ ] Compare 404 (unknown product) vs 409 (insufficient stock) vs 500 (dependency down)

---

## Production Improvements

Do **not** implement these in this folder. They *are* the next phases.

| Gap | Later |
|-----|--------|
| Hard-coded `localhost` URLs | Phase 3 discovery |
| No connect/read timeout, no retry/CB | Phase 2 + Phase 7 |
| RestClient only | Phase 2 WebClient / OpenFeign |
| Compensation is in-process and best-effort | Phase 9 saga / outbox |
| No idempotency key on `POST /orders` | Phase 2 |
| 500 for a down neighbor | Phase 2 error mapping (502/503) |
| No gateway, no auth | Phase 5 / 12 |

---

## Next Phase

**Phase 02 — Inter-Service Communication** (`phase-02-service-communication/` when that snapshot is created). Same three services: standardize clients, timeouts, connection pooling, Feign, idempotency. Do not start a new product.

Reuse this application. Do not throw it away.
