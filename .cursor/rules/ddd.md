---
name: ddd
description: >-
  Orbit DDD constraints: single Maven monolith, feature domains as named types,
  aggregate invariants, one-aggregate transactions. Always apply for domain modeling.
role: Rule — DDD (layered monolith)
model: inherit
color: blue
tools: none
user-invocable: false
---

**Role:** Rule — DDD (layered monolith)

# Rule: DDD

- Single Maven monolith with package-by-layer — not hexagonal, not multi-module Gradle.
- Feature domains (Auth, User, Product, Inventory, Order, Payment, Notification,
  Analytics) are **named types** across layers (`OrderController`, `OrderService`,
  `Order` entity), not separate Maven modules or feature package trees.
- Protect aggregate invariants inside the aggregate root / entity cluster.
- One aggregate root per `@Transactional` boundary.
- Prefer domain events (in-process first) for cross-aggregate reactions.
- Ubiquitous language lives in /.claude/memory/domain-glossary.md.
- Layout: `/.claude/rules/package-by-layer.md` and
  `/.claude/skills/spring-boot-development/SKILL.md`.
