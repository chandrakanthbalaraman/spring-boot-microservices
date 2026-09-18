---
name: decisions
description: >-
  Running log of accepted SB-MS architectural decisions. Check before proposing anything that contradicts a past decision.
role: Memory — accepted decisions index
model: inherit
color: purple
tools: none
user-invocable: false
---

**Role:** Memory — accepted decisions index

# Decisions Log

Running log of accepted architectural decisions. Terse and dated.
Full write-ups live in /docs/adr — this file is the quick index agents must check first.

Orbit entries below are **historical copies from the toolkit scaffold**. They do **not** bind this repo. SB-MS entries at the top win.

## Format
`YYYY-MM-DD | ADR-NNNN (optional) | Decision — one line`

## Entries (SB-MS)

- 2026-08-25 | — | One Git repo; independently runnable phase folders; do not create all 25 phases up front.
- 2026-08-25 | — | `.claude/` is source of truth; `.cursor/{agents,skills,commands,rules}` and `CLAUDE.md` are symlinks. Canonical brief: `AGENTS.md` (same path as `agents.md` on macOS).
- 2026-08-25 | — | Microservices from Phase 1 (product, inventory, order) — not a layered monolith first.
- 2026-08-25 | — | Database-per-service. Maven per phase. Package-by-layer per service first; package-by-feature later if earned.
- 2026-08-25 | — | Curriculum: `sb-roadmap.md`. Learning loop: run → break → debug. Do not dump a whole phase unsupervised.
- 2026-08-25 | — | Eureka is a teaching discovery tool; Kubernetes Service discovery supersedes it in later phases.
- 2026-08-25 | — | Java 21 + Spring Boot 3.x + Maven (not Gradle). Constructor injection. Flyway when a service has a DB. No secrets in git.
- 2026-09-16 | ADR-0001 | Named RestClient per neighbor; connect 500ms / read 2s; transport → 503, unmapped HTTP → 500; domain 404/400 stay domain. Feign/WebClient/idempotency not decided.
- 2026-09-16 | ADR-0002 | OpenFeign on inventory only; product stays RestClient at decision time; `url` from `clients.inventory-service.base-url` until Phase 3. Transport → 503 (`RetryableException`); unmapped HTTP → 502.
- 2026-09-17 | — | Slice D: product GET moved to WebClient (`ProductWebClient` + `.block()`); Feign remains on inventory. ADR-0002 inventory decision unchanged.
- 2026-08-25 | — | GitHub repo `chandrakanthbalaraman/spring-boot-microservices`. Trello board https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices in workspace “chandrakanth balaraman's Projects” (MCP cannot create a new Trello org). Card map: `docs/trello.md`. Trello is an optional mirror.

## Entries (Orbit scaffold — do not apply)

- 2026-08-07 | ADR-0005 | *(Orbit)* Persist VOs as `@Embeddable` — N/A until SB-MS writes an equivalent ADR.
- 2026-08-06 | ADR-0002 | *(Orbit)* Maven single module + `com.orbit` — **superseded for SB-MS** by phase-folder Maven + per-service packages.
- 2026-07-30 | — | *(Orbit)* Modular monolith first — **superseded for SB-MS** (microservices from Phase 1).
