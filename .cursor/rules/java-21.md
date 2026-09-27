---
name: java-21
description: >-
  Orbit Java 21 language/runtime constraints (virtual threads, sealed types). Always apply when writing or reviewing Java code.
role: Rule — Java 21 language & runtime
model: inherit
color: cyan
tools: none
user-invocable: false
---

**Role:** Rule — Java 21 language & runtime

# Rule: Java 21

- Target language level: Java 21.
- Use virtual threads where blocking I/O is unavoidable; do not assume they help CPU-bound work.
- Prefer Lombok `@Data` / `@Builder` for immutable-shaped Orbit VOs and API DTOs (see `lombok.md` / ADR-0004) — do not default to records for those types.
- Sealed types remain available for closed hierarchies when they fit.
- Avoid thread-locals and pinning pitfalls with virtual threads.
