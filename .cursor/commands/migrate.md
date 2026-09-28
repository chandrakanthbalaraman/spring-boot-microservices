---
name: migrate
description: >-
  Flyway-only Orbit schema change workflow with expand/contract safety. Use when adding or changing database schema.
role: Command — Flyway migration workflow
model: opus
color: orange
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Command — Flyway migration workflow

# /migrate

Database migration via Flyway. Never edit already-applied migrations.

## Steps
1. **dev-database-architect**: design change for backward compatibility
2. Use skill `generate-flyway`: new versioned migration only
3. Pair entity/repository updates in the same PR
4. **(human)** implement; run migration against Testcontainers locally
5. **qa-test-engineer**: integration coverage
6. **docs-adr-generator**: ADR if structural (not merely additive)
7. **(human)** deploy with expand → migrate → contract discipline as needed
