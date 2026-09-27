---
name: create-feature
description: >-
  Plan a new Orbit feature end-to-end using the new-feature workflow — domain boundaries, module sketch, ADR flags. Use when the user asks to add a feature or capability.
role: Skill — feature planning procedure
model: opus
color: purple
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — feature planning procedure

# Skill: create-feature

Trigger: user asks to add a new feature or capability to Orbit.

## Method
1. Follow /.claude/commands/new-feature.md — do not skip architecture steps.
2. Invoke domain + backend architecture thinking first; sketch shapes only.
3. Flag ADR-worthy decisions; do not write full unsupervised implementation.
4. List tests required per /.claude/rules/testing.md.
5. Apply Lombok-first policy (`lombok.md` / ADR-0004); VOs/DTOs use `@Data`+`@Builder`; aggregates use the explicit major entity set.
6. Output: plan + class/interface sketch + ADR candidates + test checklist.
