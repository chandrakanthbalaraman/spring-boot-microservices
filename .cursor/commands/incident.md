---
name: incident
description: >-
  Production incident response for Orbit: mitigate first, then fix, then runbook/ADR/observability. Use only for live incidents.
role: Command — production incident response
model: opus
color: red
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
disable-model-invocation: true
---

**Role:** Command — production incident response

# /incident

Production incident response. Use dashboards/logs/metrics/traces before reading code.

## Steps
1. Declare severity; diagnose from telemetry first
2. Mitigate (rollback, feature flag, degrade) per existing runbook if any
3. Form hypothesis; only then inspect code
4. **(human)** fix or mitigate permanently
5. **docs-runbook-writer**: create/update runbook from what you learned
6. **docs-adr-generator**: ADR if a systemic gap is exposed
7. **qa-observability-engineer**: add missing signals so the next time is faster
8. Retro notes → `known-issues.md` / `future-improvements.md` as needed
