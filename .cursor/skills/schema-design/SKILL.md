---
name: schema-design
description: >-
  Design Orbit Postgres/Flyway schemas and aggregate-aligned entity models (ERD/LLD style).
  Use whenever the user asks for schema design, table layout, entity diagrams, aggregate
  persistence mapping, indexing, or DB architecture for commerce features (User, Product,
  Inventory, Order, Payment, Notification, Analytics) — even if they only say "model the
  tables", "draw the entities", or "how should this look in the DB". Prefer this before
  generate-entity / generate-flyway so boundaries are locked first.
role: Skill — schema & aggregate design
model: inherit
color: orange
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — schema & aggregate design

# Skill: schema-design

Trigger: schema design, ERD/LLD entity diagrams, table modeling, aggregate→DB mapping,
indexing for Orbit features.

## Why this skill exists

`generate-entity` sketches Java types; `generate-flyway` writes migrations. This skill sits
**before** both: lock aggregate ownership, FK direction, and transaction boundaries so
migrations do not invent a second domain model.

Canonical visual: `docs/architecture/entity-diagram.png` (+ companion markdown).
Rules: `.claude/rules/schema.md`, `.claude/rules/ddd.md`.
Persona: **dev-database-architect** for migration safety sign-off.

## Method

1. **Load context**
   - Read `docs/architecture/entity-diagram.md` (and PNG if discussing visuals).
   - Read `.claude/rules/schema.md` + `ddd.md` + `package-by-layer.md`.
   - Check existing Flyway under `src/main/resources/db/migration/` and `entity/`.

2. **Name the aggregate root(s)**
   - One write model = one aggregate root = one `@Transactional` boundary.
   - Cross-aggregate links are **IDs + domain events**, never shared TX / cascade deletes across roots.

3. **Produce an LLD-style schema sketch** (elevator-diagram shape)
   - Classes overview (one line each)
   - Entity boxes: key fields + invariant methods (not every getter)
   - Enums / status vocabularies
   - Relationships: composition vs association (FK)
   - Responsibilities + deferred items (Auth JWT, Kafka, etc.)

4. **Map to Postgres**
   - Table per aggregate root (or explicit child table for composition, e.g. `order_lines`)
   - UUID PKs; `TIMESTAMPTZ` for audit columns
   - Unique constraints for natural keys (`users.email`)
   - Indexes only with a named query pattern
   - Never `ddl-auto` mutate — next step is `generate-flyway`

5. **Hand off**
   - Flag ADR if structural (new aggregate boundary, rename, destructive change)
   - Point human to implement; then `generate-entity` → `generate-flyway`
   - Sign-off line: `Schema design: READY FOR HUMAN / NEEDS DISCUSSION`

## Output template

```markdown
## Schema design — {Feature}

### Aggregate root
…

### Tables (proposed)
| Table | Owns | Key columns | Notes |
|-------|------|-------------|-------|

### Relationships
…

### Invariants
…

### Indexes (query-backed)
…

### Migration plan
- Next Flyway: V{NNN}__…
- Expand/contract notes

### Deferred
…

Schema design: READY FOR HUMAN / NEEDS DISCUSSION
```

## Anti-patterns

- God tables spanning User+Order+Payment
- FK cascades that delete across aggregate roots
- Editing applied Flyway files
- Inventing microservices packages for schema ownership
- Putting API DTO fields into the persistence model without an aggregate story
