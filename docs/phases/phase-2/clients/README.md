# Phase 2 — RestClient vs OpenFeign vs WebClient

Interview-dense **api-gateway notebook** pack (not a replacement for [`../overview/`](../overview/) or [`../overview-v2/`](../overview-v2/)).

Style: lined notebook · highlighter titles · code boxes · 1:1. Reference: [`../../reference/api-gateway/`](../../reference/api-gateway/).

Post **in filename order**. No handle on art — footer is `SB-MS · Phase 02`.

**Source of truth:** [`services/`](../../../../services/) · ADRs [`0001`](../../../adr/0001-restclient-timeouts-and-downstream-mapping.md), [`0002`](../../../adr/0002-openfeign-inventory-only.md) · architecture [`services.md`](../../../architecture/services.md).

Live `POST /api/v1/orders` path today: **WebClient → product :8081** · **OpenFeign → inventory :8082**. RestClient beans remain as Slice A comparison artifacts. Timeouts **500ms connect / 2s read**. Pooling slice still open (`detect()` is not a pool policy). URLs still `localhost` (Phase 3 drops `url`).

| # | File | Page (api-gateway analog) |
|---|------|---------------------------|
| 01 | `01-cover.png` | Hook — three HTTP clients, five concerns |
| 02 | `02-scenario.png` | Naive RestClient vs deliberate clients |
| 03 | `03-what.png` | What each client is + where it sits |
| 04 | `04-flow.png` | `POST /orders` journey through both neighbors |
| 05 | `05-restclient.png` | RestClient factory, YAML, `onStatus` / `execute()` |
| 06 | `06-timeouts-pooling.png` | Connect vs read vs pool cap |
| 07 | `07-errors.png` | Domain vs transport → 404 / 409 / 503 / 502 |
| 08 | `08-compare.png` | Side-by-side matrix + SB-MS mapping |
| 09 | `09-traps.png` | Interview traps |
| 10 | `10-revision.png` | Night-before recap |

Topic deep-dives (same style): [`../openfeign/`](../openfeign/) · stubs [`../restclient/`](../restclient/) · [`../webclient/`](../webclient/).

Phase index: [`../`](../) · Catalog: [`../../README.md`](../../README.md).

---

## Caption seed

> Phase 2 of 25 — same three services, three HTTP clients on purpose.
>
> RestClient (factory + 500ms/2s) · OpenFeign on inventory (`InventoryFeignApi` + ErrorDecoder) · WebClient on product GET (Reactor Netty + `.block()` in MVC).
>
> Neighbor down → 503 ProblemDetail, not a hang. Domain 404 stays 404. Pooling is a second knob, not a timeout.
>
> Java 21 · Spring Boot 3 · Microservices
>
> #SpringBoot #OpenFeign #WebClient #RestClient #SystemDesign

## Out of this pack

Eureka (P3) · Load balancer (P4) · Gateway (P5) · Resilience4j (P7) · Saga/Kafka (P9–10).
