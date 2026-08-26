---
name: generate-testcontainers
description: >-
  Set up Testcontainers-based integration tests for Postgres/Redis/Kafka in Orbit. Use when tests must touch real infra — never mock the database.
role: Skill — Testcontainers setup procedure
model: sonnet
color: cyan
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — Testcontainers setup procedure

# Skill: generate-testcontainers

Trigger: user needs integration tests against real containers.

## Method
1. Never mock the database for integration tests — use Testcontainers.
2. Prefer shared container lifecycle patterns appropriate to a single Maven module.
3. Isolate schema via Flyway in the test context.
4. Output: test configuration sketch + example test.
