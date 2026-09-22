# Phase 02 — Inter-Service Communication

Same three services as Phase 1. The lesson is **how they talk**: timeouts, pooling, Feign, error mapping, idempotency — not new business APIs.

Parent checklist: [`sb-roadmap.md`](../sb-roadmap.md) · Phase 1 snapshot: [`../phase-01-microservices-basics/`](../phase-01-microservices-basics/)

---

## Objective

Phase 1 proved failure **propagates**. Phase 2 makes the HTTP client **deliberate**:

- Fail fast (connect + read timeout) instead of hanging
- Map “neighbor down” to **503**, not a generic 500
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
    RestClient  /            \  OpenFeign (Slice C)
     (+ timeout)              \ (+ timeout + ErrorDecoder)
               ▼               ▼
     product-service     inventory-service
          :8081               :8082
```

| Still true from Phase 1 | New in Phase 2 |
|-------------------------|----------------|
| 3 processes, 3 Postgres | Client timeouts + pool |
| `POST /api/v1/orders` orchestration | **503** for dependency down (timeout / refused) |
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

Unchanged happy path. What changes is **failure timing and status** once Slices A + B are wired:

```
inventory DOWN
  Phase 1: connection refused (fast) OR hang (no timeout) → often HTTP 500
  Phase 2 A+B: connect/read timeout → 503 ProblemDetail (named service, no stack)
```

---

## Database

Same Flyway schemas. No Phase 2 migration required for timeouts/Feign. Idempotency may need `V002` on `order_db` (key store) — that is a later slice, not day one.

---

## Testing

Still not the focus. After timeouts exist: one test that a hung host fails inside the read-timeout budget (MockWebServer / WireMock). Authz N/A.

---

## Failure Scenarios

1. Stop `inventory-service` → `POST /api/v1/orders` → **503** `Service Unavailable` (Slice B). Connection refused is usually faster than 500ms.
2. Point `clients.inventory-service.base-url` at a black-hole / filtered port and confirm the request dies in ~500ms connect (or 2s if it hangs after accept), not forever.
3. Wrong product id while inventory is **up** → still **404**, not 503.
4. Duplicate `POST /orders` with the same `Idempotency-Key` (once Slice E exists).

---

## What We Learned (Slices A + B)

- Timeout is a **product decision**, not a JVM default. Connect 500ms vs read 2s catch different failures.
- YAML keys do nothing until `RestClientConfig` wires a request factory.
- Neighbor down is **503**, not a lying 500. A missing SKU stays **404**.
- RestClient / WebClient / Feign are adapters over HTTP; the contract is still REST. Feign is Slice C.
- Idempotency belongs on **mutating** calls the client may retry (Slice E — not done).
- Hard-coded URLs remain until Phase 3.

Study pack: [`docs/phases/phase-2/README.md`](../docs/phases/phase-2/README.md)

---

## Exercises (do in order — do not dump the phase)

- [x] **Slice A — RestClient timeouts:** `timeoutFactory` in `order-service` `RestClientConfig` using `clients.*.connect-timeout` / `read-timeout`. Re-run the inventory-down experiment.
- [x] **Slice B — error mapping:** transport → 503 ProblemDetail; unmapped HTTP → 502; domain 404/400 stay 404/409.
- [x] **Slice C — OpenFeign inventory:** Feign on inventory (`/api/v1/inventories/{id}`, reserve, release). Timeouts + `ErrorDecoder` (404/400 → domain, else 502). Inventory down → 503.
- [x] **Slice D — WebClient:** product GET via `ProductWebClient` + `.block()`; Feign inventory untouched. Break-it verified by learner.
- [ ] **Slice E — idempotency:** `Idempotency-Key` header on `POST /orders`.
- [ ] Connection pooling on the request factory (explicit max connections).
- [x] Break it after A–D. Write what you saw. Repeat after E.

### Slice C — proven

`InventoryFeignApi` + `InventoryErrorDecoder` + `@Primary` adapter. Inventory via Feign; product moved to WebClient in Slice D. `url` is still `http://localhost:8082` until Phase 3.

### Slice D — proven

`WebClientConfig` (`productWebClient`) + `ProductWebClient` (`webProductClient`). Product GET uses WebClient; inventory stays Feign. App remains Tomcat (MVC + `.block()`).

Learner carousel (slices first): [`docs/phases/phase-2/`](../docs/phases/phase-2/) · poster: [`docs/phases/phase-2.png`](../docs/phases/phase-2.png)

---

## Production Improvements

Discovery (Phase 3) · load balancing (4) · gateway (5) · Resilience4j retry/CB (7) · saga (9) · Kafka (10).

---

## Next Phase

**Phase 03 — Service Discovery.** Replace `http://localhost:8082` with the logical name `inventory-service`.
