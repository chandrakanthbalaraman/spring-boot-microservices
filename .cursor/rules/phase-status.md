---
name: phase-status
description: >-
  SB-MS phase-status constraints: Current phase in AGENTS.md, evidence before DONE,
  no silent phase jumps. Always apply when updating roadmap or progress docs.
role: Rule — phase status honesty
model: inherit
color: cyan
tools: none
user-invocable: false
---

**Role:** Rule — phase status honesty

# Rule: Phase status

- **Current phase** lives in `AGENTS.md` only (`CLAUDE.md` is a symlink to it).
- Do not mark a deliverable **DONE** without repo (or verified runtime) evidence.
- Prefer a precise in-progress line over jumping phase numbers early.
- Long-form criteria stay in `sb-roadmap.md`; progress notes go in `/.claude/memory/phase-progress.md`.
- Use `/sync-phase-status` (skill `sync-phase-status`) instead of ad-hoc status edits.
- Trello / Notion are optional mirrors — update only when the human asks.
  - Board: [SB-MS — Spring Boot Microservices](https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices)
  - Card map: `/docs/trello.md`
  - Default review card (Phase 00): https://trello.com/c/goY5ol2b
