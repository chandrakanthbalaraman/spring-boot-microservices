# Spring Boot Microservices (SB-MS)

Hands-on learning repo: **Microservices 101 → distributed systems → production architecture → Kubernetes → capstone**.

You already know Java and Spring Boot fundamentals. This repo is the engineering vehicle — one Git history, one **`services/`** tree, phases on branches/tags, same services evolved instead of thrown away.

**You implement. AI plans, scaffolds, and reviews** via [`AGENTS.md`](./AGENTS.md), [`cursor.md`](./cursor.md), and [`.cursor/`](./.cursor/).

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
| **Maven 3.9+** | Each phase has its own parent POM (`mvn` on the PATH). Wrappers (`./mvnw`) are not in these folders yet |

---

## Current status

Do **not** treat this README as the live progress board.

| Surface | Role |
|---------|------|
| [`AGENTS.md`](./AGENTS.md) / [`CLAUDE.md`](./CLAUDE.md) | One-line **Current phase** |
| [`sb-roadmap.md`](./sb-roadmap.md) | Phase-by-phase checklists |
| [`.cursor/memory/phase-progress.md`](./.cursor/memory/phase-progress.md) | Dated DONE / PARTIAL log |
| [Trello board](https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices) | Optional mirror — update only when asked |

Refresh with `/sync-phase-status` after a phase slice.

---

## Repository layout (target)

One **`services/`** Maven parent (product, inventory, order). Phases are **feature branches** + **tags** — not duplicate folders. See [`sb-roadmap.md`](./sb-roadmap.md) § Git Strategy.

```text
spring-boot-microservices/          # this repo (folder: SB-MS)
├── README.md
├── AGENTS.md                       # canonical AI brief (CLAUDE.md symlinks here)
├── cursor.md                       # Cursor-specific overlay
├── sb-roadmap.md
├── docs/                           architecture / decisions / notes
├── shared/                         common-model, common-exception, common-util (later)
├── infrastructure/docker/          reusable Compose (Postgres first)
├── scripts/dev-services.sh         start the stack in Cursor terminal tabs
├── services/                       product + inventory + order (evolves every phase)
└── capstone/                       consolidated production architecture (late)
```

Build from `services/` (or a service module):

```bash
# Refresh deps after POM changes (-U forces Maven to re-check remote metadata)
cd services
mvn clean install -U

cd product-service
mvn spring-boot:run
```

Same pattern for later phases, for example `services/order-service`. `clean install -U` is the command to re-run after adding a client library (RestClient factory, OpenFeign, WebClient) so the new artifacts actually land on the classpath.

Two stateless inventory instances (Phase 04). Run each command in its own terminal, from `services/`. The `-Dserver.port` value is a system property of the forked app JVM, so it overrides `server.port: 8082` in `application.yml`.

```bash
# INV-A
mvn spring-boot:run -pl inventory-service -Dspring-boot.run.jvmArguments="-Dserver.port=8091"

# INV-B
mvn spring-boot:run -pl inventory-service -Dspring-boot.run.jvmArguments="-Dserver.port=8092"
```

---

## Scripts

[`scripts/dev-services.sh`](./scripts/dev-services.sh) starts the local stack in **Cursor’s integrated terminal**, one tab per service. Those tabs stay in the editor, so the log is visible in chat. Run the commands from the repo root, from a Cursor terminal.

```bash
./scripts/dev-services.sh stop    # free ports held by an earlier run
./scripts/dev-services.sh all     # check ports, print the start steps
```

Then start the tabs from the editor with `Ctrl+Shift+Cmd+S`, or `Cmd+Shift+P` → **Tasks: Run Task** → **SB-MS: start all**. A shell cannot create Cursor terminal tabs, so this last step is manual. The task lives in [`.vscode/tasks.json`](./.vscode/tasks.json). The next tab opens after the previous process is ready: Postgres healthy, then Eureka, then Config Server, then the apps. `all` adds the shortcut to your Cursor keybindings on first use.

