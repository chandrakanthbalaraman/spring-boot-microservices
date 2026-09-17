# Phase 02 — Inter-Service Communication

Same three services as Phase 1. The lesson is **how they talk**: timeouts, pooling, Feign, error mapping, idempotency — not new business APIs.

Parent checklist: [`sb-roadmap.md`](../sb-roadmap.md) · Phase 1 snapshot: [`../phase-01-microservices-basics/`](../phase-01-microservices-basics/)

---

## Objective

Phase 1 proved failure **propagates**. Phase 2 makes the HTTP client **deliberate**:

- Fail fast (connect + read timeout) instead of hanging
- Map “neighbor down” to **502/503**, not a generic 500
- Compare **RestClient**, **WebClient**, and **OpenFeign** on the same inventory call
- Idempotent `POST /orders` so a retry does not double-reserve stock

Do **not** throw this folder away in Phase 3. Discovery replaces `localhost` URLs; the clients stay.

---

## Architecture

```
                 Client
                    │
                    ▼
              order-service :8083
                 /          \
     RestClient /            \  later: Feign
     (+ timeout)              \ (+ timeout)
               ▼               ▼
     product-service     inventory-service
          :8081               :8082
```

| Still true from Phase 1 | New in Phase 2 |
|-------------------------|----------------|
| 3 processes, 3 Postgres | Client timeouts + pool |
| `POST /api/v1/orders` orchestration | 502/503 for dependency down |
| Hard-coded `localhost` URLs | Feign `InventoryClient` (then Phase 3 removes the host) |
| Manual compensation | `Idempotency-Key` on create order |
| RestClient exists, no timeout | WebClient on one path (compare blocking vs reactive) |

Out of scope here: Eureka, Gateway, Resilience4j circuit breaker (Phases 3 / 5 / 7).

---

## Services

Unchanged ports and DBs. Run Compose from `infrastructure/docker/postgres` (same as Phase 1).

| Service | Port | DB |
|---------|------|-----|
| `product-service` | 8081 | `product_db` :5433 |
| `inventory-service` | 8082 | `inventory_db` :5434 |
| `order-service` | 8083 | `order_db` :5435 |

---

## Prerequisites

JDK 21 · Maven 3.9+ · Docker Compose (three Postgres) · Phase 1 mental model (you already ran the break-it).

---

## Project Structure

```text
phase-02-service-communication/
├── pom.xml                 # phase Maven parent (artifact: phase-02-service-communication)
├── README.md
├── product-service/
├── inventory-service/
└── order-service/          # RestClientConfig is the first lesson
```

Copied from Phase 1, then evolved. Do not edit Phase 1 to “finish” Phase 2.

---

## How to Run

Start databases once, then **three terminals** (order-service needs the other two).

This folder’s `pom.xml` is a Maven **parent** (`packaging: pom`). It has no `@SpringBootApplication`. Running `mvn spring-boot:run` here fails with *Unable to find a suitable main class*. Pick a child module.

```bash
# 1. Postgres (from repo root)
cd infrastructure/docker/postgres && docker compose up -d

# 2–4. One service per terminal, from this phase folder:
mvn spring-boot:run -pl product-service
mvn spring-boot:run -pl inventory-service
mvn spring-boot:run -pl order-service
```

Same idea as Phase 1 if you prefer to `cd` into the service first:

```bash
cd product-service && mvn spring-boot:run
```

---

## API Endpoints

Same `/api/v1/...` contracts as Phase 1 until the idempotency slice.

---

## Request Flow

Unchanged happy path. What changes is **failure timing** once timeouts are wired:

```
inventory DOWN
  Phase 1: connection refused (fast) OR hang (no timeout)
  Phase 2: connect/read timeout → mapped ProblemDetail (502/503)
```

---

## Database

Same Flyway schemas. No Phase 2 migration required for timeouts/Feign. Idempotency may need `V002` on `order_db` (key store) — that is a later slice, not day one.

---

## Testing

Still not the focus. After timeouts exist: one test that a hung host fails inside the read-timeout budget (MockWebServer / WireMock). Authz N/A.

---

## Failure Scenarios

1. Stop `inventory-service` → `POST /api/v1/orders` (connection refused).
2. After timeouts: point `clients.inventory-service.base-url` at a black hole (filtered port) and confirm the request dies in ~2s, not forever.
3. Duplicate `POST /orders` with the same `Idempotency-Key` (once that slice exists).

---

## What We Learned (fill as you go)

- Timeout is a **product decision**, not a JVM default.
- RestClient / WebClient / Feign are adapters over HTTP; the contract is still REST.
- Idempotency belongs on **mutating** calls the client may retry.
- Hard-coded URLs remain until Phase 3.

---

## Exercises (do in order — do not dump the phase)

- [ ] **Slice A — RestClient timeouts** (start here): implement `timeoutFactory` in `order-service` `RestClientConfig` using `clients.*.connect-timeout` / `read-timeout` already in `application.yml`. Re-run the inventory-down experiment.
- [ ] **Slice B — error mapping:** `RestClientException` / timeout → 502 or 503 ProblemDetail, not 500.
- [ ] **Slice C — OpenFeign `InventoryClient`:** keep RestClient for product; Feign for inventory (or the reverse). Same paths as today (`/api/v1/inventories/...`).
- [ ] **Slice D — WebClient:** one GET via WebClient; notice blocking vs reactive.
- [ ] **Slice E — idempotency:** `Idempotency-Key` header on `POST /orders`.
- [ ] Connection pooling on the request factory.
- [ ] Break it again. Write what you saw.

---

## Production Improvements

Discovery (Phase 3) · load balancing (4) · gateway (5) · Resilience4j retry/CB (7) · saga (9) · Kafka (10).

---

## Next Phase

**Phase 03 — Service Discovery.** Replace `http://localhost:8082` with the logical name `inventory-service`.
