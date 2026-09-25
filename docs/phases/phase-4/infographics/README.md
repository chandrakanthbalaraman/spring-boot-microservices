# Phase 4 — Load Balancing Infographics

One comprehensive topic overview plus project-specific explainers for client-side LB, distribution, health skip, and stateless scale.

| Topic | Image |
|---|---|
| Load balancing overview — concepts, algorithms, implementation, and verification | [`00-load-balancing-overview.png`](./00-load-balancing-overview.png) |
| Load-balancing algorithms — workings, pros, cons, and selection guide | [`05-load-balancing-algorithms.png`](./05-load-balancing-algorithms.png) |

Optional slice posters (`01`–`04` client-side / round-robin / health / anti-sticky) are not on disk yet — covered by `00` + runtime evidence in [`../../../services/README.md`](../../../services/README.md).

## Implementation anchors

- Two inventory instances: `inventory-service:8091` and `inventory-service:8092`
- Eureka instance IDs: `${spring.application.name}:${server.port}`
- Caller: `order-service` Feign → Spring Cloud LoadBalancer RoundRobin
- Diagnostic header: `X-Instance-Port`
- Shared durable state: Postgres `inventory_db` (not JVM memory)
- Demo health clocks (Slice C): inventory lease 5s/15s; order registry-fetch 5s; LoadBalancer cache TTL 5s

## Accuracy notes

- Default Eureka/LB clocks are slower (often 30s fetch / 90s expiration). Slice C shortened them so kill → skip is observable in a classroom session.
- Health-aware routing **converges**; a brief 503 while stale candidates remain is expected.
- Sticky sessions are intentionally avoided — they fight horizontal scale when stock is already in the DB.

Poster: [`../../posters/phase-4.png`](../../posters/phase-4.png) · Phase index: [`../`](../)