| Shortcut | Does |
|----------|------|
| `Ctrl+Shift+Cmd+S` | Run **SB-MS: start all** (this workspace only) |
| `Cmd+Shift+P` → **Tasks: Run Task** | Pick any SB-MS task, for example **SB-MS: inventory-service :8092** or **SB-MS: stop all** |
| `Cmd+Shift+P` → **Tasks: Terminate Task** | Stop one running task tab |
| `Cmd+Shift+P` → **Tasks: Restart Running Task** | Restart one service tab |
| ``Ctrl+` `` | Show or hide the terminal panel with the service tabs |

| Tab | Command | Port |
|-----|---------|------|
| Postgres (three databases + pgAdmin) | `./scripts/dev-services.sh postgres` | `5433`, `5434`, `5435`, pgAdmin `5050` |
| discovery-server | `./scripts/dev-services.sh discovery-server` | `8761` |
| config-server | `./scripts/dev-services.sh config-server` | `8888` |
| product-service | `./scripts/dev-services.sh product-service` | `8182` when `.env` sets `SPRING_PROFILES_ACTIVE=dev` (`8081` with no profile) |
| inventory-service | `./scripts/dev-services.sh inventory-service 8091` | `8091` |
| inventory-service | `./scripts/dev-services.sh inventory-service 8092` | `8092` |
| order-service | `./scripts/dev-services.sh order-service` | `8083` |
| api-gateway | `./scripts/dev-services.sh api-gateway` | `8080` |

Run one command in the current terminal to restart that service only. `./scripts/dev-services.sh stop` sends SIGTERM to the app ports above, then `docker compose stop`. Postgres volumes stay. The matching task is **SB-MS: stop all**.

`all` exits if any of those app ports is already listening. Stop the previous run first.

`product-service` is the only command that loads the repo-root [`.env`](./.env) (`PRODUCT_DB_URL`, `PRODUCT_DB_USERNAME`, `PRODUCT_DB_PASSWORD`, `SPRING_PROFILES_ACTIVE`). That matches **Product Service — dev** in [`.vscode/launch.json`](./.vscode/launch.json). The other services keep the values in their own `application.yml`. Config Server stays on the `native` profile and reads `infrastructure/config-repo`.

Inventory `8091` and `8092` are the Phase 04 load-balancer pair. The script passes `-Dserver.port`, which overrides `server.port: 8082` in `inventory-service`. Eureka’s `instance-id` includes that port, so both instances register.

A debug launch and a script tab both bind the same port. Run each service in one place.

```bash
./scripts/dev-services.sh help
```

### Debug launches

[`.vscode/launch.json`](./.vscode/launch.json) starts one JVM under the debugger and prints its log in an integrated terminal:

| Launch name | Port |
|-------------|------|
| Discovery Server | `8761` |
| Config Server | `8888` |
| API Gateway | `8080` |
| Product Service — dev | from `.env` / Config Server (`8182` on the `dev` profile) |
| Order Service | `8083` |
| Inventory Service — 8091 | `8091` |
| Inventory Service — 8092 | `8092` |

---

Graceful shutdown sends a deregistration signal. A crash cannot, so Eureka must infer failure from missing heartbeats.
First identify the exact :8092 process:

```bash
lsof -nP -iTCP:8092 -sTCP:LISTEN
```
Confirm that the listed Java process is the inventory instance. Then replace 12345 with that exact PID:

```bash
kill -9 12345
```

```bash
kill -9 $(lsof -t -i :PORT)
```
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

Source of truth is [`.cursor/`](./.cursor/). Claude Code and Codex consume the **same files** through symlinks under [`.claude/`](./.claude/) and [`.agents/`](./.agents/) — edit `.cursor/`, not the symlink copies.

| Path | What |
|------|------|
| [`.cursor/commands/`](./.cursor/commands/) | Slash workflows (`/new-feature`, `/sync-phase-status`, …) |
| [`.cursor/skills/`](./.cursor/skills/) | Repeatable procedures (Codex: also via `.agents/skills`) |
| [`.cursor/rules/`](./.cursor/rules/) | Always-on constraints |
| [`.cursor/agents/`](./.cursor/agents/) | Specialized personas |
| [`.cursor/memory/`](./.cursor/memory/) | Durable decisions and progress |

Cursor overlay: [`cursor.md`](./cursor.md). Cross-tool brief: [`AGENTS.md`](./AGENTS.md).

**Typical session**

| You say | What runs |
|---------|-----------|
| “Start Phase 1” | Architecture + repo scaffold from `sb-roadmap.md` |
| `/create-phase-branch` | `feature/phase-{N}-{slug}` |
| `/sync-phase-status` | Honest Current phase + checklist |
| “Review this PR” | `/review-pr` against `.cursor/rules/` |

---

## Related

- Roadmap: [`sb-roadmap.md`](./sb-roadmap.md)
- Trello card map: [`docs/trello.md`](./docs/trello.md)
- Decisions index: [`.cursor/memory/decisions.md`](./.cursor/memory/decisions.md)
- Naming: [`.cursor/memory/naming-conventions.md`](./.cursor/memory/naming-conventions.md)
