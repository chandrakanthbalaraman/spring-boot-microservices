# SB-MS — Runnable services (`services/`)

Single code tree for the whole roadmap. **Phases are Git branches and tags**, not separate folders — replay Phase 1 with `git checkout phase-01-complete` when tagged.

**Current curriculum focus:** Phase 04 Slices A–D done — dual inventory instances, Feign distribution, unhealthy-instance convergence, and stateless/anti-sticky behavior are learner-verified. Next: Phase 04 review/merge/tag checkpoint before Phase 05 Gateway.

Parent checklist: [`sb-roadmap.md`](../sb-roadmap.md) · Phase 3 notes: [`docs/phases/phase-3/`](../docs/phases/phase-3/)

---

## Objective

Phase 2 made HTTP clients deliberate. Phase 3 removes physical neighbor addresses from the live order path:

- Run a standalone Eureka registry on `:8761`
- Register product, inventory, and order under `spring.application.name`
- Resolve `product-service` and `inventory-service` through name-only Feign clients
- Observe registration, heartbeat, and instance metadata before changing more code
- Preserve Phase 2 timeout, pooling, error mapping, and idempotency behavior

Eureka is a teaching tool here. Kubernetes Service DNS replaces this mechanism in the later Kubernetes phases.

---

## Architecture

```
                       discovery-server :8761
                         registry / lookup
                        /        |        \
                       v         v         v
              product-service inventory-service order-service
                    ^               ^               :8083
                     \             /
                      name-only Feign
```

| Preserved from Phase 2 | New in Phase 3 |
|------------------------|----------------|
| Feign timeouts + HC5 pools | Eureka registry and clients |
| Typed downstream errors | Logical service names instead of Feign URLs |
| `Idempotency-Key` on create order | Registration, heartbeat, and instance metadata |
| Manual compensation | Client-side discovery via Spring Cloud LoadBalancer |

Out of scope here: multiple-instance traffic distribution, Gateway, and Resilience4j (Phases 4 / 5 / 7).

---

## Services

Unchanged ports and DBs. Run Compose from `infrastructure/docker/postgres` (same as Phase 1).

| Service | Port | DB |
|---------|------|-----|
| `discovery-server` | 8761 | none |
| `product-service` | 8081 | `product_db` :5433 |
| `inventory-service` | 8082 | `inventory_db` :5434 |
| `order-service` | 8083 | `order_db` :5435 |

---

## Prerequisites

JDK 21 · Maven 3.9+ · Docker Compose (three Postgres) · Phase 2 timeout/error/idempotency behavior.

---

## Project Structure

```text
services/
├── pom.xml                 # phase Maven parent (artifact: services)
├── README.md
├── discovery-server/       # Eureka registry; no database
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

`mvn test` proves all four modules compile; there are no substantive automated tests yet. The learner has supplied the required Phase 3 runtime evidence from the dashboard, end-to-end order path, metadata inspection, and break-it exercise.

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

---

## Failure Scenarios

1. Start order-service before Eureka and observe registration/lookup behavior; then start Eureka and watch recovery.
2. With all services registered, stop `inventory-service`; repeat `POST /api/v1/orders` immediately, then observe how long the stale registration remains visible.
3. Confirm the caller gets the existing typed **503**, not a raw `No servers available` message or an indefinite hang.
4. Restart inventory-service and observe it re-register before repeating the happy path.

---

## Current lesson

- A registry maps a logical service id to one or more physical instances.
- A Eureka client both registers itself and fetches registry data.
- `@FeignClient(name = "inventory-service")` becomes a discovery lookup only when no fixed `url` overrides it.
- Discovery finds an instance; it does not replace timeout, pooling, error mapping, idempotency, or resilience.

Study pack: [`docs/phases/phase-3/discovery/`](../docs/phases/phase-3/discovery/)

---

## Exercises (do in order — do not dump the phase)

- [x] **Slice A code — Eureka server:** module, server dependency, `@EnableEurekaServer`, standalone client settings.
- [x] **Slice A proof:** start `discovery-server`; inspect the empty dashboard.
- [x] **Slice B proof — registration:** start all three business services; record their application ids and instance metadata.
- [x] **Slice C proof — name-based calls:** create an order and confirm product/inventory Feign logs use discovered instances.
- [x] **Slice D — break discovery:** stop one neighbor, observe eviction/failure, then recover it.
- [x] **Slice E scope decision — N/A (intentional):** defer Eureka vs Kubernetes Services until Phase 18, when the comparison can use a real cluster.

---

## Production Improvements

Load balancing (4) · gateway (5) · Resilience4j retry/CB (7) · saga (9) · Kafka (10).

---

## Next Phase

**Phase 04 — Load Balancing.** Slices A–D are verified. Next: review the Phase 04 branch, merge it to `main`, and tag `phase-04-complete` before Phase 05 Gateway.
