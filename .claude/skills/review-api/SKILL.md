---
name: review-api
description: >-
  Review REST/OpenAPI design for consistency, versioning, and error contracts. Use when changing public or internal HTTP APIs.
role: Skill — API design review procedure
model: sonnet
color: blue
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Skill — API design review procedure

# Skill: review-api

Trigger: user asks to review an API design or controller surface.

## Method
1. Apply /.claude/rules/rest.md and api-contracts memory.
2. Check OpenAPI completeness and breaking-change risk.
3. Output: findings + recommended contract updates.
