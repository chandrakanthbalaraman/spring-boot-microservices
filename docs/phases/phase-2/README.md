# Phase 2 carousel — Inter-Service Communication

Dense **notebook** 1:1 slides (same teaching density as [`../phase-1/`](../phase-1/) and [`../reference/exception-handling/`](../reference/exception-handling/)). Landscape architecture poster: [`../phase-2.png`](../phase-2.png).

Post **in filename order**. Same files work on LinkedIn and Instagram (1080×1080-class square). Do not mix the 16:9 poster into the carousel.

**No handle on the art.** Overlay `@yourhandle` in the editor later.

This pack covers **what we have actually built so far** (Slices A + B) **and** the **full slice plan** for the rest of Phase 2. Slide 02 is the map. Do not skip it.

| # | File | Page |
|---|------|------|
| 01 | `01-cover.png` | Hook — fail-fast RestClient, same three services |
| 02 | `02-slices.png` | **All Phase 2 slices planned** (A→E + pooling) — start here |
| 03 | `03-why.png` | Phase 1 hang vs Phase 2 timeout — why this phase exists |
| 04 | `04-architecture.png` | Two named `RestClient` beans, still `localhost` |
| 05 | `05-config.png` | `clients.*` YAML + `@ConfigurationProperties` records |
| 06 | `06-factory.png` | `ClientHttpRequestFactoryBuilder` + connect/read |
| 07 | `07-timeouts.png` | Connect 500ms vs read 2s — product decision, not a JVM default |
| 08 | `08-errors.png` | Domain (`onStatus`) vs transport (`execute`) |
| 09 | `09-http-map.png` | 404 / 409 / 503 / 500 ProblemDetail map |
| 10 | `10-break.png` | Stop inventory → 503 in ~500ms, not a hang |
| 11 | `11-patterns.png` | Typed clients, fail-fast, RFC 7807 — what this is *not* |
| 12 | `12-next.png` | Quick revision + Slices C–E still open |

---

## Slice plan (the whole phase)

Do these **in order**. Do not dump Feign + WebClient + idempotency in one sitting.

| Slice | Lesson | Status in this pack |
|-------|--------|---------------------|
| **A** | RestClient **timeouts** (`connect-timeout` / `read-timeout` on the request factory) | **Covered** — wired in `RestClientConfig` |
| **B** | **Error mapping** — neighbor down → 503; unexpected HTTP → 500; domain 404/400 stay domain | **Covered** — `DownstreamService*` + `OrderExceptionHandler` |
| **C** | **OpenFeign** `InventoryClient` (keep RestClient for product, or the reverse) | **Not yet** — next slice |
| **D** | **WebClient** — one GET; notice blocking vs reactive | **DONE** — `ProductWebClient` + `WebClientConfig` |
| **E** | **Idempotency-Key** on `POST /orders` so a retry does not double-reserve | **Not yet** |
| **+** | Connection **pooling** on the request factory (explicit max connections) | **Not yet** — `detect()` may pool, we did not configure it |
| **Break** | After every slice: stop a neighbor, POST an order, write what you saw | **Covered for A+B** |

**Out of this phase (do not sneak them in):** Eureka / discovery (Phase 3), load balancing (4), Gateway (5), Resilience4j retry/CB/bulkhead (7), saga (9), Kafka (10).

Parent checklist: [`sb-roadmap.md`](../../../sb-roadmap.md) · runnable code: [`phase-02-service-communication/`](../../../phase-02-service-communication/).

---

## What Slices A + B actually changed

Phase 1 already had three services, three Postgres, `POST /api/v1/orders`, and RestClient **with no timeout**. Neighbor down became a generic **500**, or a **hang** if the host black-holed the SYN.

Phase 2 so far:

1. **Two named RestClient beans** — `productRestClient` and `inventoryRestClient` — each with its own base URL and timeouts.
2. **Timeouts live in YAML**, bound to records (`ProductClientProperties`, `InventoryClientProperties`).
3. **Request factory** applies `connect-timeout: 500ms` and `read-timeout: 2s`.
4. **Clients wrap transport failures** (`ResourceAccessException` → `DownstreamServiceUnavailableException`; other HTTP → `DownstreamServiceException`).
5. **`onStatus` keeps domain errors** (404 product/inventory, 400 insufficient stock) out of the transport wrapper.
6. **Advice maps** those types to RFC 7807 `ProblemDetail`: 404 / 409 / **503** / 500 — not a leaked stack trace.

