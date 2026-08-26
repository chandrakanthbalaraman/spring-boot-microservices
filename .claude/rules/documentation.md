---
name: documentation
description: >-
  Orbit documentation constraints: ADRs, runbooks, benchmarks as required artifacts. Always apply for structural or operational changes.
role: Rule — docs & ADR requirements
model: inherit
color: pink
tools: none
user-invocable: false
---

**Role:** Rule — docs & ADR requirements

# Rule: Documentation

- Structural decisions → ADR in /docs/adr (never delete; only supersede).
- Operational behavior → runbook in /docs/runbooks.
- Perf claims → /docs/benchmarks with numbers.
- Phase architecture posters → /docs/phases (handwritten sketch; no status chrome — rule `phase-infographic.md`).
- Keep /.claude/memory/* dated and terse.
- Standing phase deliverables include diagrams, ADRs, security/perf/PRR checklists.
