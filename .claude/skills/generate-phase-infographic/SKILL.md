---
name: generate-phase-infographic
description: >-
  Generate a handwritten-style Orbit phase architecture PNG under docs/phases from orbit-roadmap.md. Use when the user asks for a phase poster, phase diagram, or /generate-phase-infographic. Local image only — not Figma unless asked.
role: Skill — phase architecture poster
model: inherit
color: orange
tools: Read, Grep, Glob, Bash, Write
allowed-tools: Read, Grep, Glob, Bash, Write
---

**Role:** Skill — phase architecture poster

# Skill: generate-phase-infographic

Trigger: user wants a Phase N architecture infographic (e.g. “build phase 1 poster”, `/generate-phase-infographic 1`).

## Inputs

- **Phase id** from `$ARGUMENTS` or the request: `0`, `0.5`, `1`, … `14`, or `8.5`
- Optional: `architecture` (default) | `flow-focus` | `redraw` (replace existing)

## Method

1. **Apply rule** — `/.claude/rules/phase-infographic.md` (no status; local PNG; sketch style).
2. **Gather content** — read the matching `# Phase …` section in `orbit-roadmap.md`. Cross-check `AGENTS.md` / `CLAUDE.md` for agents/skills/rules/commands when Phase 0. List real paths that exist under the repo when claiming file trees.
3. **Draft panel outline** (show briefly if ambiguous) — five panels per the rule; phase-specific mapping:
   - **Phase 0:** scaffold · agent model · rules/skills/memory · command orchestration · human+AI operating loop
   - **Phase 0.5:** compose file model · L1 components · L1–L8 layer map · local runtime flow · Phase 8.5 boundary
   - **Phase 1+:** package-by-layer / Flyway / config / auth / ADRs as entities + flows — still **no** status badges
4. **Build image prompt** — follow `references/prompt-template.md` in this skill. Include concrete names (services, ports, packages) from the roadmap. Explicitly ban status chrome.
5. **Generate locally** — use the image generation tool:
   - `filename`: `phase-{id}.png`
   - `aspect_ratio`: `16:9`
   - `reference_image_paths`: existing `docs/phases/posters/phase-1.png` and/or `phase-2.png` when present
6. **Install** — copy/move the generated asset to `docs/phases/posters/phase-{id}.png` (overwrite on redraw).
7. **Verify** — confirm file exists under `docs/phases/posters/`; open/read once if the harness supports image review; fix if status badges leaked in (regenerate once).
8. **Output** — path + one-line panel summary. Do not open a PR or commit unless asked.

## Out of scope

- Figma / FigJam deliverables (unless user explicitly requests)
- Updating Current phase / Trello / Notion status
- Inventing infra or modules not in the roadmap
