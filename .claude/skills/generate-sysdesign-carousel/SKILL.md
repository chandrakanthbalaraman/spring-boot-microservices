---
name: generate-sysdesign-carousel
description: >-
  Generate LinkedIn/Instagram system-design carousel slides (1:1) matching the
  pastel sticky-note cheat-sheet style in docs/phases/reference/cache/. Use when
  the user asks for a carousel, LinkedIn/IG post pack, phase-N-v2 slides, or
  "like the cache reference". Writes under docs/phases/{slug}/ without
  overwriting an existing pack unless redraw is requested.
role: Skill — social carousel pack
model: inherit
color: pink
tools: Read, Grep, Glob, Bash, Write
allowed-tools: Read, Grep, Glob, Bash, Write
---

**Role:** Skill — system-design social carousel (cache-reference style)

# Skill: generate-sysdesign-carousel

Trigger: LinkedIn/Instagram carousel, “like cache reference”, phase pack v2, design tokens for cheat-sheet slides.

## Inputs

- **Topic slug** — e.g. `phase-2-v2`, `phase-3-discovery`, `rate-limiter`
- **Source of truth** — phase folder + `sb-roadmap.md` / ADR / architecture note (do not invent APIs)
- **Series label** — default `SYSTEM DESIGN SERIES` (or `SB-MS PHASE NN`)
- **Aspect** — default `1:1` (LinkedIn + Instagram carousel)
- **Handle** — default: **no handle on art** (overlay later). Only print a handle if the user gives one.
- Optional: `redraw` to replace an existing pack; otherwise never overwrite `docs/phases/phase-N/` — use `{topic}-v2` or a new slug.

## Design system

Load and follow [`references/design-tokens.md`](references/design-tokens.md).
Style reference images (pass to image gen as `reference_image_paths`):

- `docs/phases/reference/cache/1.jpg` … `10.jpg` (primary)
- Optionally `docs/phases/reference/exception-handling/` or `rate-limiter/` for density variants

## Canonical 10-slide arc

| # | Role | Layout cue from cache pack |
|---|------|----------------------------|
| 01 | Cover / hero flow | cache/1 — title + central diagram + 4 benefit circles |
| 02 | Cheat-sheet overview | cache/2 — multi-panel what/why/where/types |
| 03 | Core comparison or hit/miss analog | cache/3 — two-column + table |
| 04 | Pattern grid (strategies) | cache/4 or 5 — numbered cards + mini diagrams |
| 05–08 | Deep dives (one concept each) | cache/6–8 — what / flow / pros / cons / when |
| 09 | Toolkit or comparison matrix | cache/9 — tech cards + quick table |
| 10 | CTA | cache/10 — like / share / save (no mascot required) |

Trim or expand only if the user asks; keep **filename order** = post order.

## Method

1. **Plan** — write `docs/phases/{slug}/README.md` with slide table + caption seed **before** generating images (or update after if regenerating).
2. **Content lock** — pull names, ports, timeouts, exception types from the phase code/ADRs. Ban Eureka / Gateway / Resilience4j unless the topic is that phase.
3. **Prompt per slide** — use [`references/prompt-template.md`](references/prompt-template.md). Persist prompts under `docs/phases/{slug}/prompts/NN-*.md` when regenerating later.
4. **Generate** — Cursor `GenerateImage` (or project image backend):
   - `aspect_ratio`: `1:1`
   - `filename`: `NN-short-slug.png`
   - `reference_image_paths`: at least `cache/1.jpg` + one matching layout ref (e.g. cover→1, comparison→3)
5. **Install** — copy/move outputs into `docs/phases/{slug}/`. Never write into an older pack unless `redraw`.
6. **Verify** — open each PNG once; regenerate once if text is garbled or style drifted (purple-glow / dark mode / Inter-dashboard look).
7. **Output** — path list + one-line per slide. Do not commit unless asked.

## Out of scope

- Figma unless requested
- Overwriting `docs/phases/phase-2/` (or any prior pack) without explicit `redraw`
- Dumping competitor handles (`@coderz.py`) onto SB-MS art
- Status badges / Trello updates
