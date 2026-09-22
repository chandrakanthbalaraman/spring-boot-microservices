# SB-MS — Cursor overlay

Cursor-specific working agreement. Canonical project memory is [`AGENTS.md`](./AGENTS.md). Full checklist: [`sb-roadmap.md`](./sb-roadmap.md).

GitHub: https://github.com/chandrakanthbalaraman/spring-boot-microservices  
Trello: [board](https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices) · [card map](./docs/trello.md)

Do not duplicate status here. If this file and `AGENTS.md` disagree on **Current phase**, `AGENTS.md` wins.

---

## Symlink map (edit `.claude/`, never the Cursor copies)

`.claude/` is the source of truth. `.cursor/` exposes the same toolkit so Cursor skills, commands, and agents stay in sync with Claude Code.

| Cursor path | Points at |
|-------------|-----------|
| `.cursor/agents` | `.claude/agents` |
| `.cursor/skills` | `.claude/skills` |
| `.cursor/commands` | `.claude/commands` |
| `.cursor/rules` | `.claude/rules` |
| `.cursor/mcp.json` | **real file** (Cursor MCP only — not a symlink) |

Root aliases:

| Path | Points at |
|------|-----------|
| `CLAUDE.md` | `AGENTS.md` |
| `AGENTS.md` | real file (Cursor / Codex). On macOS this **is** `agents.md` — do not add a second symlink |

If you change an agent, skill, command, or rule, change it under **`.claude/`**. Recreating files under `.cursor/agents` (etc.) will either fail (symlink) or drift.

To repair:

```bash
cd "$(git rev-parse --show-toplevel 2>/dev/null || pwd)"
rm -rf .cursor/agents .cursor/skills .cursor/commands .cursor/rules
ln -s ../.claude/agents    .cursor/agents
ln -s ../.claude/skills    .cursor/skills
ln -s ../.claude/commands  .cursor/commands
ln -s ../.claude/rules     .cursor/rules
# keep .cursor/mcp.json
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
| Review **their** diffs against `.claude/rules/` | Fix failures |
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
| @.claude/memory/phase-progress.md | Status honesty |
| @.claude/memory/decisions.md | Before structural advice |
| @.claude/memory/naming-conventions.md | Before creating packages/files |
| Matching @.claude/skills/{name}/SKILL.md | Repeatable procedure |
| Matching @.claude/agents/{name}.md | Specialized persona |

Slash commands: @.cursor/commands/ (symlinked from `.claude/commands/`).

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
| `/review-pr` | Review against `.claude/rules/` |
| `/migrate` | Flyway-only |
| `/arch-review` / `/security-review` / `/performance-review` | Specialized review |
| `/generate-phase-infographic` | Architecture PNG |

Natural language still works: `start phase 1`, `break inventory-service`, `status`, `review`.

---

## Review, not rewrite

Code review identifies issues with file:line references. Do not silently replace the learner’s service code. Exception: they explicitly asked you to implement.
