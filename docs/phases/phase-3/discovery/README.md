# Phase 3 — Service Discovery

Interview-dense **api-gateway notebook** pack for the **planned** phase. There is **no** `phase-03-service-discovery/` runnable folder yet — content is locked to [`sb-roadmap.md`](../../../../sb-roadmap.md) Phase 03 (do not invent APIs or ports).

Style: [`../../reference/api-gateway/`](../../reference/api-gateway/) · 1:1 · footer `SB-MS · Phase 03`.

**Bridge from Phase 2:** Feign already has `name = "inventory-service"` and `url = localhost:8082`. This phase **drops `url`** so the name resolves via a registry.

Eureka is a **teaching** discovery tool. Kubernetes Service DNS supersedes it later — say that on slides 08–10, do not over-invest.

| # | File | Page |
|---|------|------|
| 01 | `01-cover.png` | Hook — call `inventory-service`, not `localhost:8082` |
| 02 | `02-scenario.png` | Hard-coded URLs vs registry |
| 03 | `03-what.png` | Registry, registration, discovery |
| 04 | `04-flow.png` | Heartbeat + lookup + call |
| 05 | `05-client-vs-server.png` | Client-side vs server-side discovery |
| 06 | `06-eureka.png` | Eureka as the teaching registry |
| 07 | `07-feign-name.png` | Drop Feign `url`; keep `name` |
| 08 | `08-k8s.png` | Eureka vs Kubernetes DNS/Services |
| 09 | `09-traps.png` | Interview traps |
| 10 | `10-revision.png` | Night-before recap |

Phase index: [`../`](../) · Catalog: [`../../README.md`](../../README.md). Phase 2 clients: [`../../phase-2/clients/`](../../phase-2/clients/).

---

## Caption seed

> Phase 3 of 25 — stop calling `http://localhost:8082`. Call `inventory-service`.
>
> Registry · registration · heartbeats · client-side discovery. Eureka teaches the idea; Kubernetes Service DNS is what production often uses instead.
>
> #SpringBoot #Eureka #ServiceDiscovery #Microservices #Java

## Out of this pack

Gateway (P5) · Resilience4j (P7) · Kafka (P10). Load balancing *concepts* may appear as “next” — instance pick is Phase 4.
