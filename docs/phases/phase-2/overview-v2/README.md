# Phase 2 carousel v2 — Service-to-Service Communication

**New pack** inspired by [`../../reference/cache/`](../../reference/cache/). Does **not** replace [`../overview/`](../overview/) (Slices A+B RestClient poster set). Phase index: [`../`](../).

Style: pastel sticky-note cheat sheets · **1:1** · LinkedIn + Instagram.

Post **in filename order**. Overlay your handle in the editor (art uses `SB-MS · Phase 02` only).

Source of truth: [`services/`](../../../../services/) · ADRs [`0001`](../../../adr/0001-restclient-timeouts-and-downstream-mapping.md), [`0002`](../../../adr/0002-openfeign-inventory-only.md).

| # | File | Page |
|---|------|------|
| 01 | `01-cover.png` | Hook — same 3 services, deliberate HTTP clients |
| 02 | `02-overview.png` | Cheat sheet — RestClient / Feign / WebClient / Idempotency |
| 03 | `03-architecture.png` | Order flow — product RestClient + inventory Feign |
| 04 | `04-restclient.png` | RestClient — timeouts, factory, named beans |
| 05 | `05-openfeign.png` | OpenFeign — declarative inventory only |
| 06 | `06-webclient.png` | WebClient — one GET, blocking vs reactive |
| 07 | `07-compare.png` | Client comparison matrix |
| 08 | `08-errors.png` | Domain vs transport → 404 / 409 / 503 / 500 |
| 09 | `09-idempotency.png` | Retry-safe order keys (`Idempotency-Key` / Slice E) |
| 10 | `10-cta.png` | Like / share / save + what’s next |

On-slide labels use **Retry-Safe Key** / **IDEM-KEY** because image models routinely misspell “Idempotency”. Caption and this README use the real header name `Idempotency-Key`.

Skill + tokens for future topics: [`.claude/skills/generate-sysdesign-carousel/`](../../../../.claude/skills/generate-sysdesign-carousel/).

---

## Caption seed

> Phase 2 of 25 — Service-to-service communication done deliberately.
>
> RestClient with 500ms connect / 2s read · OpenFeign on inventory · WebClient for one GET · Idempotency-Key so retries don’t double-reserve.
>
> Same three services. Fail-fast 503 instead of a hang.
>
> Java 21 · Spring Boot 3 · Microservices
>
> #SpringBoot #Microservices #Java #SystemDesign #OpenFeign #WebClient

---

## Out of this pack

Eureka (P3) · Load balancer (P4) · Gateway (P5) · Resilience4j (P7) · Saga/Kafka (P9–10).
