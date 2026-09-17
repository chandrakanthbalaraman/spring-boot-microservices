# SB-MS — Project Memory (cross-tool entry point)

> Canonical brief for Cursor, Claude Code, Codex, and other agents.
> [`CLAUDE.md`](./CLAUDE.md) is a **symlink** to this file — edit here only.
> On macOS, `agents.md` and `AGENTS.md` are the same path (case-insensitive disk).
> Cursor-specific overlay: [`cursor.md`](./cursor.md).
> Full checklist: [`sb-roadmap.md`](./sb-roadmap.md).
> GitHub: https://github.com/chandrakanthbalaraman/spring-boot-microservices
> Trello: [board](https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices) · [card map](./docs/trello.md)

---

## What this is

**SB-MS** is a Spring Boot **microservices** learning platform (Java 21 / Spring Boot 3.x / Maven), built by evolving one system through 25 phases into a production-style commerce platform.

AI is an engineering partner: strongest at **planning, scaffolding, and review**; weakest at dumping a whole phase unsupervised. The learner writes the lesson’s business logic; AI does not skip the run → break → debug loop.

**Target outcomes**
- Design, implement, test, containerize, deploy, observe, secure, and troubleshoot a microservices platform
- Engineering docs (ADRs, runbooks, architecture notes)
- Reusable AI toolkit (agents, skills, rules, memory, commands)
- Capstone: gateway + core services + Kafka + Redis + IdP + K8s
- Portfolio suitable for Senior/Staff interviews

This is **not** Orbit (layered monolith). When a copied `.claude` agent or rule still says “Orbit / `com.orbit` / Maven single module / never package-by-feature”, **this file and `sb-roadmap.md` win**.

---

## Current phase

