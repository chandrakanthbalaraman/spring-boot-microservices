---
name: security
description: >-
  Orbit security constraints: validation, secrets, AuthZ, parameterized SQL, CVE hygiene. Always apply for security-sensitive changes.
role: Rule — security baseline
model: inherit
color: red
tools: none
user-invocable: false
---

**Role:** Rule — security baseline

# Rule: Security

- Validate input at every trust boundary.
- No secrets in code, logs, or committed config.
- Authorization is server-side — never trust client role claims alone.
- Parameterized SQL only.
- Dependency CVE scanning in CI; fix or explicitly waive with rationale.