Happy-path orchestration did **not** change: get product → reserve stock → confirm → save; compensate reserved lines on failure.

---

## Architecture (Slices A + B)

```
                 Client
                    │
                    ▼
              order-service :8083
              RestClient + timeouts
                 /          \
     500ms / 2s /            \  500ms / 2s
               ▼               ▼
     product-service     inventory-service
          :8081               :8082
```

| Still true from Phase 1 | New in A + B |
|-------------------------|--------------|
| 3 processes, 3 Postgres | Connect + read timeout on each client |
| `POST /api/v1/orders` orchestration | Neighbor unreachable → **503** ProblemDetail |
| Hard-coded `localhost` URLs | Unexpected downstream HTTP → **500** (typed), not a raw RestClient dump |
| Manual compensation | Domain 404/400 still 404/409 — do not disguise them as 503 |
| RestClient exists | Factory is wired; YAML is no longer decorative |

URLs stay `http://localhost:8081` / `8082` until Phase 3. Feign is Slice C, not a replacement for this factory.

---

## Code map (files to re-read)

| Path under `order-service` | Why it exists |
|----------------------------|---------------|
| `src/main/resources/application.yml` | `clients.product-service` / `clients.inventory-service` — `base-url`, `connect-timeout`, `read-timeout` |
| `config/ProductClientProperties.java` | `@ConfigurationProperties(prefix = "clients.product-service")` record |
| `config/InventoryClientProperties.java` | Same for inventory |
| `config/RestClientConfig.java` | Two `@Bean` RestClients; `timeoutFactory()` via `ClientHttpRequestFactoryBuilder.detect()` |
| `client/ProductClient.java` | GET product; `onStatus` 404; `execute()` wraps transport |
| `client/InventoryClient.java` | GET / reserve / release; `onStatus` 404 + 400; same `execute()` |
| `exception/DownstreamServiceException.java` | Unexpected downstream HTTP; carries `serviceName` |
| `exception/DownstreamServiceUnavailableException.java` | Connect/read/refused — subclass of the above |
| `exception/OrderExceptionHandler.java` | 503 / 500 / 404 / 409 ProblemDetail |

Service layer (`OrderServiceImpl`) still talks only to `ProductClient` and `InventoryClient`. It does not know about timeouts or HTTP status codes. That split is the point.

---

## Timeouts (Slice A)

These are **product numbers**, not Spring defaults.

| Key | Value we set | Means |
|-----|----------------|-------|
| `connect-timeout` | `500ms` | Give up if TCP connect does not finish (typical: neighbor **down** / filtered port) |
| `read-timeout` | `2s` | Give up if the neighbor accepted the connection but **did not finish the body** |

Spring Boot 3.4+ API used here:

```java
ClientHttpRequestFactoryBuilder.detect()
    .build(ClientHttpRequestFactorySettings.defaults()
        .withConnectTimeout(connectTimeout)
        .withReadTimeout(readTimeout));
```

`detect()` picks a factory from the classpath (often JDK `HttpClient`). That may imply a pool; **Slice “+ pooling”** is when we set max connections on purpose.

**Black hole vs connection refused**

- Process stopped, port closed → `Connection refused` — usually **faster** than 500ms.
- Packet filtered / wrong host that never RSTs → you wait the **connect-timeout**.
- Neighbor alive but hung after accept → you wait the **read-timeout**.

Phase 1 had no factory, so the last two could wait until the OS or the caller gave up.

---

## Domain vs transport (Slice B)

Two different questions:

1. **Did the neighbor answer with a business no?** (404 product, 400 insufficient stock)
2. **Did the HTTP call fail as a call?** (refused, timeout, 5xx we did not map)

`onStatus` handles (1) **inside** `retrieve()`, by throwing `ProductNotFoundException`, `InventoryNotFoundException`, `InsufficientInventoryException`. Those skip the `execute()` catches because they are not `RestClient*` types.

`execute()` handles (2):

| Caught | Wrapped as | HTTP to the order client |
|--------|------------|---------------------------|
| `ResourceAccessException` | `DownstreamServiceUnavailableException` | **503** Service Unavailable |
| `RestClientResponseException` (unmapped status) | `DownstreamServiceException` | **500** Internal Server Error |

