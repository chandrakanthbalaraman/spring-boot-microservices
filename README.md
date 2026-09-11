# Spring Boot Microservices (SB-MS)

Hands-on learning repo: **Microservices 101 → distributed systems → production architecture → Kubernetes → capstone**.

You already know Java and Spring Boot fundamentals. This repo is the engineering vehicle — one Git history, independently runnable phase folders, same services evolved instead of thrown away.

**You implement. AI plans, scaffolds, and reviews** via [`AGENTS.md`](./AGENTS.md), [`cursor.md`](./cursor.md), and [`.claude/`](./.claude/).

Full checklist: [`sb-roadmap.md`](./sb-roadmap.md).

| | |
|--|--|
| **GitHub** | https://github.com/chandrakanthbalaraman/spring-boot-microservices |
| **Trello** | [SB-MS board](https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices) · [card map](./docs/trello.md) |

---

## Prerequisites

| Tool | Why |
|------|-----|
| **JDK 21** | Language / runtime (`java -version`) |
| **Docker** + Compose | Local Postgres (later Redis, Kafka, observability) |
| **Git** | One repo, tags per phase (`phase-01-complete`, …) |
| **Maven Wrapper** | Per-phase `./mvnw` once Phase 1 exists — no system Maven required |

---

## Current status

Do **not** treat this README as the live progress board.

| Surface | Role |
|---------|------|
| [`AGENTS.md`](./AGENTS.md) / [`CLAUDE.md`](./CLAUDE.md) | One-line **Current phase** |
| [`sb-roadmap.md`](./sb-roadmap.md) | Phase-by-phase checklists |
| [`.claude/memory/phase-progress.md`](./.claude/memory/phase-progress.md) | Dated DONE / PARTIAL log |
| [Trello board](https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices) | Optional mirror — update only when asked |

Refresh with `/sync-phase-status` after a phase slice.

---

## Repository layout (target)

Do **not** create all 25 phase folders up front. Start with Phase 1 only.

```text
spring-boot-microservices/          # this repo (folder: SB-MS)
├── README.md
├── AGENTS.md                       # canonical AI brief (CLAUDE.md symlinks here)
├── cursor.md                       # Cursor-specific overlay
├── sb-roadmap.md
├── docs/                           architecture / decisions / notes
├── shared/                         common-model, common-exception, common-util (later)
├── infrastructure/docker/          reusable Compose (Postgres first)
├── phase-01-microservices-basics/  product + inventory + order
├── phase-02-…                      independently runnable snapshots
└── capstone/                       consolidated production architecture
```

Each phase has its **own Maven parent**. Run folder-by-folder:

```bash
cd phase-01-microservices-basics/product-service
./mvnw spring-boot:run
```

---

## How we learn

```text
CONCEPT → WHY → ARCHITECTURE → MINIMAL CODE → RUN → BREAK → DEBUG → PRODUCTION → CAPSTONE
```

Every phase README should cover: Objective, Architecture, How to Run, APIs, Failure Scenarios, What We Learned, Next Phase.

---

## Local infrastructure

Keep databases off the host Mac. Phase 1 uses **database-per-service**: three Postgres containers plus one **pgAdmin** UI that talks to all three.

```bash
cd infrastructure/docker/postgres
docker compose up -d
```

| Service | Database | User | Password (local only) | Host from pgAdmin | Host from Mac |
|---------|----------|------|------------------------|-------------------|---------------|
| `product-postgres` | `product_db` | `product_app` | `product_app_password` | `product-postgres` | `localhost:5433` |
| `inventory-postgres` | `inventory_db` | `inventory_app` | `inventory_app_password` | `inventory-postgres` | `localhost:5434` |
| `order-postgres` | `order_db` | `order_app` | `order_app_password` | `order-postgres` | `localhost:5435` |

Spring Boot services on the Mac use the **localhost** ports. Inside Docker (including pgAdmin), use the **service names** — they resolve on the Compose network. Do **not** use `localhost` inside pgAdmin; that would mean the pgAdmin container itself.

### Open pgAdmin (all three databases in one client)

1. Start Compose (command above). Wait until `pgadmin` is running.
2. Open [http://localhost:5050](http://localhost:5050).
3. Log in with:
   - **Email:** `admin@example.com`
   - **Password:** `admin`
4. In the left tree, expand **Servers → SB-MS**. The three servers (`product`, `inventory`, `order`) are pre-registered from `infrastructure/docker/postgres/pgadmin/servers.json`.
5. Click a server and enter that database’s password from the table (for example `product_app_password`). Save the password if you want.

Each tree node is a **different Postgres instance**, not a database on the same server. Switch databases by clicking another server in the tree — do not expect `\c` / “change database” to jump from product to inventory.

If the left tree is empty after a first run, the `servers.json` mount only applies on a fresh pgAdmin volume. Recreate it with:

```bash
cd infrastructure/docker/postgres
docker compose up -d --force-recreate pgadmin
```

Passwords above are local Compose defaults — never commit real secrets. Use `.env.example` plus `application-local.yml` (gitignored) when you leave these defaults.

Later: Redis, Kafka, Prometheus, Grafana, Tempo/Jaeger.

---

## AI toolkit

Source of truth is [`.claude/`](./.claude/). Cursor consumes the **same files** through symlinks under [`.cursor/`](./.cursor/) — edit `.claude/`, not the symlink copies.

| Path | What |
|------|------|
| [`.claude/commands/`](./.claude/commands/) | Slash workflows (`/new-feature`, `/sync-phase-status`, …) |
| [`.claude/skills/`](./.claude/skills/) | Repeatable procedures |
| [`.claude/rules/`](./.claude/rules/) | Always-on constraints |
| [`.claude/agents/`](./.claude/agents/) | Specialized personas |
| [`.claude/memory/`](./.claude/memory/) | Durable decisions and progress |

Cursor overlay: [`cursor.md`](./cursor.md). Cross-tool brief: [`AGENTS.md`](./AGENTS.md).

**Typical session**

| You say | What runs |
|---------|-----------|
| “Start Phase 1” | Architecture + repo scaffold from `sb-roadmap.md` |
| `/create-phase-branch` | `feature/phase-{N}-{slug}` |
| `/sync-phase-status` | Honest Current phase + checklist |
| “Review this PR” | `/review-pr` against `.claude/rules/` |

---

## Related

- Roadmap: [`sb-roadmap.md`](./sb-roadmap.md)
- Trello card map: [`docs/trello.md`](./docs/trello.md)
- Decisions index: [`.claude/memory/decisions.md`](./.claude/memory/decisions.md)
- Naming: [`.claude/memory/naming-conventions.md`](./.claude/memory/naming-conventions.md)
