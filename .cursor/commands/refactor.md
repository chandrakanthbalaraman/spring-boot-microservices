---
name: refactor
description: >-
  Behavior-preserving Orbit refactor with boundary checks. Use when restructuring code without intentional behavior change.
role: Command — safe refactor workflow
model: sonnet
color: cyan
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Command — safe refactor workflow

# /refactor

Behavior-preserving refactor unless intentional changes are listed upfront.

## Steps
1. State the goal and non-goals; confirm no behavior change (or list intentional ones)
2. **arch-backend-architect** / **arch-domain-architect**: ensure boundaries stay intact
3. **(human)** refactor in small steps with tests green
4. **qa-test-engineer**: confirm pyramid still holds
5. **qa-senior-code-reviewer**: readability/maintainability pass
6. **docs-adr-generator**: ADR only if structure/dependency rules change
7. **(human)** merge
