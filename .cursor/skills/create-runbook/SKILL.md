---
name: create-runbook
description: >-
  Draft an operational runbook under docs/runbooks for a failure mode or procedure. Use before incidents or after simulations.
role: Skill — runbook drafting procedure
model: sonnet
color: yellow
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — runbook drafting procedure

# Skill: create-runbook

Trigger: user asks for a runbook or post-simulation procedure.

## Method
1. Follow docs-runbook-writer agent structure.
2. Symptoms → diagnosis via telemetry → mitigation → escalation.
3. Place under /docs/runbooks.
4. Output: runbook path + draft.
