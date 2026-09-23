# Phase 02 — Inter-service communication (architecture note)

**Branch:** `feature/phase-2-service-communication`  
**Code tree:** [`services/`](../../services/) — one Maven parent for all phases (no `phase-02-*` folder)

**Poster:** [`docs/phases/posters/phase-2.png`](../phases/posters/phase-2.png) · **Carousel (A+B):** [`phase-2/overview`](../phases/phase-2/overview/) · **Carousel v2:** [`phase-2/overview-v2`](../phases/phase-2/overview-v2/) · **Interview packs:** [`clients`](../phases/phase-2/clients/) · [`openfeign`](../phases/phase-2/openfeign/) · catalog [`docs/phases/README.md`](../phases/README.md)  
**ADRs:** [`0001`](../adr/0001-restclient-timeouts-and-downstream-mapping.md) · [`0002`](../adr/0002-openfeign-inventory-only.md) (inventory Feign; product moved to WebClient in Slice D)

---

## Slice map

| Slice | Intent | In repo now |
|-------|--------|-------------|
| A | Fail-fast RestClient (connect + read timeout) | Yes |
| B | Typed downstream errors → 503 / 502 ProblemDetail | Yes |
| C | OpenFeign on inventory | Yes |
| D | WebClient on product GET | Yes |
| E | Idempotency-Key on `POST /orders` | Yes |
| + pooling | Named max connections on the **live** clients | Yes — Feign HC5 + WebClient `ConnectionProvider` |

Discovery, gateway, and Resilience4j are **later phases**, not slices of this one.

---

## Runtime (after C–E — this is what `POST /orders` uses)

Work only under [`services/order-service/`](../../services/order-service/).

```
Client
  |  POST /api/v1/orders  + Idempotency-Key
  v
order-service :8083
     /                         \
WebClient (product GET)         OpenFeign (reserve / release)
ConnectionProvider              Request.Options + (slice +) HC5 pool
localhost:8081                  localhost:8082
```

| Neighbor | Live client | Config (under `services/order-service/…`) | YAML |
|----------|-------------|-------------------------------------------|------|
| Product | `ProductWebClient` | `…/config/WebClientConfig.java` | `clients.product-service` |
| Inventory | `InventoryFeignApi` | `…/config/InventoryFeignConfig.java` | `clients.inventory-service` |
| RestClient beans | Unused on create-order | `…/config/RestClientConfig.java` | Slice A leftover |
| Phase 1 RestClient | Historical | tag `phase-01-complete` | replay via Git, not a second folder |

Timeouts stay **500ms connect / 2s read** on those YAML keys. Pooling is a second knob (how many in-flight), not a replacement.

---

## Slice + pooling — steps (live path)

Implement on `feature/phase-2-service-communication` under `services/`. Full lesson in chat (`/write-slice-lesson`); no docs dump unless you say `file`.

1. **YAML** — `services/order-service/src/main/resources/application.yml`: add `max-connections` (product) and `max-connections` + `max-connections-per-route` (inventory). Bind on `ProductClientProperties` / `InventoryClientProperties`.
2. **Product** — `services/order-service/…/config/WebClientConfig.java`: stop bare `HttpClient.create()`. Use `ConnectionProvider.builder("product-service").maxConnections(...)`. Keep Slice D timeouts + `.block()` in `ProductWebClient`.
3. **Inventory** — Feign default `Client` is `HttpURLConnection` (no pool). Add `httpclient5` (+ `feign-hc5` if needed) on **order-service** POM only. In `InventoryFeignConfig` (still not a scanned `@Configuration`), add pooled `feign.Client` (`ApacheHttp5Client`). Keep `Request.Options`. `disableAutomaticRetries()` — retries are Phase 7.
4. **Skip** RestClient pooling as a requirement. `RestClientConfig` may stay as A/C comparison leftover.
5. **Happy path** — from `services/`: `mvn spring-boot:run -pl order-service`. `POST http://localhost:8083/api/v1/orders` + `Idempotency-Key` → 201. Tomcat on 8083.
6. **Break it** — stop product, then inventory; still **503** inside the timeout budget. Pooling must not hang the caller.

Do not share one connection manager across product and inventory.

---

## Boundary

- Do not put Eureka names in `base-url` yet (Phase 3; this branch stays on `localhost`).
- Do not treat compensation as a saga (Phase 9).
- Do not add Kafka because retries feel scary (Phase 10); Slice E is the HTTP retry story.
- Do not add Resilience4j bulkhead (Phase 7). A pool cap is not a bulkhead.
- Do not invent a second phase folder — evolve `services/` on this branch.
