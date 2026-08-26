---
name: generate-test
description: >-
  Sketch unit or integration tests for Orbit following the testing rule (pure unit vs Testcontainers). Use when adding coverage for new behavior.
role: Skill — test sketch generator
model: sonnet
color: cyan
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — test sketch generator

# Skill: generate-test

Trigger: user asks for tests for a class, endpoint, or module behavior.

## Method
1. Choose layer: unit (no Spring) vs integration (Testcontainers).
2. For new endpoints: happy path + validation failure + authz failure.
3. Follow /.claude/rules/testing.md flaky-test policy.
4. Output: test class sketch + fixtures needed.
