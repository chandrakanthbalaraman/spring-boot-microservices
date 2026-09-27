---
name: release
description: >-
  Orbit release checklist: notes, migration safety, gated deploy, post-deploy watch. Use when cutting a release.
role: Command — release and gated deploy workflow
model: sonnet
color: green
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
disable-model-invocation: true
---

**Role:** Command — release and gated deploy workflow

# /release

Release checklist: notes, migrations, gated deploy, post-deploy signals.

## Steps
1. Confirm tests, security scan, and migration safety (expand/contract)
2. **docs-release-notes**: draft notes + ops checklist
3. **docs-adr-generator**: ensure accepted ADRs for structural changes are linked
4. **(human)** gated deploy
5. **qa-observability-engineer**: watch golden + business signals post-deploy
6. **docs-runbook-writer**: update if release changes operational behavior