`DownstreamServiceUnavailableException` **extends** `DownstreamServiceException`. Advice has a **more specific** handler for 503, so 503 wins over 500.

A leftover `@ExceptionHandler(RestClientException.class)` still maps to 500. After wrapping, that path should not fire for the inventory/product calls. Treat it as a safety net, not the Slice B design.

**502 vs 503 vs 500** — interview talking point. RFC-ish reading:

- **503** — we could not get a useful answer (down / timeout). *This is what we implemented for transport.*
- **502 Bad Gateway** — we reached them, they returned garbage. *Valid alternative for unmapped 5xx; we used 500 instead.*
- **500** — *our* bug. Using it for “inventory is down” lies to the caller (that was Phase 1).

We did **not** leak `ex.getMessage()` from RestClient into the 503 title as a stack. Detail is `"inventory-service unavailable"` / `"… returned an unexpected error"`.

---

## HTTP map (order-service → caller)

| Situation | Exception | Status |
|-----------|-----------|--------|
| Product id missing downstream | `ProductNotFoundException` | 404 |
| Inventory row missing | `InventoryNotFoundException` | 404 |
| Not enough stock (inventory 400) | `InsufficientInventoryException` | 409 Conflict |
| Validation on `POST /orders` | `MethodArgumentNotValidException` | 400 + `errors` map |
| Neighbor down / timeout | `DownstreamServiceUnavailableException` | **503** |
| Unmapped downstream HTTP | `DownstreamServiceException` | 500 |
| Optimistic lock / constraint | as before | 409 |

Body shape is still RFC 7807 `ProblemDetail` (`spring.mvc.problemdetails.enabled: true`).

---

## How to run (same as Phase 1 ports)

```bash
# Postgres (repo root)
cd infrastructure/docker/postgres && docker compose up -d

# Three terminals, from phase-02-service-communication/
mvn spring-boot:run -pl product-service
mvn spring-boot:run -pl inventory-service
mvn spring-boot:run -pl order-service
```

| Service | Port | DB |
|---------|------|-----|
| product-service | 8081 | `product_db` :5433 |
| inventory-service | 8082 | `inventory_db` :5434 |
| order-service | 8083 | `order_db` :5435 |

Happy path is unchanged: create product → create inventory → `POST /api/v1/orders`.

---

## Break-it (after Slices A + B)

1. Stop **inventory-service** (leave order-service running).
2. `POST /api/v1/orders` with a real `productId`.
3. Expect **503** `ProblemDetail`, title **Service Unavailable**, detail mentioning `inventory-service`. Time-to-fail should be **sub-second** on connection refused, or ~**500ms** on a filtered connect, **not** a hang.
4. Optional: point `clients.inventory-service.base-url` at a black-hole port and confirm you die inside the connect budget.
5. Optional: keep inventory up, but force a hung read (later: a sleep endpoint — we do not have one yet) to see the **2s** read timeout.

Write what you saw (status, title, about how long it took). That note is the lesson.

---

## Interview prompts (A + B)

- Why is timeout a **client** concern, not something you configure on the server you call?
- Connect timeout vs read timeout — which one fires when the process is dead? When it is stuck?
- Why map neighbor-down to **503** instead of 500?
- Why must `onStatus` 404 **not** go through the 503 wrapper?
- Why two RestClient beans instead of one shared client?
- Why still `localhost` if we already have typed clients? (Answer: discovery is Phase 3.)
- RestClient vs RestTemplate vs Feign vs WebClient — what problem does **each** slice add?

---

## Still open (do not mark Phase 2 DONE)

- Slice **C** — OpenFeign `InventoryClient`
- Slice **D** — WebClient comparison
- Slice **E** — `Idempotency-Key`
- Explicit connection pooling
- Tests (MockWebServer / WireMock for the timeout budget) — still not the focus
- Replacing `localhost` — **Phase 3**, not a Phase 2 slice

---

Suggested caption seed:

> Phase 2 of 25 — same three services, but the HTTP client is now deliberate: 500ms connect, 2s read, and a down inventory returns 503 ProblemDetail instead of hanging or lying with a 500.
>
> Slices left: Feign, WebClient, idempotency.
>
> Java 21 · Spring Boot 3 · RestClient
>
> #SpringBoot #Microservices #Java
