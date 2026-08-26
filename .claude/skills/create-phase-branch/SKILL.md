---
name: create-phase-branch
description: >-
  Create a feature branch from main to start the next Orbit roadmap phase. Use when the user says start phase N, create phase branch, or begin local/setup work for a phase — not for full phase scaffolding.
role: Skill — phase branch bootstrap
model: inherit
color: green
tools: Bash, Read
allowed-tools: Bash, Read
---

**Role:** Skill — phase branch bootstrap

# Skill: create-phase-branch

Trigger: user wants to start the next roadmap phase and open a feature branch. Do **not** scaffold the whole phase.

## Branch name

```
feature/phase-{N}-{short-kebab-slug}
```

Examples: `feature/phase-0.5-local-develop-setup`, `feature/phase-1-maven-foundation`.

## Method

1. Confirm working tree is clean enough to branch (warn on unrelated dirty files; do not discard).
2. `git checkout main` && `git pull --ff-only` (if remote exists).
3. `git checkout -b feature/phase-{N}-{slug}` using the name the user gave, or propose one and wait if unclear.
4. Stop. Report branch name. Wait for the next task — no Compose, Terraform, CI, or module scaffolding unless asked.

## Out of scope

- Implementing phase deliverables
- Updating `Current phase` — use `/sync-phase-status` when the user wants progress docs updated
- Opening a PR
