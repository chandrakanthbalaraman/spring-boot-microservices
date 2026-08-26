---
name: performance
description: >-
  Orbit performance constraints: measure first; benchmarks with commit hash. Always apply before optimizing.
role: Rule — performance discipline
model: inherit
color: yellow
tools: none
user-invocable: false
---

**Role:** Rule — performance discipline

# Rule: Performance

- Measure before optimizing; record before/after in /docs/benchmarks with commit hash.
- Watch for N+1 queries, missing indexes, unbounded collections.
- Size pools (e.g. HikariCP) deliberately — document the formula used.
- Virtual threads are not a substitute for profiling.
