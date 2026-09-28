---
name: performance-review
description: >-
  Orbit performance review with mandatory baselines and before/after benchmarks. Use when investigating latency or optimizing hot paths.
role: Command — performance review workflow
model: sonnet
color: yellow
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Command — performance review workflow

# /performance-review

Performance review with mandatory baselines. No optimization without measurements.

## Steps
1. **qa-performance-engineer**: define SLI and baseline
2. Use skill `benchmark-service`: capture numbers → `/docs/benchmarks`
3. Optimize (query/code) as indicated — **(human)** implements
4. Re-benchmark; record before/after with commit hash
5. **docs-adr-generator**: ADR if caching, pooling, or consistency tradeoffs change
6. Sign-off: `PASS` / `PASS WITH NOTES` / `BLOCKED`
