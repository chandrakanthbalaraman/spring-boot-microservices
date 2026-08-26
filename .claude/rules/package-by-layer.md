---
name: package-by-layer
description: >-
  Default layout inside each microservice is package-by-layer. Package-by-feature
  is allowed later when a service grows (sb-roadmap.md). Always apply when creating
  or reviewing Java packages.
role: Rule — package layout per service
model: inherit
color: purple
tools: none
user-invocable: false
---

**Role:** Rule — package layout per service

# Rule: Package-by-Layer (default per service)

- **Phase 1 default** inside each service (`product-service`, `inventory-service`, `order-service`, …):

  `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `exception`, `config`

  Base package: see `/.claude/memory/naming-conventions.md` (not `com.orbit`).

- **Dependency direction:** `controller` → `service` → `repository` / `entity`.
  Controllers do not call repositories directly.

- **Forbidden as the default:** hexagonal / ports-and-adapters (`domain`, `application`,
  `infrastructure`, `port`, `adapter`) as the primary tree.

- **Package-by-feature** (`product/controller`, `product/service`, plus `shared/`) is
  **allowed later** when a service is large enough — document the switch in an ADR.
  Do not start Phase 1 that way.

- This is **per microservice**, not one monolith package tree for the whole repo.
