# Phase 1 carousel — LinkedIn / Instagram

Dense **notebook** 1:1 slides (same teaching density as [`../../reference/exception-handling/`](../../reference/exception-handling/)). Landscape architecture poster: [`../../posters/phase-1.png`](../../posters/phase-1.png). Phase index: [`../`](../).

Post **in filename order**. Same files work on both apps (1080×1080-class square). Do not mix the 16:9 poster into the carousel — Instagram will letterbox it.

**No handle on the art.** Overlay `@yourhandle` in the editor later (bottom-center is left open). Do not bake a username into a regenerate.

| # | File | Page |
|---|------|------|
| 01 | `01-cover.png` | Hook — three services, one order flow |
| 02 | `02-why.png` | Monolith vs modular monolith vs microservices |
| 03 | `03-architecture.png` | Bounded contexts + Maven folder |
| 04 | `04-layers.png` | Package-by-layer, constructor injection, Flyway |
| 05 | `05-databases.png` | Database-per-service + Compose ports |
| 06 | `06-apis.png` | REST surface (`/api/v1/...`) + sample JSON |
| 07 | `07-flow.png` | `POST /api/v1/orders` RestClient sequence |
| 08 | `08-patterns.png` | DTOs, sync REST, compensation ≠ 2PC |
| 09 | `09-failure.png` | Stop inventory → 500 ProblemDetail |
| 10 | `10-next.png` | Quick revision + next phases |

**LinkedIn:** document or multi-image post, 1:1. **Instagram:** carousel, 1:1 (safe) — 4:5 would crop these.

Suggested caption seed:

> Phase 1 of 25 — I built three Spring Boot services (product, inventory, order), each with its own Postgres, talking over RestClient. Then I shut inventory down on purpose. That hang is why timeouts, Feign, and Resilience4j exist.
>
> Java 21 · Spring Boot 3 · Maven · Flyway
>
> #SpringBoot #Microservices #Java
