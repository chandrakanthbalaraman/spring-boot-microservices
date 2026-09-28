# SB-MS AI Engineering Platform (Claude Code entry)

Toolkit for commands, skills, rules, agents, and memory.

**How to use this toolkit (paths from repo root):** see [`README.md`](../README.md), [`AGENTS.md`](../AGENTS.md), and [`cursor.md`](../cursor.md).

**Source of truth is [`../.cursor/`](../.cursor/).** This folder only holds `README.md` plus **symlinks** into `.cursor/` (`agents`, `skills`, `commands`, `rules`, `memory`, `settings.local.json`) so Claude Code and Cursor share one tree. Edit files under **`.cursor/`**, not here.

| Path | Purpose |
|------|---------|
| [`commands/`](./commands/) | Slash commands (`/{name}`) → `.cursor/commands` |
| [`skills/`](./skills/) | Invokable procedures → `.cursor/skills` |
| [`rules/`](./rules/) | Always-on constraints → `.cursor/rules` |
| [`agents/`](./agents/) | Specialized personas → `.cursor/agents` |
| [`memory/`](./memory/) | Durable project knowledge → `.cursor/memory` |

> Multi-step procedures live under `commands/` (not a separate `workflows/` folder).
