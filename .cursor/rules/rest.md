---
name: rest
description: >-
  Orbit REST constraints: resources, OpenAPI on every public method, stable errors, versioning. Always apply for HTTP APIs.
role: Rule — REST & OpenAPI
model: inherit
color: blue
tools: none
user-invocable: false
---

**Role:** Rule — REST & OpenAPI

# Rule: REST

- Resource-oriented URLs; consistent nouns and status codes.
- Every public API method has OpenAPI annotations.
- Stable error contract across modules.
- Version deliberately; record stable shapes in /.claude/memory/api-contracts.md.
- Distinguish internal vs external APIs when both exist.
