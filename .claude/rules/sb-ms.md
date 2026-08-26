---
name: sb-ms
description: >-
  SB-MS project identity. Always apply. This repo is Spring Boot microservices
  learning, not the Orbit layered monolith. AGENTS.md + sb-roadmap.md win on conflict.
alwaysApply: true
role: Rule — SB-MS identity
model: inherit
color: orange
tools: none
user-invocable: false
---

**Role:** Rule — SB-MS identity

# Rule: SB-MS (not Orbit)

- This repository is **Spring Boot Microservices** (`SB-MS`), not Orbit.
- Canonical brief: `/AGENTS.md` (symlinked as `CLAUDE.md` and `agents.md`).
- Curriculum: `/sb-roadmap.md`. Cursor overlay: `/cursor.md`.
- One Git repo + independently runnable **phase folders**. Do not create all 25 phases up front.
- Database-per-service. Maven per phase (not one giant reactor on day one).
- If another rule, agent, or skill still says Orbit / `com.orbit` / Maven single-module monolith / “never package-by-feature”, follow **this rule and `AGENTS.md`**.
- Edit `.claude/` only. `.cursor/{agents,skills,commands,rules}` are symlinks.
