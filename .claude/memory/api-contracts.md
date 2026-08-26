---
name: api-contracts
description: >-
  Stable Orbit API shapes and versioning history. Load when designing or changing HTTP contracts.
role: Memory — API contracts & versioning
model: inherit
color: blue
tools: none
user-invocable: false
---

**Role:** Memory — API contracts & versioning

# API Contracts

Stable external (and notable internal) API shapes and versioning history.

## Versioning policy
(TBD in Phase 4 — record the chosen strategy here once decided via ADR.)

## Stable contracts

### Users (Phase 2 T2)
- `POST /api/v1/users` — body `{ email, password }` → `201` `{ id, email, createdAt, updatedAt }` (no password)
- `GET /api/v1/users/{id}` — `200` same shape; `404` if missing
- Auth: HTTP Basic (Phase 1 in-memory smoke users); unauthenticated → `401`
- Duplicate email → `409`

## Changelog
- 2026-08-07: User create/get (`/api/v1/users`) — Phase 2 T2 Slice C.
- 2026-07-30: File created; awaiting first public API.
