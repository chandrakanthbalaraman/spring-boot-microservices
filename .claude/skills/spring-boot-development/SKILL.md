---
name: spring-boot-development
description: >-
  Orbit backend folder structure — Spring Boot 3.x single Maven module with
  package-by-layer (flat) only. Do not use hexagonal or package-by-feature.
role: Skill — Orbit package-by-layer layout
model: inherit
color: blue
---

**Role:** Skill — Orbit package-by-layer layout

# Backend Folder Structure — Orbit

> **When to use:** Locating or creating Java/resources files in the Orbit app.
> **Architecture:** Single Maven module, package-by-layer under `com.orbit`.  
> **Never:** hexagonal/ports-and-adapters, package-by-feature trees, Gradle.

---

## 1. Project Metadata

| Property         | Value                                      |
| ---------------- | ------------------------------------------ |
| **Framework**    | Spring Boot 3.x                            |
| **Java Version** | 21                                         |
| **Build Tool**   | Maven (single module — not Gradle)         |
| **DB**           | PostgreSQL 16                              |
| **Migrations**   | Flyway                                     |
| **Mapping**      | Manual `{Feature}Mapper` now; MapStruct later (ADR-0005) |
| **Schema design**| Skill `schema-design` → entity + Flyway     |
| **Security**     | Spring Security + JWT / OAuth2 (later)     |
| **Docs**         | SpringDoc OpenAPI                          |
| **Boilerplate**  | Lombok (Lombok-first — see `.claude/rules/lombok.md` / ADR-0004) |
| **Packaging**    | JAR + Docker                               |
| **Base package** | `com.orbit`                                |

---

## 2. Top-Level Layout

```
orbit-production-ready-system/
├── .claude/                         # AI platform (agents, skills, rules, …)
├── .mvn/                            # Maven wrapper
├── docker/                          # Compose layers (Postgres, Redis, …)
├── docs/
├── platform/                        # Terraform / infra
├── src/
│   ├── main/
│   │   ├── java/com/orbit/          # ← Application root package
│   │   └── resources/               # Config, Flyway, templates
│   └── test/
│       └── java/com/orbit/
├── pom.xml                          # Single Maven module
├── mvnw / mvnw.cmd
├── CLAUDE.md
├── AGENTS.md
└── README.md
```

---

## 3. Java Source Tree — `com.orbit`

**Package-by-layer (flat) only.** Features are type name prefixes, not packages.

```
com.orbit/
│
├── OrbitApplication.java               # @SpringBootApplication
│
├── common/                              # ── Cross-Cutting Concerns ──
│   ├── dto/                             # Shared API response contracts
│   │   ├── ApiResponse.java
│   │   ├── ErrorResponse.java
│   │   └── PageResponse.java
│   ├── exception/
│   │   ├── BusinessException.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   ├── filters/
│   │   ├── CorrelationIdFilter.java
│   │   └── RequestLoggingFilter.java
│   ├── security/
│   │   ├── AuthenticationFilter.java
│   │   ├── TokenProvider.java
│   │   └── AuthProperties.java
│   ├── types/
│   └── utils/
│
├── config/
│   ├── AppConfig.java
│   ├── SecurityConfig.java
│   ├── OpenApiConfig.java
│   └── {Feature}Config.java
│
├── controller/
│   ├── {Feature}Controller.java
│   └── ...
│
├── dto/
│   ├── {Feature}Request.java
│   ├── {Feature}Response.java
│   └── {feature}/                       # optional when a feature has many DTOs
│       ├── {Feature}Request.java
│       └── {Feature}Response.java
│
├── entity/
│   └── {Entity}.java
│
├── enums/
│   └── {DomainConcept}.java
│
├── exception/
│   └── {Condition}Exception.java
│
├── mapper/
│   └── {Feature}Mapper.java
│
├── repository/
│   └── {Entity}Repository.java
│
├── scheduler/
│   └── {Purpose}Scheduler.java
│
└── service/
    ├── {Feature}Service.java            # Interface (controllers inject this)
    ├── impl/                            # Implementation layer (required)
    │   └── {Feature}ServiceImpl.java    # @Service — never referenced from controllers
    └── {subsystem}/                     # Strategy-based subsystems only
        ├── {Strategy}Interface.java
        └── provider/
            └── {Implementation}.java
```

**Service layer rule:** interfaces in `com.orbit.service`; implementations in
`com.orbit.service.impl`. Controllers depend on interfaces only.

**Forbidden packages:** `domain`, `application`, `infrastructure`, `port`, `adapter`,
feature-root trees like `order.controller` / `order.service`.

---

## 4. Resources Directory

```
src/main/resources/
├── application.yml                      # or application.yaml
├── application-{profile}.yml            # profile overrides (local, staging, prod)
├── db/
│   └── migration/
│       ├── V001__{description}.sql
│       └── ...
├── templates/                           # email / HTML templates (when needed)
└── META-INF/
    └── additional-spring-configuration-metadata.json
```

---

## 5. Where to Put New Files

