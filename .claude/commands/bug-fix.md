---
name: bug-fix
description: >-
  Diagnose and fix an Orbit bug with telemetry-first diagnosis and a regression test. Use when investigating failures or defects.
role: Command — bug diagnosis and minimal fix workflow
model: sonnet
color: yellow
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Command — bug diagnosis and minimal fix workflow

# /bug-fix

Diagnose and fix a bug with a regression test. Prefer telemetry before code diving.

## Steps
1. Reproduce and capture evidence (logs/metrics/traces)
2. Identify owning aggregate/module (**arch-domain-architect** if unclear)
3. **(human)** implement minimal fix + regression test
4. **qa-test-engineer**: confirm regression coverage
5. **qa-senior-code-reviewer**: review fix scope
6. **qa-security-engineer**: if the bug is security-related
7. **docs-runbook-writer**: update runbook if this was an operational surprise
8. **(human)** merge; consider `/.claude/memory/known-issues.md` if residual risk is deferred
