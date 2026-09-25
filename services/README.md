# SB-MS — Runnable services (`services/`)

Single code tree for the whole roadmap. **Phases are Git branches and tags**, not separate folders — replay Phase 1 with `git checkout phase-01-complete` when tagged.

**Current curriculum focus:** Phase 05 Slices A–B are DONE — order, product, and inventory Gateway routes plus outage/recovery are verified. Next: Slice C correlation IDs and basic observability. Phase 04 is tagged `phase-04-complete`.

Parent checklist: [`sb-roadmap.md`](../sb-roadmap.md) · Phase 4 notes: [`docs/phases/phase-4/`](../docs/phases/phase-4/)

---

## Objective

Phase 4 proved client-side load balancing across inventory instances. Phase 5 adds a single external front door:

- Run Spring Cloud Gateway on `:8080`
- Route `/api/v1/orders/**` via `lb://order-service` (Slice A)
- Keep Eureka + Feign for service-to-service calls
- Expand to product/inventory routes and cross-cutting filters in later slices

Eureka remains the teaching discovery tool; Kubernetes Ingress/Service DNS appear in later phases.

---

## Architecture

```
                   Client
                      │
                      ▼
               api-gateway :8080
                 Path=/api/v1/orders/**
                      │ lb://order-service
                      ▼
                 order-service :8083
                    │        │
             Feign/LB        Feign/LB
                    ▼        ▼
              product     inventory
```

| Preserved from Phase 4 | New in Phase 5 Slice A |
|------------------------|------------------------|
| Feign + RoundRobin to inventory | Gateway as client entry |
| Dual inventory instances | One `lb://order-service` route |
| Eureka registry | Gateway registers / discovers via Eureka |

Out of scope here: product/inventory gateway routes, CORS, rate limits, auth (later slices / phases).

---

## Services

Unchanged ports and DBs for business services. Run Compose from `infrastructure/docker/postgres` (same as Phase 1).

| Service | Port | DB |
|---------|------|-----|
| `discovery-server` | 8761 | none |
| `api-gateway` | 8080 | none |
| `product-service` | 8081 | `product_db` :5433 |
| `inventory-service` | 8082 | `inventory_db` :5434 |
| `order-service` | 8083 | `order_db` :5435 |

---

## Prerequisites

JDK 21 · Maven 3.9+ · Docker Compose (three Postgres) · Phase 04 load-balancing behavior.

---

## Project Structure

```text
services/
├── pom.xml                 # phase Maven parent (artifact: services)
├── README.md
├── discovery-server/       # Eureka registry; no database
├── api-gateway/            # Spring Cloud Gateway (WebFlux); no database
├── product-service/
├── inventory-service/
└── order-service/          # name-only Feign clients resolve through Eureka
```

Evolve in place on feature branches; merge to `main` and tag when a phase is complete.

---

## How to Run

Start databases once, then use **four terminals**. Start Eureka first, then the three business services.

This folder’s `pom.xml` is a Maven **parent** (`packaging: pom`). It has no `@SpringBootApplication`. Running `mvn spring-boot:run` here fails with *Unable to find a suitable main class*. Pick a child module.

```bash
# 1. Postgres (from repo root)
cd infrastructure/docker/postgres && docker compose up -d

# 2. Registry first, from `services/`:
mvn spring-boot:run -pl discovery-server

# 3–5. One business service per terminal, from `services/`:
mvn spring-boot:run -pl product-service
mvn spring-boot:run -pl inventory-service
mvn spring-boot:run -pl order-service

# Phase 04 — two stateless inventory instances (one terminal each).
# Replaces the single inventory-service command above.
# INV-A
mvn spring-boot:run -pl inventory-service -Dspring-boot.run.jvmArguments="-Dserver.port=8091"

# INV-B
mvn spring-boot:run -pl inventory-service -Dspring-boot.run.jvmArguments="-Dserver.port=8092"
```

Open http://localhost:8761 and wait until all three application names appear before testing the order flow.

The same module commands work if you prefer to `cd` into a child service first:

```bash
cd product-service && mvn spring-boot:run
```

---

## API Endpoints

The `/api/v1/...` contracts and required `Idempotency-Key` from Phase 2 are unchanged.

---

## Request Flow

The business contract is unchanged; only endpoint resolution changes:

```
POST /api/v1/orders
  → Feign asks LoadBalancer for product-service
  → LoadBalancer uses the Eureka registry to select an instance
  → Feign asks LoadBalancer for inventory-service
  → order is created with the existing idempotency behavior
```

---

## Database

Discovery adds no database changes. The three business services keep their existing database-per-service Flyway schemas.

---

## Testing

`mvn clean test` proves all six modules compile; there are no substantive automated tests yet. Runtime evidence remains part of each learning slice.

Phase 04 Slice A runtime evidence (2026-09-23):

