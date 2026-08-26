---
name: new-feature
description: >-
  Plan and deliver a new Orbit feature via domain → architecture → human impl → review → security → tests → ADR → observability → runbook. Use when starting a new capability.
role: Command — end-to-end new feature workflow
model: opus
color: purple
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Command — end-to-end new feature workflow

# /new-feature

Plan and deliver a new Orbit feature. Do not implement the full feature unsupervised —
sketch shapes, flag ADRs, then hand implementation to the human.

## Steps
1. **arch-domain-architect**: confirm which aggregate(s)/bounded context this touches
2. **arch-backend-architect**: sketch module/package structure, flag ADR-worthy decisions
3. **(human)** implement
4. **qa-senior-code-reviewer**: review against `/.claude/rules/*.md`
5. **qa-security-engineer**: security pass
6. **qa-test-engineer**: confirm test coverage per `/.claude/rules/testing.md`
7. **docs-adr-generator**: draft ADR if step 2 flagged one
8. **qa-observability-engineer**: confirm metrics/logs/traces added
9. **(human)** merge, deploy
10. **docs-runbook-writer**: update runbook if this changes operational behavior