**Phase 2 — folder scaffolded (`phase-02-service-communication/`) · Slice A timeouts not wired · Trello [Phase 01](https://trello.com/c/Ogs7VdO1) Done · [Phase 02](https://trello.com/c/Zkxt4CVl) In Progress**

Update this line after every slice (e.g. `Phase 1: product-service REST + Postgres`).

---

## Non-negotiable constraints

- Java 21, Spring Boot 3.x, Maven (not Gradle)
- **One Git repo**, independently runnable **phase folders** — not 25 repositories
- Each phase has its **own Maven parent**; do not force one giant reactor on day one
- **Do not** create all 25 phase folders up front — start with Phase 1 only
- Evolve the same services; do not throw the app away each phase
- **Database-per-service** — no shared databases between services
- Constructor injection only (`@RequiredArgsConstructor` OK) — no field `@Autowired`
- Externalize config; **never** commit secrets
- Flyway for schema changes once a service has a DB — never Hibernate `ddl-auto`
- Validate mutating APIs (`@Valid`); hide internals behind `@RestControllerAdvice`
- Eureka is a **teaching** discovery tool; Kubernetes Service DNS supersedes it in later phases
- Start **package-by-layer** inside each service; package-by-feature is allowed later when a service grows (see `sb-roadmap.md`)

---

## Where things live

| Path | Role |
|------|------|
| `/sb-roadmap.md` | Phase-by-phase source of truth (checklists) |
| `/phase-NN-*/` | Independently runnable snapshot of the system |
| `/capstone/` | Consolidated production architecture (late) |
| `/shared/` | Cross-service modules (`common-model`, …) when earned |
| `/infrastructure/docker/` | Compose: Postgres first, then Redis/Kafka/obs |
| `/docs/adr` | Architecture Decision Records — numbered; never delete, only supersede |
| `/docs/architecture` | Per-phase architecture notes |
| `/docs/notes` | Topic notes (resilience, kafka, k8s, …) |
| `/.claude/` | AI platform (agents, skills, rules, memory, commands) — **edit here** |
| `/.cursor/` | Cursor entry: `mcp.json` plus **symlinks into `.claude/`** |
| `/AGENTS.md` | This brief |
| `/cursor.md` | Cursor overlay (learning contract + symlink map) |

---

## How to work with me

- **Plan first.** Show architecture (what / why / where) before generating a tree of files.
- **Scaffold structure** (folders, parent POM, package shells, README checklist). Leave the lesson’s business logic for the human unless they say `pair` / `write it` / `implement this`.
- **Do not dump an entire phase** in one shot. Follow: Repository → Parent Maven → one service → DB → APIs → next service → REST → tests → Docker.
- After the human implements, **review** against `/.claude/rules/*.md` — do not silently rewrite their code.
- Check `/.claude/memory/decisions.md` before contradicting a past decision; if it should change, say so explicitly.
- Use slash **commands** under `/.claude/commands/` (`/new-feature`, `/review-pr`, `/sync-phase-status`, …).
- Prefer the matching **agent** for specialized work; prefer a **skill** for a repeatable procedure.
- After a phase slice, run **`/sync-phase-status`** so Current phase + `sb-roadmap.md` stay honest.

### Lesson loop (every phase)

```
CONCEPT → WHY IT EXISTS → ARCHITECTURE → MINIMAL CODE → RUN IT
      → BREAK IT → DEBUG IT → PRODUCTION VERSION → CAPSTONE INTEGRATION
```

### Standing deliverables (every phase)

Working services · how-to-run in the phase README · at least one failure exercise · tests appropriate to the phase · ADR if the structure changed · notes for interview prep.

### ADR template

`docs/adr/NNNN-title.md` — Status / Context / Decision / Consequences.

---

## `.claude/` — AI engineering platform

Claude Code loads this tree. Cursor loads the **same** tree via symlinks (see `cursor.md`). **Slash commands only come from `commands/`**.

### `agents/` — specialized personas

Naming: `{team}-{role}.md`. Sketch; do not unsupervised-implement full features unless the learner asked to pair.

| Team | Agents | Purpose |
|------|--------|---------|
| **Architecture** | `arch-backend-architect`, `arch-domain-architect`, `arch-solution-architect`, `arch-system-design-reviewer` | Service boundaries, sagas, NFRs |
| **Development** | `dev-spring-boot-expert`, `dev-java21-expert`, `dev-api-designer`, `dev-database-architect` | Boot 3.x, Java 21, REST/OpenAPI, schema/Flyway |
| **Platform** | `platform-devops`, `platform-kubernetes`, `platform-terraform`, `platform-aws-architect` | CI/CD, K8s, Terraform, AWS |
| **Quality** | `qa-senior-code-reviewer`, `qa-test-engineer`, `qa-performance-engineer`, `qa-security-engineer`, `qa-observability-engineer` | Review, Testcontainers, benches, OWASP, telemetry |
| **Documentation** | `docs-technical-writer`, `docs-adr-generator`, `docs-runbook-writer`, `docs-release-notes` | Prose, ADRs, runbooks, notes |

### `skills/` — reusable procedures

| Skill | Does |
|-------|------|
| `create-feature` / `create-phase-branch` | Plan a feature; open `feature/phase-{N}-{slug}` |
| `sync-phase-status` | Diff repo vs `sb-roadmap.md`; update Current phase |
| `generate-controller` / `generate-rest-api` / `generate-openapi` | HTTP + OpenAPI sketches |
| `generate-entity` / `generate-flyway` / `schema-design` | Persistence + migrations |
| `generate-test` / `generate-testcontainers` | Unit vs Testcontainers |
| `review-pr` / `review-security` / `review-performance` / `review-api` / `review-trello` | Structured reviews |
| `generate-dockerfile` / `generate-helm` / `generate-terraform` | Container / K8s / IaC sketches |
| `spring-boot-development` | Canonical Boot layering for this repo |

### `rules/` — always-on constraints

One topic per file. If a rule still names Orbit, apply the **SB-MS interpretation** in this file.

| Rule | Enforces |
|------|----------|
| `sb-ms.md` | This repo’s identity (wins over Orbit wording) |
| `java-21.md` | Language/runtime |
| `lombok.md` | Lombok-first beans/DTOs |
| `schema.md` | Postgres/Flyway; no ddl-auto |
| `spring-boot.md` | Constructor injection, externalized secrets |
| `package-by-layer.md` | Default layout **inside each service** (feature packages allowed later) |
| `rest.md` | Resources, OpenAPI, errors, versioning |
| `testing.md` | Unit + Testcontainers discipline |
| `security.md` | Validation, secrets, AuthZ |
| `logging.md` / `kafka.md` / `redis.md` | Telemetry and data-plane discipline |
| `phase-status.md` | Honest Current phase; evidence before DONE |
| `documentation.md` | ADRs, runbooks, notes |

### `memory/` — durable knowledge

| File | Purpose |
|------|---------|
| `decisions.md` | Accepted decisions (check first) |
| `naming-conventions.md` | Packages, Flyway, ADR, Git |
| `phase-progress.md` | Dated DONE/PARTIAL log |
| `domain-glossary.md` | Ubiquitous language |
| `known-issues.md` | Do not re-suggest |
| `api-contracts.md` / `infra-decisions.md` / `benchmarks.md` | Contracts, infra, perf index |

### `commands/` — slash commands

| Command | Purpose |
|---------|---------|
| `/new-feature` | Plan → human impl → review → tests → ADR |
| `/bug-fix` / `/refactor` / `/incident` | Diagnosis, safe change, ops |
| `/security-review` / `/performance-review` / `/arch-review` / `/review-pr` | Structured reviews |
| `/migrate` | Flyway-only schema change |
| `/sync-phase-status` | Current phase + `sb-roadmap.md` from evidence |
| `/generate-phase-infographic` | Phase architecture PNG under `docs/` |
| `/create-phase-branch` | `feature/phase-{N}-{slug}` |

---

## Roadmap phases (summary)

Detail lives in `sb-roadmap.md`. Do not jump phases without updating **Current phase**.

| # | Focus |
|---|--------|
| **01** | Three services, REST, DB-per-service, first order flow |
| **02** | RestClient / WebClient / OpenFeign, timeouts, idempotency |
| **03** | Service discovery (Eureka as teaching tool) |
| **04** | Load balancing, stateless scale-out |
| **05** | Spring Cloud Gateway |
| **06** | Config Server / twelve-factor config |
| **07** | Resilience4j (timeout, retry, CB, bulkhead) |
| **08** | Flyway, locking, pools, query hygiene |
| **09** | Saga, outbox, compensating transactions |
| **10** | Kafka, consumers, DLT, choreography |
| **11** | Redis cache (+ when cache makes things worse) |
| **12** | OAuth2 / JWT / resource server |
| **13** | WebSockets, real-time order tracking |
| **14–16** | Tracing, logging, metrics (OTel / Prometheus / Grafana) |
| **17–19** | Docker Compose → Kubernetes → production K8s |
| **20–22** | CI/CD, testing pyramid, load / capacity |
| **23–24** | Distributed-systems topics + pattern catalog |
| **25** | Capstone commerce platform |

**Learning philosophy:** fail on purpose; discovery in K8s ≠ Eureka; docs/ADRs are deliverables, not polish.

---

## Active known issues

- Orbit-copied agents/skills may still say `com.orbit` / single-module monolith — **ignore those lines**; follow this file
- Hexagonal / ports-and-adapters as the **default** layout — **rejected** (layer first; feature packages later if earned)
- Gradle as app build tool — **rejected** (Maven)
- Creating all 25 phase directories on day one — **rejected**

---

## Sync checklist

- [x] `CLAUDE.md` symlinks to `AGENTS.md` (2026-08-26)
- [x] `.cursor/{agents,skills,commands,rules}` symlink to `.claude/` (2026-08-26)
- [x] Phase status updates go through `/sync-phase-status` once Phase 1 exists (2026-09-16)
- [x] Current phase line matches `sb-roadmap.md` master tracker (2026-09-16)
