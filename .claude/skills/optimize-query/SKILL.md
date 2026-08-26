---
name: optimize-query
description: >-
  Analyze and improve SQL/repository queries using EXPLAIN ANALYZE and indexing strategy. Use when queries are slow or N+1 is suspected.
role: Skill — query optimization procedure
model: opus
color: orange
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — query optimization procedure

# Skill: optimize-query

Trigger: user reports slow queries or asks to optimize a repository query.

## Method
1. Get EXPLAIN ANALYZE (or equivalent) before changing indexes.
2. Prefer indexes matched to real predicates; avoid speculative indexes.
3. Pair schema changes with generate-flyway.
4. Record before/after in /docs/benchmarks when material.
5. Output: diagnosis + proposed migration/query change.
