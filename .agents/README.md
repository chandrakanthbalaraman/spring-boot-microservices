# SB-MS AI Engineering Platform (Codex entry)

Toolkit for commands, skills, rules, agents, and memory.

**How to use this toolkit (paths from repo root):** see [`README.md`](../README.md), [`AGENTS.md`](../AGENTS.md), and [`cursor.md`](../cursor.md).

**Source of truth is [`../.cursor/`](../.cursor/).** This folder only holds **symlinks** into `.cursor/` so Codex and Cursor share one tree. Edit files under **`.cursor/`**, not here.

| Path | Purpose |
|------|---------|
| [`skills/`](./skills/) | Codex auto-discovers `$REPO/.agents/skills/*/SKILL.md` |
| [`agents/`](./agents/) | Specialized personas (open when needed) |
| [`commands/`](./commands/) | Slash-style workflows (same names as Cursor) |
| [`rules/`](./rules/) | Always-on constraints |
| [`memory/`](./memory/) | Durable project knowledge |

Root [`AGENTS.md`](../AGENTS.md) is already the Codex project brief (loaded every session). Do **not** create a second `agents.md` on macOS — it is the same path as `AGENTS.md`.