- Eureka reported `inventory-service:8091` and `inventory-service:8092` as `UP`.
- Both ports returned HTTP `200` with the same database-backed inventory record.
- A third process on `:8091` failed with `Port 8091 was already in use`; the healthy process subsequently re-registered.
- The complete Maven reactor remained green.

Phase 04 Slice B runtime evidence (2026-09-24):

- Direct requests to both inventory instances returned the matching `X-Instance-Port` value.
- Four fresh orders through the updated order-service returned HTTP `201` and alternated `8092 → 8091 → 8092 → 8091`.
- Review result: **PASS WITH NOTES**. The diagnostic header is suitable for this lesson but should not become an accidental public production contract; focused propagation tests remain follow-up work.
- The complete four-module Maven reactor remained green.

Phase 04 Slice C runtime evidence (learner verified, 2026-09-24):

- Stopping/crashing `:8092` produced a temporary typed `503` while its stale registration remained selectable.
- After Eureka and LoadBalancer converged, every successful order returned `201` with `X-Instance-Port: 8091`.
- Restarting `:8092` restored distribution across both inventory ports.
- Fresh `mvn clean test` completed successfully across all four modules; substantive automated tests remain open.

Phase 04 Slice D runtime evidence (learner verified, 2026-09-24):

- Direct reads through `:8091` and `:8092` returned the same inventory row.
- Mutations through either instance were immediately visible through the other, including the incremented optimistic-lock version.
- Load-balanced orders changed one shared stock value, and that state survived stopping and restarting an inventory instance.
- No sticky routing, HTTP session, or JVM-local stock storage was introduced.

Phase 05 Slice A runtime evidence (reviewed, 2026-09-25):

- Eureka reported `API-GATEWAY` as `UP` on `:8080`.
- `GET :8080/api/v1/orders/1` returned HTTP `200` with the same payload as direct `:8083`.
- The unconfigured `GET :8080/api/v1/products/1` returned Gateway HTTP `404`.
- With order-service stopped and Eureka converged, the learner observed Gateway HTTP `503` for the matched order route.
- After order-service restarted and re-registered, `GET :8080/api/v1/orders/1` recovered to HTTP `200`; the recovered route was independently rechecked.

Phase 05 Slice B evidence (reviewed, 2026-09-25):

- A fresh six-module `mvn clean test` completed with `BUILD SUCCESS`; the Gateway module still has no substantive automated tests.
- Gateway `GET /api/v1/products/1` returned `200` with a body identical to direct product-service.
- Gateway `GET /api/v1/inventories/1` returned `200` through instance `:8092`, with a body identical to the direct `:8091` response.
- The existing Gateway order route still returned `200`.
- Immediately after product-service stopped, its stale registered instance produced a transient Gateway `500`; after Eureka convergence, the matched product route returned the expected no-instance `503`.
- Inventory remained available through Gateway, and after product-service restarted plus Gateway refreshed, all three Gateway routes returned `200` (final recheck 2026-09-25 16:13 UTC).
- Non-blocking cleanup remains: stale Slice A/C comments plus module README/POM/Javadoc wording.

---

## Failure Scenarios

1. Call an unconfigured gateway path such as `/api/v1/products/1`; confirm the predicate miss returns `404`.
2. Stop order-service while Gateway remains running; observe the stale-registration interval.
3. After Eureka convergence, confirm `lb://order-service` returns Gateway `503` because no instance is available.
4. Restart order-service and verify the same Gateway route recovers after registration.

---

## Current lesson

- Gateway is the client-facing reverse proxy; business logic remains in downstream services.
- A route combines an id, destination URI, predicates, and optional filters.
- `Path=/api/v1/orders/**` selects the route; `lb://order-service` resolves its destination through Eureka and ReactorLoadBalancer.
- Predicate miss (`404`) and matched route with no service instance (`503`) are different failures.

---

## Exercises (do in order — do not dump the phase)

- [x] **Slice A scaffold:** WebFlux Gateway module on `:8080`, Eureka client, one `lb://order-service` route.
- [x] **Slice A happy path:** Gateway order response matches direct order-service response.
- [x] **Slice A predicate boundary:** unconfigured product path returns Gateway `404`.
- [x] **Slice A break-it:** stop order-service, observe eventual Gateway `503`, restart, and prove recovery.
- [x] **Slice B:** product/inventory routes, direct-vs-Gateway happy paths, product-outage `503`, route isolation, and recovery verified.
- [ ] **Slice C:** correlation IDs and basic gateway logs/metrics.
- [ ] **Slice D:** CORS and rate-limit awareness.

---

## Production Improvements

Load balancing (4) · gateway (5) · Resilience4j retry/CB (7) · saga (9) · Kafka (10).

---

## Next Phase

**Phase 05 — API Gateway.** Slices A–B are DONE. Next: Slice C correlation IDs and basic observability; carry the stale Gateway module wording as cleanup.
