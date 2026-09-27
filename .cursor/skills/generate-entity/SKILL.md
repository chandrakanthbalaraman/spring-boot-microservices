---
name: generate-entity
description: >-
  Sketch JPA entities or domain aggregates for Orbit with correct aggregate boundaries. Use when adding persistence models or domain entities.
role: Skill — entity/aggregate sketch generator
model: sonnet
color: cyan
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — entity/aggregate sketch generator

# Skill: generate-entity

Trigger: user asks for an entity, aggregate, or persistence model.

## Method
1. Confirm aggregate root and invariants with DDD rules (/.claude/rules/ddd.md) and schema ownership (/.claude/rules/schema.md). Prefer skill `schema-design` when table boundaries are unclear.
2. Place JPA types under `com.orbit.entity` (package-by-layer — never hexagonal packages).
3. Prefer domain model clarity over anemic getters/setters. On aggregate roots, use the explicit major Lombok set (`@Getter`/`@Setter`/`@Builder`/`@NoArgsConstructor`/`@AllArgsConstructor`/id `@EqualsAndHashCode` — not `@Data`). Value objects and API DTOs use `@Data`+`@Builder`+args ctors (`lombok.md` / ADR-0004). Do not introduce new VO/API records.
4. Persist single-field VOs as `@Embeddable` with `@Embedded`/`@EmbeddedId` + `@AttributeOverride` on the aggregate — **no** `AttributeConverter` classes (ADR-0005).
5. Pair with Flyway migration via generate-flyway — never rely on ddl-auto.
6. Flag repository query impacts.
7. Output: entity sketch in `entity/` + migration note + test ideas for invariants.
