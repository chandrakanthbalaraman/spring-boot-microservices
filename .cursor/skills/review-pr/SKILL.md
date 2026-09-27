---
name: review-pr
description: >-
  Review an Orbit PR against project rules and constraints. Use after human implementation for correctness, style, and checklist coverage.
role: Skill — PR review procedure
model: opus
color: green
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Skill — PR review procedure

# Skill: review-pr

Trigger: user asks to review a PR or recent changes.

## Method
1. Load /.claude/rules/*.md and CLAUDE.md non-negotiables.
2. Prefer qa-senior-code-reviewer output format.
3. Check tests, OpenAPI, Flyway, injection style, transaction boundaries, and Lombok policy (`lombok.md`).
4. Output: ranked findings + PASS / PASS WITH NOTES / BLOCKED.
