# Regeneration notes — phase-2/clients

Style: `docs/phases/reference/api-gateway/{n}.jpg` for slide n. Prefix every prompt with `prompts/00-style.md`. Footer `SB-MS · Phase 02`. No handles.

Content lock:

- Live path: WebClient GET product `:8081` · OpenFeign inventory `:8082` · RestClient beans = artifacts
- YAML: `connect-timeout: 500ms` / `read-timeout: 2s`
- Errors: domain 404/409 · transport 503 · unmapped HTTP 502 · ProblemDetail
- Pooling: open slice; `detect()` is not policy; Feign default = no pool
- Ban: Eureka, Gateway, Resilience4j, Kafka
- Spell on-art: RestClient, OpenFeign, WebClient, Timeout, Connection, Pooling (models garble these)

| File | Layout ref | One-line |
|------|------------|----------|
| 01-cover | api-gateway/1 | Three clients + five concerns |
| 02-scenario | 2 | Hang/500 vs fail-fast 503 |
| 03-what | 3 | What each client is |
| 04-flow | 4 | POST /orders hops |
| 05-restclient | 5 | Factory + YAML code |
| 06-timeouts-pooling | 6 | Connect vs read vs pool |
| 07-errors | 7 | Domain vs transport map |
| 08-compare | 8 | Live-path matrix |
| 09-traps | 9 | 8 interview Q&A |
| 10-revision | 10 | 9 recap cards |

Redraw: only replace a PNG when asked `redraw`. Copy new asset into this folder; do not touch `../overview/` or `../overview-v2/`.
