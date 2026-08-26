---
name: sync-phase-status
description: >-
  Sync SB-MS phase progress into AGENTS.md and sb-roadmap.md (and optional Trello) from repo evidence. Use after completing phase tasks or when Current phase is stale. Pass phase or Trello URL as $ARGUMENTS. CLAUDE.md is a symlink to AGENTS.md.
role: Command — phase status sync
model: inherit
color: cyan
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Command — phase status sync

# /sync-phase-status

Compare what is **done in the repo** to `sb-roadmap.md` and the **Current phase** line in `AGENTS.md`, then update those status surfaces. Do not build missing work.

**Scope:** `$ARGUMENTS`  
Examples: empty (use Current phase), `0.5`, `phase 0.5 local compose`, or a Trello card URL.  
Add phrases like `update trello` / `update notion` when the human wants external trackers refreshed (or when they say “update all status”).

## Steps

1. **Load skill** — follow `/.claude/skills/sync-phase-status/SKILL.md` end-to-end.
2. **Apply rule** — `/.claude/rules/phase-status.md` (AGENTS ↔ CLAUDE sync; evidence required; Notion/Trello only when asked).
3. **Criteria** — extract bullets from the active phase section in `sb-roadmap.md` (and Trello checklists if a URL was passed).
4. **Evidence** — Glob/Grep/git for compose, Terraform, CI, modules, docs, etc. Prefer paths + short commit subjects; note uncommitted DONE work.
5. **Matrix** — each criterion → `DONE` / `PARTIAL` / `MISSING` / `N/A (intentional)`.
6. **Write status (required unless user said dry-run)**
   - Update **Current phase** in `AGENTS.md` only (`CLAUDE.md` follows via symlink).
   - Tick matching checkboxes in `sb-roadmap.md` when evidence exists; keep the master tracker honest.
   - Append dated line(s) to `/.claude/memory/phase-progress.md`.
   - Bump AGENTS **and** CLAUDE Sync checklist “as of” when content changes.
7. **Trello (when asked)** — board https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices ; card map `/docs/trello.md`. Flip check items / update card `desc` to match matrix; do not move list without ask.
8. **Notion (when asked)** — only if an SB-MS Notion hub exists; do not write to Orbit Second Brain by default.
9. **Output** — matrix, files edited, Trello/Notion links touched, next recommended task, sign-off `Phase sync: UPDATED / DRY-RUN / BLOCKED`.
