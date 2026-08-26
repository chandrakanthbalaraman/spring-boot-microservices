---
name: phase-infographic
description: >-
  Orbit phase poster design: handwritten sketch architecture infographics under docs/phases. Always apply when generating or revising phase diagrams.
role: Rule — phase infographic design
model: inherit
color: orange
tools: none
user-invocable: false
---

**Role:** Rule — phase infographic design

# Rule: Phase Infographic

Apply when creating or revising posters under `docs/phases/`.

## Purpose

Architecture / topology / operating-model posters for a roadmap phase — **not** status dashboards.

## Hard constraints

- **Local PNG only** under `docs/phases/phase-{N}.png` (e.g. `phase-0.png`, `phase-0.5.png`, `phase-1.png`). Do **not** create the deliverable in Figma/FigJam unless the user explicitly asks.
- **No status tracking** on architecture posters: no DONE / PARTIAL / MISSING badges, no green checklists for completion, no commit hashes, no branch names, no “Last synced” ribbons.
- **Source of truth for content:** `orbit-roadmap.md` phase section + `AGENTS.md` / `CLAUDE.md` structure (agents, skills, rules, commands, layers). Do not invent services or folders.
- **Handwritten sketch aesthetic:** cream/paper background, thin ink outlines, soft teal/blue/coral accents, informal hand lettering, optional friendly robot mascots.
- **Avoid:** purple-on-white AI clichés, dark-mode neon, flat corporate slide decks, emoji spam, photorealism.

## Required content shape

Five (±1) numbered panels that mix:

1. **Entity / scaffold map** (folders, packages, compose files)
2. **Capability or persona model** (agents, L1 components, aggregates — phase-specific)
3. **Knowledge or layer model** (rules/skills/memory **or** Compose L1–L8)
4. **Flow** (orchestration, runtime path, request/data path)
5. **Boundary** (what is deferred / out of scope for this phase)

Footer: sticky-note callouts + tiny caption pointing at `orbit-roadmap.md`.

## Generation

Use skill `generate-phase-infographic` and command `/generate-phase-infographic`. Prefer prior `docs/phases/phase-*.png` as visual reference.
