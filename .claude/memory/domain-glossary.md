---
name: domain-glossary
description: >-
  Orbit ubiquitous language — one definition per domain term. Load when naming aggregates, modules, or APIs.
role: Memory — domain glossary / ubiquitous language
model: inherit
color: blue
tools: none
user-invocable: false
---

**Role:** Memory — domain glossary / ubiquitous language

# Domain Glossary

Ubiquitous language for Orbit. One definition per term. Keep dated updates terse.

Teaching note: `docs/architecture/ddd-fundamentals.md` (Phase 2 T1).

## Core modules (Phase 2)
| Term | Definition |
|------|------------|
| Auth | Authentication and credential/session concerns |
| User | User profile and identity attributes beyond auth credentials |
| Product | Catalog items offered for sale |
| Inventory | Stock levels and reservations for products |
| Order | Customer purchase aggregate and lifecycle |
| Payment | Capture/authorization of funds for an order |
| Notification | Outbound messages (email/SMS/push) triggered by domain events |
| Analytics | Read models and metrics derived from commerce events |

## Aggregate / DDD (Phase 2 T1 — fill in your own words)

> Replace each `TODO (T1): …` with one clear Orbit sentence. Do not paste Wikipedia.
> See examples in `docs/architecture/ddd-fundamentals.md`, then write the definition yourself.

| Term | Definition |
|------|------------|
| Entity | TODO (T1): object with lasting identity — e.g. how you’d explain User vs “a row of fields” |
| Value Object | TODO (T1): no identity, equality by values — e.g. Email / Money in Orbit |
| Aggregate root | TODO (T1): consistency boundary / only entry point — e.g. Order owning OrderItems; one per `@Transactional` |
| Bounded context | TODO (T1): language + model boundary — how it shows up as feature type prefixes in our monolith |
| Domain event | TODO (T1): fact that already happened — in-process now, Kafka later |

## Changelog
- 2026-08-07: Phase 2 T1 — five DDD term placeholders for human-authored definitions; linked `ddd-fundamentals.md`.
- 2026-07-30: Initial stub glossary for Phase 0.
