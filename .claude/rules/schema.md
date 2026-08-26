---
name: schema
description: >-
  Orbit Postgres/Flyway schema constraints: aggregate-aligned tables, additive
  migrations, UUID PKs, no ddl-auto. Always apply for DB or entity persistence work.
role: Rule — schema & persistence
model: inherit
color: orange
tools: none
user-invocable: false
---

**Role:** Rule — schema & persistence

# Rule: Schema

- **Flyway only** for schema changes — never Hibernate `ddl-auto` create/update; prefer `validate`.
- **Never edit** an already-applied migration; always add `V{NNN}__…`.
- Prefer **additive** (expand) migrations; cleanup (contract) in a later version.
- Align tables with **aggregate roots** — one root per `@Transactional`; child rows only for composition (e.g. `order_lines` owned by `orders`).
- Cross-aggregate references use **IDs** (and later domain events) — no cascading deletes across roots.
- Default keys: **UUID** PKs; timestamps as **TIMESTAMPTZ**.
- Unique constraints for natural keys (e.g. `users.email`).
- Indexes require a **named query pattern**, not guesswork.
- Design first via skill `schema-design`; then `generate-entity` / `generate-flyway`.
- Visual reference: `docs/architecture/entity-diagram.png` + `entity-diagram.md`.
