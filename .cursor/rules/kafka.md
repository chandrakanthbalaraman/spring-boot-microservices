---
name: kafka
description: >-
  Orbit Kafka constraints: prefer in-process events until Phase 5; then idempotency, outbox, schema evolution. Apply when messaging is in scope.
role: Rule — Kafka messaging discipline
model: inherit
color: orange
tools: none
user-invocable: false
---

**Role:** Rule — Kafka messaging discipline

# Rule: Kafka

- Prefer in-process domain events until Phase 5 distributed messaging is intentional.
- When using Kafka: idempotency, outbox, and schema/contract evolution are mandatory topics.
- Document event versioning decisions in ADRs / api-contracts memory.
- Handle poison messages and DLQs explicitly.
