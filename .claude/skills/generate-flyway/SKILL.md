---
name: generate-flyway
description: >-
  Create a new versioned Flyway migration for Orbit schema changes. Use when adding/changing DB columns, tables, or indexes. Never edit already-applied migrations.
role: Skill — Flyway migration generator
model: opus
color: orange
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — Flyway migration generator

# Skill: generate-flyway

Trigger: user asks to add/change a DB column, table, or index in any module.

## Method
1. Confirm the change was designed via skill `schema-design` / rule `schema.md` (aggregate ownership clear).
2. Never modify an already-applied migration. Always create a new versioned file:
   V{next_number}__{snake_case_description}.sql
3. Write the migration to be backward-compatible with the currently-deployed app version
   (additive first, cleanup in a later migration) — this is what makes zero-downtime
   deploys possible.
4. Include a rollback note as a SQL comment even though Flyway doesn't auto-rollback.
5. Update the corresponding JPA entity / value type in the same PR, and flag if the change
   affects any existing repository query.
6. Output: the migration file path + contents, plus a one-line note for the ADR if this
   is a structural (not additive) change.
