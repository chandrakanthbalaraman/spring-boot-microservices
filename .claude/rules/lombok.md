---
name: lombok
description: >-
  Orbit Lombok-first policy: major decorators on beans, entities, VOs, DTOs;
  prefer explicit entity set over @Data on aggregates; no VO/API records.
role: Rule — Lombok usage
model: inherit
color: yellow
tools: none
user-invocable: false
---

**Role:** Rule — Lombok usage

# Rule: Lombok (Lombok-first)

Orbit uses **major Lombok decorators** as the default style for Spring beans, aggregates,
value objects, and API DTOs. Prefer Lombok over Java records for Orbit VOs/DTOs.
See ADR-0004 and `docs/superpowers/specs/2026-08-07-lombok-first-immutables-design.md`.

## Allowed

### Spring beans
- `@RequiredArgsConstructor` — constructor injection; no field `@Autowired`
- `@Slf4j` when logging is needed

### Aggregates / JPA entities (`com.orbit.entity` roots)
Prefer the **explicit major set** (not `@Data`):
- `@Getter` / `@Setter`
- `@NoArgsConstructor` / `@AllArgsConstructor`
- `@Builder`
- `@ToString`
- `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` with `@EqualsAndHashCode.Include` on the id

### Value objects + API DTOs
- `@Data`
- `@Builder`
- `@NoArgsConstructor` / `@AllArgsConstructor`
- Static factories (`Email.of`, `UserId.newId`, …) still own validation / normalization

## Forbidden

- `@Data` on **JPA aggregate roots** — use the explicit set above; id-based equality only
- New Java **records** for Orbit VOs or API DTOs
- Field `@Autowired`
- Lombok on constant/utility holders (e.g. role name classes) — plain `private` constructor

## Domain construction

Prefer factories (`User.register`, `Email.of`) for invariant paths even when `@Builder` /
`@AllArgsConstructor` exist for frameworks (JPA, Jackson, tests).

## Dependency

`org.projectlombok:lombok` with `optional=true`; version from Spring Boot parent — do not pin unless parent stops managing it.
