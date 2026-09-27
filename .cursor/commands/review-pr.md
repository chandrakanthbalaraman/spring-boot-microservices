---
name: review-pr
description: >-
  Review current Orbit changes as a PR against rules and non-negotiables. Use after human implementation or before merge.
role: Command — PR review workflow
model: opus
color: green
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Command — PR review workflow

# /review-pr

Review current changes as a PR against project rules and non-negotiables.

## Steps
1. Load `CLAUDE.md` / `AGENTS.md` constraints and `/.claude/rules/*.md`
2. Use skill `review-pr` and **qa-senior-code-reviewer** output format
3. Check: constructor injection, Flyway, OpenAPI, transaction boundaries, tests, Lombok policy (`lombok.md`)
4. Optionally run security notes via **qa-security-engineer** if auth/input/SQL touched
5. Output ranked findings + sign-off: `PASS` / `PASS WITH NOTES` / `BLOCKED`
