---
name: testing
description: >-
  Orbit testing constraints: pure unit tests, Testcontainers for DB, endpoint triad, flaky-test policy. Always apply when writing or reviewing tests.
role: Rule — testing pyramid & policy
model: inherit
color: cyan
tools: none
user-invocable: false
---

**Role:** Rule — testing pyramid & policy

# Rule: Testing

- Unit tests: no Spring context. Pure JVM, <50ms per test.
- Integration tests: Testcontainers only — never mock the database.
- Every new endpoint requires: one happy-path test, one validation-failure test,
  one authz-failure test.
- Flaky test policy: quarantine immediately (a @Disabled with a linked ticket),
  never leave it red or silently retried.