| What You're Creating     | Where It Goes                                        |
| ------------------------ | ---------------------------------------------------- |
| New REST endpoint        | `controller/{Feature}Controller.java`                |
| Request / Response DTO   | `dto/{Feature}Request.java` or `dto/{feature}/`      |
| JPA entity               | `entity/{Entity}.java`                               |
| Enum                     | `enums/{EnumName}.java`                              |
| DB migration             | `resources/db/migration/V{NNN}__{description}.sql`   |
| Repository               | `repository/{Entity}Repository.java`                 |
| Service interface        | `service/{Feature}Service.java`                      |
| Service implementation   | `service/impl/{Feature}ServiceImpl.java`             |
| Mapper                   | `mapper/{Feature}Mapper.java`                        |
| Spring configuration     | `config/{Name}Config.java`                           |
| Feature exception        | `exception/{Name}Exception.java`                     |
| Shared DTO / errors      | `common/dto/`, `common/exception/`                   |
| Servlet filter           | `common/filters/{Name}Filter.java`                   |
| Auth infrastructure      | `common/security/`                                   |
| Scheduled job            | `scheduler/{Name}Scheduler.java`                     |
| Strategy interface       | `service/{subsystem}/{Strategy}Interface.java`       |
| Strategy implementation  | `service/{subsystem}/provider/{Implementation}.java` |
| Email / HTML template    | `resources/templates/{name}.html`                    |

---


## 6. Feature Inventory (Phase 2+)

Features exist as **named types** across layers, not as Maven/Gradle modules:

| Feature        | Controller | Service | Entity (examples) | Notes        |
| -------------- | ---------- | ------- | ----------------- | ------------ |
| **Auth**       | ✅         | Auth    | User, Token       | Phase 1/2    |
| **User**       | ✅         | User    | User              |              |
| **Product**    | ✅         | Product | Product           |              |
| **Inventory**  | ✅         | Inventory | StockItem       |              |
| **Order**      | ✅         | Order   | Order, OrderLine  |              |
| **Payment**    | ✅         | Payment | Payment           |              |
| **Notification** | ✅       | Notification | —              |              |
| **Analytics**  | ✅         | Analytics | —               |              |

---

## 7. Key Design Patterns

| Pattern                   | Where Applied                                                         |
| ------------------------- | --------------------------------------------------------------------- |
| **Layered architecture**  | controller → service (iface) → repository; impls in `service.impl`   |
| **DDD**                   | Aggregate roots, domain events (in-process first)                     |
| **Interface Segregation** | Service interfaces in `service/`, `@Service` impls in `service/impl/` |
| **Strategy**              | `service/{subsystem}/{Interface}` → `provider/{Implementation}`       |
| **Factory**               | Strategy factories or complex aggregate construction when needed      |
| **Global Error Handler**  | `common/exception/GlobalExceptionHandler`                             |
| **Filter Chain**          | Correlation ID → logging → auth                                       |
| **DTO Mapping**           | Manual `{Feature}Mapper` in `mapper/` for entity→DTO (ADR-0005); MapStruct deferred; create path stays in service; entities never returned as JSON |
| **Embeddable VOs**        | Single-field VOs `@Embeddable` + `@AttributeOverride` on aggregate — no `AttributeConverter` (ADR-0005) |
| **Lombok (first)**        | Beans `@RequiredArgsConstructor`/`@Slf4j`; aggregates major entity set; VOs/DTOs `@Data`+`@Builder` — `lombok.md` / ADR-0004 |

---

## 8. Naming Conventions

| Type              | Pattern                                | Example                         |
| ----------------- | -------------------------------------- | ------------------------------- |
| Controller        | `{Feature}Controller`                  | `OrderController`               |
| Service Interface | `{Feature}Service`                     | `OrderService`                  |
| Service Impl      | `{Feature}ServiceImpl`                 | `OrderServiceImpl`              |
| Repository        | `{Entity}Repository`                   | `OrderRepository`               |
| Entity            | `{DomainNoun}`                         | `Order`                         |
| Request DTO       | `{Feature}Request` / `{Action}Request` | `OrderRequest`, `LoginRequest`  |
| Response DTO      | `{Feature}Response`                    | `OrderResponse`                 |
| Mapper            | `{Feature}Mapper` (manual; MapStruct later) | `UserMapper`                    |
| Enum              | `{DomainConcept}`                      | `OrderStatus`, `PaymentType`    |
| Exception         | `{Condition}Exception`                 | `ResourceNotFoundException`     |
| Filter            | `{Purpose}Filter`                      | `CorrelationIdFilter`           |
| Config            | `{Concern}Config`                      | `SecurityConfig`                |
| Scheduler         | `{Purpose}Scheduler`                   | `CleanupScheduler`              |
| Flyway Migration  | `V{NNN}__{snake_case_desc}.sql`        | `V001__create_orders_table.sql` |

---

## 9. Migration Naming Rules

```
V{NNN}__{description_in_snake_case}.sql
```

- **NNN** = zero-padded 3-digit sequential number
- Double underscore `__` between version and description
- Description uses `snake_case`
- Pick the next unused `V{NNN}` after existing files under `db/migration/`

---

## 10. Docker & Infrastructure

```
orbit-production-ready-system/
├── Dockerfile                       # Multi-stage build (when added)
├── docker/
│   ├── compose.yml                  # Entry compose file
│   ├── compose.base.yml             # Shared base services
│   └── compose.layer*.yml           # Layered infra (data, messaging, …)
└── platform/terraform/              # Cloud infra (not app runtime)
```

Local infra runs via Compose under `docker/` (Postgres required for the app; Redis/Kafka as layers land). Do not put app business packages under `docker/` or `platform/`.

---

## 11. Build commands (Maven)

```bash
./mvnw verify          # test + package
./mvnw spring-boot:run # local run (needs Compose Postgres)
./mvnw -DskipTests package
```

Never introduce Gradle wrappers or `build.gradle` for the app.
