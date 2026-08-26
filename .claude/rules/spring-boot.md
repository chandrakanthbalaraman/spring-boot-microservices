---
name: spring-boot
description: >-
  Orbit Spring Boot constraints: constructor injection, Flyway-only schema, externalized secrets. Always apply for Spring code.
role: Rule — Spring Boot 3.x conventions
model: inherit
color: green
tools: none
user-invocable: false
---

**Role:** Rule — Spring Boot 3.x conventions

# Rule: Spring Boot

- Spring Boot 3.x.
- Constructor injection only — no field injection. Prefer `@RequiredArgsConstructor` (Lombok) over field `@Autowired`; see `lombok.md`.
- No Hibernate `ddl-auto` for schema changes — Flyway only.
- Externalize configuration; secrets via Secrets Manager (or local equivalents), never plaintext in git.
- Prefer explicit configuration over magic when behavior is security- or consistency-sensitive.
