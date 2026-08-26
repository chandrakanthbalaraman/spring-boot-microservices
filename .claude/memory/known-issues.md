---
name: known-issues
description: >-
  Deferred or rejected Orbit approaches — do not re-suggest. Load before proposing alternatives already discarded.
role: Memory — known issues & rejected approaches
model: inherit
color: red
tools: none
user-invocable: false
---

**Role:** Memory — known issues & rejected approaches

# Known Issues

Deliberately deferred items and rejected approaches — so they are not re-suggested.

## Format
`YYYY-MM-DD | Status | Issue — why deferred/rejected`

## Entries
- 2026-08-07 | Rejected | JPA `AttributeConverter` per VO — prefer `@Embeddable` + `@AttributeOverride` (ADR-0005).
- 2026-08-07 | Deferred | MapStruct for entity↔DTO — use manual `{Feature}Mapper` until multi-feature mapping volume (ADR-0005).
- 2026-07-31 | Rejected | Hexagonal / ports-and-adapters for Orbit app structure — prefer package-by-layer for learning clarity (`package-by-layer.md`).
- 2026-07-31 | Rejected | Gradle (multi-module or otherwise) as app build tool — use Maven single module instead.
- 2026-07-31 | Rejected | Package-by-feature trees (e.g. `order.controller`) as primary organization — use type prefixes in flat layers.
