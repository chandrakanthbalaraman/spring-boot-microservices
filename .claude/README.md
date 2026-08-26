# SB-MS AI Engineering Platform

Toolkit for commands, skills, rules, agents, and memory.

**How to use this toolkit (paths from repo root):** see [`README.md`](../README.md), [`AGENTS.md`](../AGENTS.md), and [`cursor.md`](../cursor.md).

Cursor consumes this tree through **symlinks** under `../.cursor/` (`agents`, `skills`, `commands`, `rules`). Edit files **here**, not under `.cursor/`.

| Path | Purpose |
|------|---------|
| [`commands/`](./commands/) | Slash commands (`/{name}`) |
| [`skills/`](./skills/) | Invokable procedures |
| [`rules/`](./rules/) | Always-on constraints |
| [`agents/`](./agents/) | Specialized personas |
| [`memory/`](./memory/) | Durable project knowledge |

> Multi-step procedures live under `commands/` (not a separate `workflows/` folder).
