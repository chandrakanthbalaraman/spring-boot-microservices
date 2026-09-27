---
name: redis
description: >-
  Orbit Redis constraints: explicit invalidation, key/TTL conventions, degradation behavior. Apply when caching is in scope.
role: Rule — Redis caching discipline
model: inherit
color: orange
tools: none
user-invocable: false
---

**Role:** Rule — Redis caching discipline

# Rule: Redis

- Cache with an explicit invalidation strategy — no accidental stale-forever caches.
- Document key naming and TTL conventions.
- Failures: define degradation behavior (fail open vs closed) in runbooks.
- Do not use Redis as a substitute for the system of record without an ADR.
