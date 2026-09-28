---
name: review-trello
description: >-
  Review SB-MS work against a Trello card description as acceptance criteria. Use with /review-trello or when the user pastes a Trello URL and asks if scaffolding/feature work matches the card. Applies /.claude/rules and review-pr / review-security skills as needed.
role: Skill — Trello acceptance criteria review
model: opus
color: green
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Skill — Trello acceptance criteria review

# Skill: review-trello

Trigger: user provides a Trello card URL (or Phase 0 card) and asks to review what was built against the card description.

## Method

1. **Fetch the card**
   - Use Trello MCP `trelloReadCard` (`action: get`, `cardIdOrUrl`).
   - Load checklists with `trelloReadChecklist` when present.
   - Default Phase 0 card if none given:
     `https://trello.com/c/goY5ol2b` (Phase 00 — Toolkit, GitHub, Trello)
   - Board map: `/docs/trello.md` · board: https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices

2. **Parse acceptance criteria**
   - Pull Key Tasks, Deliverables, and checklist items into a flat list.
   - Apply SB-MS name mappings (intentional, not failures):
     | Card wording | Location |
     |--------------|----------|
     | `.claude/workflows/` | `.claude/commands/` |
     | `.claude/CLAUDE.md` | Root `CLAUDE.md` (symlink) + `AGENTS.md` |
     | Orbit / `com.orbit` | Ignore — this repo is SB-MS (`AGENTS.md` wins) |

3. **Verify against the filesystem**
   - For each criterion: search/list matching paths; record counts and sample quality.
   - Skills: each `skills/{name}/SKILL.md` must have frontmatter (`name`, `description`, `role`, `model`, `color`, `tools` / `allowed-tools`) and a Method section.
   - Rules: each `rules/*.md` must state enforceable constraints (not empty stubs).
   - Memory: expected files from the card (glossary, decisions, api-contracts, naming, infra, benchmarks, known-issues; future-improvements is allowed extra).
   - Commands: expected multi-step workflows exist under `commands/` (not `workflows/`).

4. **Apply rules & sibling skills**
   - Always: `/.claude/rules/documentation.md`, `clean-code.md`.
   - Phase 0 scaffolding: also sanity-check agent/skill naming vs `/.claude/memory/naming-conventions.md`.
   - Quality of authored prompts: skill `review-pr` + agent `qa-senior-code-reviewer` output format.
   - Security skill only if secrets/auth/SQL appear in the card or diff.

5. **Build the acceptance matrix**
   - Each criterion → `DONE` | `PARTIAL` | `MISSING` | `N/A (intentional)` + evidence path(s).
   - Partial = folder exists but content thin, missing frontmatter, or CLAUDE/AGENTS drift.

6. **Output**
   1. Card title + URL
   2. Acceptance matrix
   3. Rules/skills applied
   4. Ranked findings (Critical → Nit)
   5. Next actions to move the card
   6. Sign-off: `Trello review: PASS / PASS WITH NOTES / BLOCKED`

## Do not
- Mark the Trello card complete or write to Trello without explicit human approval.
- Treat `workflows/` wording on older cards as a failure when `commands/` is present and documented.
