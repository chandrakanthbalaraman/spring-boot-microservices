---
name: review-trello
description: >-
  Review SB-MS repo work against a Trello card description (acceptance criteria), applying /.claude/rules and skills such as review-pr / review-security. Use after scaffolding or feature work tied to a Trello card. Pass a card URL as $ARGUMENTS, or default to Phase 00.
role: Command — Trello card acceptance review
model: opus
color: green
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Command — Trello card acceptance review

# /review-trello

Review developed changes against a **Trello card description** as the source of truth for acceptance criteria. Apply SB-MS **rules** and **skills**; do not rubber-stamp.

**Card:** `$ARGUMENTS`  
If `$ARGUMENTS` is empty, default to:
`https://trello.com/c/goY5ol2b` (Phase 00). Board: https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices

## Steps

1. **Load the card (required)**
   - Fetch the Trello card via MCP (`trelloReadCard` action `get` with `cardIdOrUrl`).
   - Also load checklists if present (`trelloReadChecklist` action `list_by_card`).
   - Treat **name + description + checklist items** as the acceptance criteria.

2. **Normalize criteria**
   - Extract a checklist of deliverables from the card (Key Tasks / Deliverables / bullets).
   - Map obsolete names to SB-MS conventions:
     - `.claude/workflows/` → `.claude/commands/` (slash commands; no `workflows/` folder)
     - `.claude/CLAUDE.md` → repo-root `CLAUDE.md` (symlink to `AGENTS.md`)
   - Note intentional divergences as **NOTES**, not failures, when they match `AGENTS.md` / `sb-roadmap.md` / `/.claude/memory/decisions.md`.

3. **Inventory the repo**
   - Compare criteria to what exists under:
     - `CLAUDE.md`, `AGENTS.md`
     - `.claude/agents/`, `.claude/skills/`, `.claude/rules/`, `.claude/memory/`, `.claude/commands/`
     - `docs/` (including `docs/trello.md`)
     - `.cursor/agents/` if Cursor parity was part of the work
   - Count files; spot-check frontmatter (`name`, `description`, `role`, `model`, `color`, `tools` / `allowed-tools`).

4. **Apply rules (always)**
   - Load and apply relevant `/.claude/rules/*.md` (at minimum: `documentation.md`, `clean-code.md`; others if the card implies them).
   - Check `CLAUDE.md` / `AGENTS.md` sync and non-negotiables when code is in scope.

5. **Apply skills / agents**
   - Skill **`review-trello`**: structured acceptance matrix (done / partial / missing).
   - Skill **`review-pr`** + agent **`qa-senior-code-reviewer`**: quality of what was built (frontmatter completeness, prompt/checklist quality, no empty stubs pretending to be done).
   - Skill **`review-security`** + **`qa-security-engineer`**: only if the card or diff touches auth, secrets, SQL, or deploy credentials (usually N/A for pure scaffolding).

6. **Output format (required)**
   1. **Card summary** — title, URL, list name
   2. **Acceptance matrix** — each criterion → `DONE` / `PARTIAL` / `MISSING` / `N/A (intentional)` with evidence (paths, counts)
   3. **Rules & skills applied** — which rule/skill files were used
   4. **Findings** ranked Critical / High / Medium / Low / Nit (qa-senior-code-reviewer style)
   5. **Suggested next actions** — concrete gaps to close before moving the card
   6. Sign-off: `Trello review: PASS / PASS WITH NOTES / BLOCKED`

7. **Optional Trello update (write — ask first)**
   - Do **not** comment on or move the card unless the human explicitly asks.
   - If asked: draft a short comment summarizing the matrix + sign-off for the human to paste, or confirm before any `trelloWriteCard` call.
