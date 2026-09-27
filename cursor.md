# SB-MS — Cursor overlay

Cursor-specific working agreement. Canonical project memory is [`AGENTS.md`](./AGENTS.md). Full checklist: [`sb-roadmap.md`](./sb-roadmap.md).

GitHub: https://github.com/chandrakanthbalaraman/spring-boot-microservices  
Trello: [board](https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices) · [card map](./docs/trello.md)

Do not duplicate status here. If this file and `AGENTS.md` disagree on **Current phase**, `AGENTS.md` wins.

---

## Symlink map (edit `.cursor/`, never the Claude/Codex copies)

`.cursor/` is the source of truth for the AI toolkit. `.claude/` and `.agents/` expose the same trees via symlinks so Claude Code and Codex stay in sync.

| Path | Role |
|------|------|
| `.cursor/agents` | **real** specialized personas |
| `.cursor/skills` | **real** invokable procedures |
| `.cursor/commands` | **real** slash commands |
| `.cursor/rules` | **real** always-on constraints |
| `.cursor/memory` | **real** durable project knowledge |
| `.cursor/mcp.json` | Project MCP only (merged with `~/.cursor/mcp.json`; gitignored) |
| `.cursor/hooks.json` | Cursor hooks (Jolli session-start / stop; gitignored) |
| `.cursor/settings.local.json` | Claude Code local hooks (gitignored) |

Claude Code aliases (symlinks into `.cursor/`):

| Claude path | Points at |
|-------------|-----------|
| `.claude/agents` | `.cursor/agents` |
| `.claude/skills` | `.cursor/skills` |
| `.claude/commands` | `.cursor/commands` |
| `.claude/rules` | `.cursor/rules` |
| `.claude/memory` | `.cursor/memory` |
| `.claude/settings.local.json` | `.cursor/settings.local.json` |

Codex aliases (symlinks into `.cursor/`):

| Codex path | Points at |
|------------|-----------|
| `.agents/skills` | `.cursor/skills` (Codex **auto-discovers** repo skills here) |
| `.agents/agents` | `.cursor/agents` |
| `.agents/commands` | `.cursor/commands` |
| `.agents/rules` | `.cursor/rules` |
| `.agents/memory` | `.cursor/memory` |

Root aliases:

| Path | Points at |
|------|-----------|
| `CLAUDE.md` | `AGENTS.md` |
| `AGENTS.md` | real file (Cursor / Codex). On macOS this **is** `agents.md` — do not add a second symlink |

If you change an agent, skill, command, rule, or memory file, change it under **`.cursor/`**. Recreating files under `.claude/` or `.agents/` will either fail (symlink) or drift.

**MCP:** keep project servers in `.cursor/mcp.json` only. Cursor merges that file with `~/.cursor/mcp.json` at startup. Do not copy global servers (or their secrets) into the project file.

**Codex:** project brief is root `AGENTS.md`. Skills load from `.agents/skills`. For rules/memory/agents/commands, open the matching path under `.agents/` (or ask Codex to follow `AGENTS.md` / those folders).

To repair:

```bash
cd "$(git rev-parse --show-toplevel 2>/dev/null || pwd)"
# Real trees live under .cursor — do not replace them with symlinks.
rm -f .claude/agents .claude/skills .claude/commands .claude/rules .claude/memory .claude/settings.local.json
ln -s ../.cursor/agents              .claude/agents
ln -s ../.cursor/skills              .claude/skills
ln -s ../.cursor/commands            .claude/commands
ln -s ../.cursor/rules               .claude/rules
ln -s ../.cursor/memory              .claude/memory
ln -s ../.cursor/settings.local.json .claude/settings.local.json

mkdir -p .agents
rm -f .agents/agents .agents/skills .agents/commands .agents/rules .agents/memory
ln -s ../.cursor/agents   .agents/agents
ln -s ../.cursor/skills   .agents/skills
ln -s ../.cursor/commands .agents/commands
ln -s ../.cursor/rules    .agents/rules
ln -s ../.cursor/memory   .agents/memory

# keep .cursor/mcp.json and .cursor/hooks.json
# Do NOT ln agents.md — APFS is case-insensitive and would overwrite AGENTS.md
ln -sf AGENTS.md CLAUDE.md
```

---

## Learning contract (guide, don't dump)

**The learner writes the phase’s business logic. The agent guides and scaffolds.**

| Agent does | Learner does |
|------------|--------------|
| Teach WHY / WHAT / HOW for the current phase slice | IMPLEMENT under `services/` on the phase branch |
| Create phase Maven shells, package dirs, README checklists | Run, break, debug |
| Review **their** diffs against `.cursor/rules/` | Fix failures |
| Wire Compose / Flyway **sketches** when asked | Own service behavior and tests |

**Allowed agent writes by default:** `services/` scaffold, parent/child POMs, `package-info` / empty application class, README checklists, docs/ADR stubs, infrastructure Compose stubs.

**Forbidden by default:** filling in a complete working order flow, Feign clients, saga, or security config as a one-shot dump.

**Override:** learner says `pair` / `write it for me` / `implement this`. Then return to guide mode.

Hard constraints: [`AGENTS.md`](./AGENTS.md) Non-negotiables.

---

## Always-open references

Attach these instead of pasting the whole roadmap:

| File | When to load |
|------|----------------|
| @AGENTS.md | **Every** session (Current phase + constraints) |
| @sb-roadmap.md | **Only** the current `Phase NN` section |
| @.cursor/memory/phase-progress.md | Status honesty |
| @.cursor/memory/decisions.md | Before structural advice |
| @.cursor/memory/naming-conventions.md | Before creating packages/files |
| Matching @.cursor/skills/{name}/SKILL.md | Repeatable procedure |
| Matching @.cursor/agents/{name}.md | Specialized persona |

Slash commands: @.cursor/commands/.

(`.claude/...` paths still resolve via symlinks if an older skill or note references them.)

---

## Shared first actions

Every session should:

1. Read `AGENTS.md` **Current phase**.
2. Confirm the phase with the learner, or use that line.
3. Read **only** that phase in `sb-roadmap.md`.
4. Prefer `/sync-phase-status` over guessing DONE.
5. Start the next phase with `/create-phase-branch` — same `services/` tree, no new folder copy.

Do not load all 25 phases. Do not skip more than one phase without an explicit request.

---

## Mixed scaffolding (Phase 1 example)

When asked to start Phase 1:

- **Generate:** repo files already in this toolkit, `services/` Maven parent, three service modules, layered packages, README checklist, Docker Postgres stub.
- **Leave as TODO:** product/inventory/order business rules, REST call wiring, validation messages, failure-handling behavior — unless the learner asked to pair.

Never write the whole three-service happy path in one answer.

---

## Commands (Cursor)

Same names as Claude Code. Invoke as `/{name}` or in natural language.

| Command | When |
|---------|------|
| `/new-feature` | Plan a capability inside the current phase |
| `/create-phase-branch` | `feature/phase-{N}-{slug}` |
| `/sync-phase-status` | Honest Current phase + roadmap checkboxes |
| `/review-pr` | Review against `.cursor/rules/` |
| `/migrate` | Flyway-only |
| `/arch-review` / `/security-review` / `/performance-review` | Specialized review |
| `/generate-phase-infographic` | Architecture PNG |

Natural language still works: `start phase 1`, `break inventory-service`, `status`, `review`.

---

## Review, not rewrite

Code review identifies issues with file:line references. Do not silently replace the learner’s service code. Exception: they explicitly asked you to implement.
