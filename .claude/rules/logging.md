---
name: logging
description: >-
  Orbit logging constraints: structured JSON and correlation IDs. Always apply for logging/observability changes.
role: Rule — structured logging
model: inherit
color: pink
tools: none
user-invocable: false
---

**Role:** Rule — structured logging

# Rule: Logging

- Structured JSON logging from the start.
- Propagate correlation IDs across requests and async boundaries.
- No secrets or PII in logs without redaction policy.
- Log enough to diagnose via runbooks without reading code first.
