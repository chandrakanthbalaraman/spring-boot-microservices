---
name: generate-phase-infographic
description: >-
  Generate a handwritten-style Orbit phase architecture PNG under docs/phases. Pass phase id as $ARGUMENTS (e.g. 0, 0.5, 1). Local image only.
role: Command — phase architecture poster
model: inherit
color: orange
tools: Read, Grep, Glob, Bash, Write
allowed-tools: Read, Grep, Glob, Bash, Write
---

**Role:** Command — phase architecture poster

# /generate-phase-infographic

Build (or redraw) a **status-free** architecture infographic for one Orbit roadmap phase and save it under `docs/phases/`.

**Scope:** `$ARGUMENTS`  
Examples: `0` · `0.5` · `1` · `phase 1` · `redraw 0.5`

## Steps

1. **Parse phase** — extract phase id from `$ARGUMENTS` (default: Current phase number from `AGENTS.md` if only “redraw” / empty is ambiguous — then ask).
2. **Load skill** — follow `/.claude/skills/generate-phase-infographic/SKILL.md` end-to-end.
3. **Apply rule** — `/.claude/rules/phase-infographic.md`.
4. **Content** — read that phase in `orbit-roadmap.md` (+ `AGENTS.md` / `CLAUDE.md` for Phase 0 toolkit).
5. **Generate** — local PNG via image tool + prompt template; reference existing `docs/phases/phase-*.png` for style.
6. **Install** — write/overwrite `docs/phases/phase-{id}.png`.
7. **Output** — absolute/repo path, panel summary, reminder: architecture poster ≠ `/sync-phase-status`.

## Do not

- Create the deliverable in Figma/FigJam unless the user explicitly asks
- Put DONE/PARTIAL/commit/branch chrome on the poster
- Commit or open a PR unless asked
- Confuse this with `/sync-phase-status` (progress docs)
