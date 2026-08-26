---
name: naming-conventions
description: >-
  SB-MS naming conventions for packages, Flyway, ADRs, Git, and Java types. Load when creating files or modules.
role: Memory — naming conventions
model: inherit
color: cyan
tools: none
user-invocable: false
---

**Role:** Memory — naming conventions

# Naming Conventions

## Packages

- Base package: `com.example.microservices` until the learner sets a permanent root (see TODO below).
- Organization: **package-by-layer per service** in early phases — `controller`, `service`,
  `repository`, `entity`, `dto`, `mapper`, `exception`, `config`,
  plus `service.impl` for `{Feature}ServiceImpl`.
- Later: package-by-feature inside a large service is allowed (ADR required).
- Agents: `{team}-{role}.md` under `.claude/agents/`.
- Flyway: `V{NNN}__{snake_case_description}.sql` under each service’s `src/main/resources/db/migration/`.
- ADRs: `NNNN-kebab-title.md` under `/docs/adr`.
- Phase folders: `phase-{NN}-{short-kebab}/` (e.g. `phase-01-microservices-basics`).

## Code (Java)

- Entities: domain nouns (`Order`, `Product`).
- Services: `{Feature}Service`.
- REST paths: plural nouns (`/api/products`, `/api/orders`, `/api/inventory/{productId}`).
- Lombok: follow `.claude/rules/lombok.md`.

## Build

- Maven **per phase** (parent POM in the phase folder + one module per service).
- Wrapper `./mvnw` inside each runnable service (or phase) once generated.
- Do not add Gradle.

## Git

- Conventional Commits: `phase 01: …`
- Feature branches: `feature/phase-{N}-{short-kebab-slug}` (skill: `create-phase-branch`).
- Milestone tags: `phase-01-complete`, `phase-02-complete`, … `capstone`.

## Changelog

- 2026-08-25: SB-MS conventions (replace Orbit `com.orbit` / single-module).

## TODO (learner)

Set the permanent Java base package (5–10 lines in this file under **Packages**):

- Keep `com.example.microservices` (roadmap default, fine for learning), or
- Pick a personal root (`com.chandrakanth.sbms`, …) and use it in every service.

This choice is reused for 25 phases — decide before generating Phase 1 Java files.
