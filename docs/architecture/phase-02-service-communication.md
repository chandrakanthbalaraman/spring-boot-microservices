# Phase 02 — Inter-service communication (architecture note)

**Folder:** `phase-02-service-communication/`  
**Poster:** [`docs/phases/phase-2.png`](../phases/phase-2.png) · **Carousel (A+B pack):** [`docs/phases/phase-2/`](../phases/phase-2/) · **Carousel v2 (cache-style, full Phase 2):** [`docs/phases/phase-2-v2/`](../phases/phase-2-v2/)  
**ADR:** [`docs/adr/0001-restclient-timeouts-and-downstream-mapping.md`](../adr/0001-restclient-timeouts-and-downstream-mapping.md)

## Slice map

| Slice | Intent | In repo now |
|-------|--------|-------------|
| A | Fail-fast RestClient (connect + read timeout) | Yes |
| B | Typed downstream errors → 503 / 500 ProblemDetail | Yes |
| C | OpenFeign on one neighbor | No |
| D | WebClient on one GET | No |
| E | Idempotency-Key on `POST /orders` | No |
| + pooling | Explicit pool sizing on the factory | No |

Discovery, gateway, and Resilience4j are **later phases**, not slices of this one.

## Runtime (A + B)

```
Client → order-service :8083
           ├─ productRestClient  (localhost:8081, 500ms / 2s)
           └─ inventoryRestClient (localhost:8082, 500ms / 2s)
```

Order orchestration is unchanged from Phase 1. What changed is **how the call fails**.

## Boundary

- Do not put Eureka names in `base-url` yet.
- Do not treat compensation as a saga.
- Do not add Kafka because retries feel scary — that is Phase 10; Slice E is the HTTP retry story.
