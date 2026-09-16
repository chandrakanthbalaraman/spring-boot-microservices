# Spring Boot Microservices — Consolidated Phase Checklist

Hands-on progression: **Microservices 101 → distributed systems → production architecture → Kubernetes → capstone**.

Skip Java fundamentals and Spring Boot fundamentals. Start at Spring Boot Microservices Engineering and learn by building one production-grade system that evolves phase by phase.

---

## Target Outcome

By the end, you should be able to **design, implement, test, containerize, deploy, observe, secure, and troubleshoot** a production-style Spring Boot microservices platform.

- [ ] Design service boundaries and APIs
- [ ] Implement, test, and debug independent Spring Boot services
- [ ] Containerize and run the full local stack
- [ ] Deploy and operate on Kubernetes
- [ ] Observe with logs, metrics, and traces
- [ ] Secure with OAuth2/JWT at the gateway and services
- [ ] Troubleshoot failures across service, network, database, and broker layers

---

## Target Platform Architecture

```
                    ┌──────────────────────┐
                    │     Client / UI       │
                    └──────────┬───────────┘
                               │
                         HTTPS / WebSocket
                               │
                    ┌──────────▼───────────┐
                    │    API Gateway       │
                    │ Spring Cloud Gateway │
                    └──────────┬───────────┘
                               │
          ┌────────────────────┼────────────────────┐
          │                    │                    │
   ┌──────▼──────┐      ┌──────▼──────┐      ┌──────▼──────┐
   │ User Service │      │Order Service│      │Product Svc  │
   └──────┬──────┘      └──────┬──────┘      └──────┬──────┘
          │                    │                    │
          └────────────┬───────┴────────────┬───────┘
                       │                    │
                ┌──────▼──────┐      ┌──────▼──────┐
                │ PostgreSQL  │      │    Redis    │
                └─────────────┘      └─────────────┘

                       Infrastructure
                              │
       ┌──────────────┬───────┼────────┬──────────────┐
       ▼              ▼       ▼        ▼              ▼
   Discovery       Kafka   Tracing  Metrics       Kubernetes
   Config          Events  OTEL     Prometheus     Docker
```

---

## How We Learn Each Phase

Do **not** follow Theory → Theory → Theory.

For every phase, follow this loop:

```
CONCEPT → WHY IT EXISTS → ARCHITECTURE → MINIMAL CODE → RUN IT
      → BREAK IT → DEBUG IT → PRODUCTION VERSION → CAPSTONE INTEGRATION
```

**Example — Circuit Breaker**

1. Build the service-to-service call
2. Shut down the dependency
3. Observe the failure
4. Add timeout
5. Add retry
6. Add circuit breaker
7. Measure behavior
8. Add fallback
9. Test recovery
10. Integrate into the capstone

### Lesson Pattern (every phase)

- [ ] **Architecture** — What are we building? Why? Where does this component belong?
- [ ] **Repository** — Exact folders/files for this phase
- [ ] **Code** — Implement together, incrementally
- [ ] **Run** — Exact commands (`./mvnw clean install`, `./mvnw spring-boot:run`)
- [ ] **Test** — curl, Postman, JUnit, integration tests
- [ ] **Break** — Intentional failure exercises
- [ ] **Debug** — Logs, HTTP status, stack trace, timeout, database, network
- [ ] **Production upgrade** — How would we make this production-ready? (introduces the next phase)

### Learning Rules

- [ ] One Git repository for the entire journey (not 25 repos)
- [ ] Each phase is independently runnable
- [ ] Later phases evolve the same application; do not throw away code
- [ ] Do **not** dump an entire phase codebase in one shot
- [ ] Do **not** create all 25 phase folders up front — start with Phase 1 only
- [ ] Treat Spring Cloud Gateway, Eureka, Resilience4j, Kafka, Redis, OpenTelemetry, Prometheus/Grafana, Docker, and Kubernetes as **architectural tools**, not disconnected features
- [ ] Learn Eureka for discovery concepts, then compare it to Kubernetes Service discovery so it is not over-taught

```
Local / VM deployment  →  Eureka
Kubernetes deployment  →  Kubernetes Service Discovery
```

---

## Master Progress Tracker

Optional Trello mirror: [SB-MS board](https://trello.com/b/Cjb5ESUA/sb-ms-spring-boot-microservices) — card URLs in [`docs/trello.md`](./docs/trello.md). GitHub: https://github.com/chandrakanthbalaraman/spring-boot-microservices

| # | Phase | Folder | Status |
|---|-------|--------|--------|
| 01 | Microservices Architecture Fundamentals | `phase-01-microservices-basics/` | DONE (tests N/A) |
| 02 | Inter-Service Communication | `phase-02-service-communication/` | [ ] |
| 03 | Service Discovery | `phase-03-service-discovery/` | [ ] |
| 04 | Load Balancing | `phase-04-load-balancing/` | [ ] |
| 05 | API Gateway | `phase-05-api-gateway/` | [ ] |
| 06 | Configuration Management | `phase-06-configuration/` | [ ] |
| 07 | Resilience Engineering | `phase-07-resilience/` | [ ] |
| 08 | Database Architecture | `phase-08-database/` | [ ] |
| 09 | Distributed Transactions / Saga | `phase-09-saga/` | [ ] |
| 10 | Kafka & Event-Driven Architecture | `phase-10-kafka/` | [ ] |
| 11 | Distributed Cache (Redis) | `phase-11-redis/` | [ ] |
| 12 | Security | `phase-12-security/` | [ ] |
| 13 | WebSockets & Real-Time Systems | `phase-13-websocket/` | [ ] |
| 14 | Distributed Tracing | `phase-14-tracing/` | [ ] |
| 15 | Centralized Logging | `phase-15-logging/` | [ ] |
| 16 | Observability | `phase-16-observability/` | [ ] |
| 17 | Docker & Containerization | `phase-17-docker/` | [ ] |
| 18 | Kubernetes | `phase-18-kubernetes/` | [ ] |
| 19 | Kubernetes Production Architecture | `phase-19-kubernetes-production/` | [ ] |
| 20 | CI/CD | `phase-20-cicd/` | [ ] |
| 21 | Testing Microservices | `phase-21-testing/` | [ ] |
| 22 | Performance & Scalability | `phase-22-performance/` | [ ] |
| 23 | Advanced Distributed Systems | `phase-23-distributed-systems/` | [ ] |
| 24 | Production Architecture Patterns | `phase-24-production-patterns/` | [ ] |
| 25 | Capstone: Enterprise Commerce Platform | `capstone/` | [ ] |

**Recommended sequence**

```
START → 01 Architecture → 02 REST/Feign → 03 Discovery → 04 Load Balancing
     → 05 Gateway → 06 Config → 07 Resilience → 08 Database → 09 Saga
     → 10 Kafka → 11 Redis → 12 Security → 13 WebSockets → 14 Tracing
     → 15 Logging → 16 Observability → 17 Docker → 18 Kubernetes
     → 19 K8s Production → 20 CI/CD → 21 Testing → 22 Performance
     → 23 Distributed Systems → 24 Patterns → 25 CAPSTONE
     → PRODUCTION-READY MICROSERVICES
```

---

## Repository Structure

One repo. Each phase is a snapshot/evolution of the previous phase. Independently runnable. Reusable shared modules and infrastructure. Portfolio-ready on GitHub.

```
spring-boot-microservices/
│
├── README.md
├── .gitignore
├── pom.xml
│
├── docs/
│   ├── architecture/
│   ├── decisions/
│   └── notes/
│
├── shared/
│   ├── common-model/
│   ├── common-exception/
│   └── common-util/
│
├── infrastructure/
│   ├── docker/
│   ├── postgres/
│   ├── redis/
│   ├── kafka/
│   └── observability/
│
├── phase-01-microservices-basics/
├── phase-02-service-communication/
├── phase-03-service-discovery/
├── phase-04-load-balancing/
├── phase-05-api-gateway/
├── phase-06-configuration/
├── phase-07-resilience/
├── phase-08-database/
├── phase-09-saga/
├── phase-10-kafka/
├── phase-11-redis/
├── phase-12-security/
├── phase-13-websocket/
├── phase-14-tracing/
├── phase-15-logging/
├── phase-16-observability/
├── phase-17-docker/
├── phase-18-kubernetes/
├── phase-19-kubernetes-production/
├── phase-20-cicd/
├── phase-21-testing/
├── phase-22-performance/
├── phase-23-distributed-systems/
├── phase-24-production-patterns/
│
└── capstone/
    ├── README.md
    ├── api-gateway/
    ├── user-service/
    ├── product-service/
    ├── inventory-service/
    ├── order-service/
    ├── payment-service/
    ├── shipping-service/
    └── notification-service/
```

**Evolution (conceptual)** — each phase adds one capability; do not blindly duplicate forever. Capstone consolidates reusable pieces into a clean production architecture.

```
phase-01  → add communication      → phase-02
          → add discovery          → phase-03
          → add load balancing     → phase-04
          → add gateway            → phase-05
          → …                      → capstone
```

### Git Strategy

One repository + tags for milestones. Work on `main` for learning.

- [x] Initialize one repo (`spring-boot-microservices`)
- [ ] Commit per phase increment: `phase 01: implement service-to-service communication`
- [ ] Tag milestones: `phase-01-complete`, `phase-02-complete`, … `capstone`
- [x] Do **not** maintain 25 separate repositories

```
main
 │
 ├── phase-01-complete
 ├── phase-02-complete
 ├── phase-03-complete
 ├── phase-04-complete
 │
 └── capstone
```

### Maven Structure

Root parent exists eventually, but **each phase has its own Maven parent** so you can run folder-by-folder.

```
phase-01-microservices-basics/
├── pom.xml
├── product-service/pom.xml
├── inventory-service/pom.xml
└── order-service/pom.xml
```

- [x] Enter one service and run: `cd phase-01-microservices-basics/product-service && ./mvnw spring-boot:run`
- [x] Repeat for inventory-service and order-service
- [x] Do **not** force one giant Maven reactor on day one

### Phase README Template

Every phase folder gets a `README.md` with:

- [x] Objective
- [x] Architecture
- [x] Services
- [x] Prerequisites
- [x] Project Structure
- [x] How to Run
- [x] API Endpoints
- [x] Request Flow
- [x] Database
- [x] Testing (section present; JUnit **N/A (intentional)**)
- [x] Failure Scenarios
- [x] What We Learned
- [x] Exercises
- [x] Production Improvements
- [x] Next Phase

### Package Structure (every service)

**Start (package-by-layer):**

```
src/main/java/com/example/product/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
├── exception/
├── config/
└── ProductServiceApplication.java
```

**Later (package-by-feature for larger services):**

```
product-service/src/main/java/com/example/product/
├── product/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── domain/
│   └── dto/
├── shared/
│   ├── exception/
│   └── config/
└── ProductServiceApplication.java
```

- [ ] Use the same layout in every service
- [ ] Discuss package-by-feature vs package-by-layer when services grow
- [ ] Prefer package-by-feature for larger production services

### Local Infrastructure

Keep infrastructure reusable. Do not install databases directly on the Mac.

```
infrastructure/
└── docker/
    ├── docker-compose.yml
    └── README.md
```

- [ ] Start with PostgreSQL: `docker compose up -d`
- [ ] Later add Redis, Kafka, Prometheus, Grafana, Jaeger/Tempo

### Environment Configuration

- [ ] Never commit secrets
- [ ] Use `application.yml`, `application-local.yml`, `application-docker.yml`, `application-test.yml`
- [ ] Provide `.env.example` only

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=product_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

### Documentation (from Day 1)

```
docs/
├── architecture/     phase-01.md, phase-02.md, …
├── decisions/        ADR-001-service-boundaries.md, ADR-002-rest-vs-events.md, …
└── notes/            microservices.md, resilience.md, kafka.md, kubernetes.md
```

- [ ] Architecture notes per phase
- [ ] ADRs for major design choices
- [ ] Topic notes for interview / system-design prep

---

## Bootstrap (before Phase 1 code)

Do **not** create all 25 phases yet. Start with only:

```
spring-boot-microservices/
├── README.md
├── docs/
├── infrastructure/
└── phase-01-microservices-basics/
    ├── README.md
    ├── pom.xml
    ├── product-service/
    ├── inventory-service/
    └── order-service/
```

Build incrementally:

```
Repository → Parent Maven → Product Service → Database → Product APIs
         → Inventory Service → Inventory APIs → Order Service
         → REST communication → Integration testing → Docker
         → Phase 1 complete
```

- [x] Create root README, `.gitignore`, docs, infrastructure
- [x] Create Phase 1 parent Maven + three services
- [x] Dockerize PostgreSQL
- [x] Write, run, break, fix, and understand before moving on

---

# Phase 01 — Microservices Architecture Fundamentals

**Goal:** Understand why microservices exist and build the first multi-service application.

**Folder:** `phase-01-microservices-basics/`

**Starting lesson:** Build `product-service` + `inventory-service` + `order-service` with project structure, Maven, PostgreSQL, REST APIs, service-to-service communication, and the first real order flow.

```
POST /orders
        │
        ▼
  order-service
        │
        ├──────► product-service
        │
        └──────► inventory-service
```

```
                Client
                   │
                   ▼
             Order Service
              /          \
             /            \
            ▼              ▼
     Product Service   Inventory Service
```

### Learn

- [x] Monolith vs modular monolith vs microservices
- [x] Service boundaries
- [x] Bounded contexts
- [x] Database-per-service
- [x] Service-to-service communication
- [x] REST communication
- [x] DTOs
- [x] API contracts
- [x] Synchronous vs asynchronous communication (sync REST in Phase 1; Kafka is Phase 10)
- [x] Inter-service dependencies
- [x] Failure propagation (compensation + RestClientException)
- [x] Distributed transactions (problem awareness — manual release on failure)
- [x] Eventual consistency (problem awareness)

### Build

- [x] `product-service`
- [x] `inventory-service` (APIs + Flyway + Docker DB + advice; path `/api/v1/inventories`)
- [x] `order-service` (Flyway + `order_db` + entities; POST `/api/v1/orders` + GET `/{id}` + RestClient)

### Product Service

Responsible for: `id`, `name`, `price`, `sku`

- [x] `GET /api/products`
- [x] `GET /api/products/{id}`
- [x] `POST /api/products`
- [x] `PUT /api/products/{id}` (implemented as `PUT /api/v1/products/{id}`)
- [x] `DELETE /api/products/{id}` (implemented as `DELETE /api/v1/products/{id}`)

### Inventory Service

Responsible for: `id`, `productId`, `quantity`, `reservedQuantity`

- [x] `GET /api/inventory/{productId}` (implemented as `GET /api/v1/inventories/{productId}`)

### Order Service

Responsible for: `id`, `customerId`, `status`, `totalAmount`

- [x] `POST /api/orders` (implemented as `POST /api/v1/orders`)
- [x] Validate product via Product Service (sync REST)
- [x] Check availability via Inventory Service (sync REST reserve)

### Hands-on

- [x] Independent Spring Boot apps in `spring-microservices/` (or `phase-01-microservices-basics/`)
- [x] Service layer, repository layer, DTOs, validation, error handling
- [x] PostgreSQL + database-per-service (`product_db` :5433, `inventory_db` :5434, `order_db` :5435)
- [x] Dockerized PostgreSQL
- [x] Run multiple services locally
- [ ] Basic integration testing — **N/A (intentional):** skipped; manual Postman + break-it in phase README
- [x] Git repository organization

### Break it

```
inventory-service DOWN → order-service → timeout → bad user experience
```

- [x] Stop `inventory-service` (also product-service DOWN — same RestClientException path)
- [x] Observe what happens to `order-service`
- [x] Use this failure as motivation for later phases

### Communication evolution path

| Phase | Communication / capability |
|-------|----------------------------|
| 1 | REST |
| 2 | OpenFeign |
| 3 | Service Discovery |
| 4 | Load Balancing |
| 5 | Gateway |
| 7 | Resilience |
| 10 | Kafka |

### Phase 1 complete when you can implement

- [x] Microservice boundaries
- [x] 3 independent Spring Boot applications
- [x] REST APIs, DTOs, service layer, repository layer
- [x] PostgreSQL and database-per-service
- [x] Service-to-service REST calls
- [x] Error handling and request validation
- [ ] Basic integration testing — **N/A (intentional):** skipped
- [x] Dockerized PostgreSQL
- [x] Running multiple services locally
- [x] Git repository organization

**Next:** Reuse this exact application in Phase 2. Do not start over.

---

# Phase 02 — Inter-Service Communication

**Folder:** `phase-02-service-communication/`

Remove naive HTTP wiring. Standardize clients, timeouts, errors, and contracts.

```
Order Service ── REST ──► Inventory Service
Order Service ── Feign ─► Inventory Service
```

```java
@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/api/inventory/{sku}")
    InventoryResponse getInventory(@PathVariable String sku);
}
```

### Learn

- [ ] RestClient
- [ ] WebClient
- [ ] OpenFeign
- [ ] HTTP timeouts
- [ ] Connection pooling
- [ ] Serialization / deserialization
- [ ] Error handling
- [ ] API versioning
- [ ] Idempotency
- [ ] Synchronous communication
- [ ] Asynchronous communication (awareness; Kafka comes in Phase 10)

### Hands-on

- [ ] Replace ad-hoc REST calls with RestClient and/or WebClient
- [ ] Implement OpenFeign `InventoryClient`
- [ ] Configure timeouts and connection pooling
- [ ] Handle client errors without leaking internals
- [ ] Version APIs
- [ ] Make mutating calls idempotent where needed
- [ ] Move beyond simple REST calls (prepare for discovery)

**Next:** Remove hard-coded service URLs.

---

# Phase 03 — Service Discovery

**Folder:** `phase-03-service-discovery/`

Replace `http://localhost:8082` with the logical name `inventory-service`.

```
                  ┌──────────────┐
                  │   Eureka     │
                  │   Registry   │
                  └──────┬───────┘
                         │
          ┌──────────────┼──────────────┐
          ▼              ▼              ▼
     order-service  inventory-service product-service
```

```
Order → inventory-service → instance 1 / instance 2 / instance 3
```

### Learn

- [ ] Service registry
- [ ] Service discovery
- [ ] Client-side discovery
- [ ] Server-side discovery
- [ ] Service registration
- [ ] Health checks
- [ ] Instance metadata
- [ ] Load-balanced service calls
- [ ] Spring Cloud LoadBalancer
- [ ] Eureka
- [ ] Alternatives and when discovery is unnecessary in Kubernetes

### Hands-on

- [ ] Add `discovery-server`
- [ ] Register product, inventory, and order services
- [ ] Call services by name, not host/port
- [ ] Confirm health and instance metadata in the registry
- [ ] Compare Eureka vs Kubernetes DNS/Services (do not over-invest in Eureka)

**Next:** Scale instances and distribute traffic.

---

# Phase 04 — Load Balancing

**Folder:** `phase-04-load-balancing/`

Scale services. Keep them stateless.

```
                  ┌──────────────┐
                  │Order Service │
                  └──────┬───────┘
                         │
                  Load Balancer
                  ┌──────┼──────┐
                  ▼      ▼      ▼
                 INV1   INV2   INV3
```

### Learn

- [ ] Client-side load balancing
- [ ] Server-side load balancing
- [ ] Spring Cloud LoadBalancer
- [ ] Round robin
- [ ] Weighted strategies
- [ ] Health-aware routing
- [ ] Sticky sessions (and why to avoid them)
- [ ] Horizontal scaling
- [ ] Stateless services

### Hands-on

- [ ] Run `inventory-service` on `:8081`, `:8082`, `:8083`
- [ ] Demonstrate traffic distribution
- [ ] Prove unhealthy instances are skipped
- [ ] Confirm services stay stateless under scale

**Next:** Introduce a single real entry point.

---

# Phase 05 — API Gateway

**Folder:** `phase-05-api-gateway/`

```
                   Client
                      │
                      ▼
              ┌──────────────┐
              │ API Gateway   │
              └──────┬───────┘
                     │
        ┌────────────┼─────────────┐
        ▼            ▼             ▼
      users        orders       products
```

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
```

### Learn

- [ ] Spring Cloud Gateway
- [ ] Routing
- [ ] Predicates
- [ ] Filters
- [ ] Authentication (gateway-level awareness)
- [ ] Authorization (gateway-level awareness)
- [ ] Rate limiting
- [ ] Request transformation
- [ ] CORS
- [ ] API versioning
- [ ] Correlation IDs
- [ ] Gateway observability

### Hands-on

- [ ] Add API Gateway as the only client entry point
- [ ] Route `/api/orders/**`, products, inventory via `lb://`
- [ ] Add correlation IDs
- [ ] Configure CORS and rate limiting
- [ ] Observe gateway metrics/logs

**Next:** Centralize configuration.

---

# Phase 06 — Configuration Management

**Folder:** `phase-06-configuration/`

```
              Config Server
                  │
        ┌─────────┼─────────┐
        ▼         ▼         ▼
      Order    Product   Inventory
```

### Learn

- [ ] Externalized configuration
- [ ] Spring profiles
- [ ] Environment variables
- [ ] Secrets (do not commit them)
- [ ] Spring Cloud Config
- [ ] Configuration refresh
- [ ] Configuration hierarchy
- [ ] Twelve-factor application principles

### Hands-on

- [ ] Externalize all environment-specific values
- [ ] Add Config Server (or equivalent hierarchy)
- [ ] Demonstrate refresh
- [ ] Plan the later move of secrets to Kubernetes-native mechanisms

**Next:** Intentionally create failures and survive them.

---

# Phase 07 — Resilience Engineering

**Folder:** `phase-07-resilience/`

One of the most important phases. Create failures on purpose.

```
Order → Inventory → X DOWN
```

```java
@CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
public InventoryResponse checkInventory(String sku) {
    return inventoryClient.getInventory(sku);
}
```

### Learn / implement (Resilience4j)

- [ ] Timeout
- [ ] Retry
- [ ] Exponential backoff
- [ ] Circuit breaker
- [ ] Fallback
- [ ] Bulkhead
- [ ] Rate limiter
- [ ] Concurrency control
- [ ] Graceful degradation
- [ ] Fail-fast
- [ ] Idempotency
- [ ] Dead-letter handling
- [ ] **When NOT to retry** (as important as retry itself)

### Hands-on loop

- [ ] Build the call
- [ ] Shut down inventory
- [ ] Observe failure
- [ ] Add timeout → retry → circuit breaker
- [ ] Measure behavior
- [ ] Add fallback
- [ ] Test recovery
- [ ] Integrate into capstone later

**Next:** Make data genuinely distributed.

---

# Phase 08 — Database Architecture

**Folder:** `phase-08-database/`

No direct database sharing between services.

```
Product Service ──► product_db
Order Service ────► order_db
Inventory Service ─► inventory_db
```

### Learn

- [ ] Database-per-service
- [ ] PostgreSQL
- [ ] Flyway
- [ ] Schema migration
- [ ] Indexes
- [ ] Transactions
- [ ] Optimistic locking
- [ ] Pessimistic locking
- [ ] Connection pools
- [ ] Transaction isolation
- [ ] N+1 problems
- [ ] Pagination
- [ ] Query optimization
- [ ] Read/write patterns

### Hands-on

- [ ] One database per service
- [ ] Flyway migrations in each service
- [ ] Connection pooling and isolation settings
- [ ] Fix N+1 and add pagination
- [ ] Prove another service cannot query a foreign DB

**Next:** Coordinate work across those databases.

---

# Phase 09 — Distributed Transactions

**Folder:** `phase-09-saga/`

This is where microservices become genuinely interesting. Major capstone building block.

```
Create Order
    ├── Reserve Inventory
    ├── Process Payment
    └── Confirm Order
```

Failure case: **Payment SUCCESS + Inventory FAILED**.

```
              Order
                │
                ▼
          Saga Orchestrator
          ┌─────┼─────┐
          ▼     ▼     ▼
       Inventory Payment Shipping
```

### Learn

- [ ] Distributed transaction problem
- [ ] 2PC concept (and why we usually avoid it)
- [ ] Saga pattern
- [ ] Orchestration
- [ ] Choreography
- [ ] Compensating transactions
- [ ] Eventual consistency
- [ ] Transactional outbox

### Hands-on

- [ ] Implement an order saga (orchestrator first)
- [ ] Add compensating actions
- [ ] Handle payment success / inventory failure
- [ ] Introduce transactional outbox
- [ ] Carry saga into the capstone

**Next:** Move from sync-only toward events.

---

# Phase 10 — Kafka & Event-Driven Architecture

**Folder:** `phase-10-kafka/`

Transition from synchronous microservices toward event-driven systems.

```
Order Service ── OrderCreated ──► Kafka
                                    ├────────────► Inventory
                                    ├────────────► Payment
                                    └────────────► Notification
```

### Learn

- [ ] Kafka fundamentals
- [ ] Producer
- [ ] Consumer
- [ ] Topic
- [ ] Partition
- [ ] Offset
- [ ] Consumer group
- [ ] Ordering
- [ ] Retries
- [ ] Dead-letter topics
- [ ] Schema evolution
- [ ] Idempotent consumers
- [ ] Event-driven architecture
- [ ] Event choreography

### Hands-on

- [ ] Publish `OrderCreated`
- [ ] Consume in inventory, payment, notification
- [ ] Consumer groups, retries, DLTs
- [ ] Idempotent consumers
- [ ] Schema evolution strategy

**Next:** Add a distributed cache — and learn when it makes things worse.

---

# Phase 11 — Distributed Cache

**Folder:** `phase-11-redis/`

```
                 ┌─────────┐
                 │  Redis  │
                 └────┬────┘
                      │
Product ──────────────┤
Order ────────────────┤
                      │
                 PostgreSQL
```

```java
@Cacheable(value = "products", key = "#id")
public Product getProduct(Long id) {
    return repository.findById(id).orElseThrow();
}
```

### Learn

- [ ] Caching strategies
- [ ] Cache-aside
- [ ] Read-through
- [ ] Write-through
- [ ] Write-behind
- [ ] TTL
- [ ] Cache invalidation
- [ ] Distributed locks
- [ ] Cache stampede
- [ ] Hot keys
- [ ] Redis data structures
- [ ] Why incorrect caching can make a distributed system **worse**

### Hands-on

- [ ] Add Redis
- [ ] Cache product reads with `@Cacheable`
- [ ] Invalidate on writes
- [ ] Demonstrate stampede / hot-key problems
- [ ] Use a distributed lock where it is actually justified

**Next:** Production-style security.

---

# Phase 12 — Security

**Folder:** `phase-12-security/`

Integrate an identity provider. Do not invent your own authentication system.

```
                 Identity Provider
                       │
                     JWT
                       │
                       ▼
Client ───────► API Gateway
                       │
                       ▼
                 Microservices
```

### Learn

- [ ] Spring Security
- [ ] OAuth2
- [ ] JWT
- [ ] Access tokens
- [ ] Refresh tokens
- [ ] Authentication vs authorization
- [ ] Roles
- [ ] Permissions
- [ ] RBAC
- [ ] Method security
- [ ] Gateway authentication
- [ ] Service-to-service authentication
- [ ] CORS
- [ ] CSRF
- [ ] Secrets management
- [ ] Token validation
- [ ] OAuth2 Resource Server

### Hands-on

- [ ] JWT validation at the gateway
- [ ] Resource server on services
- [ ] RBAC / method security
- [ ] Service-to-service auth
- [ ] Secrets out of Git

**Next:** Real-time order tracking.

---

# Phase 13 — WebSockets & Real-Time Systems

**Folder:** `phase-13-websocket/`

Dedicated phase (explicitly requested).

```
Customer ── WebSocket ──► Gateway ──► Order Service ──► Kafka
                                                    ──► WebSocket notification
                                                    ──► Customer UI
```

### Learn

- [ ] WebSocket lifecycle
- [ ] STOMP
- [ ] Message brokers
- [ ] Subscriptions
- [ ] Broadcasting
- [ ] Authentication
- [ ] Scaling WebSocket servers
- [ ] Redis/Kafka-backed messaging
- [ ] Reconnect strategy
- [ ] Connection management

### Hands-on

- [ ] Real-time order tracking
- [ ] Authenticated WebSocket sessions
- [ ] Broadcast order status from Kafka events
- [ ] Reconnect and connection-management strategy
- [ ] Plan horizontal scale (Redis/Kafka-backed)

**Next:** Answer why one request took 4.8 seconds.

---

# Phase 14 — Distributed Tracing

**Folder:** `phase-14-tracing/`

One request crossing many services:

```
Request → Gateway → Order → Inventory
                         → Payment
                         → Notification
```

Goal: answer **"Why did this particular request take 4.8 seconds?"** instead of grepping app logs.

### Learn

- [ ] Trace
- [ ] Span
- [ ] Trace ID
- [ ] Span ID
- [ ] Context propagation
- [ ] OpenTelemetry
- [ ] Micrometer Tracing
- [ ] Tempo / Jaeger
- [ ] Sampling
- [ ] Baggage

### Hands-on

- [ ] Propagate trace context across gateway and all services
- [ ] Export to Tempo/Jaeger
- [ ] Inspect a slow request as a trace, not a pile of logs

**Next:** Structured, searchable logs tied to those traces.

---

# Phase 15 — Centralized Logging

**Folder:** `phase-15-logging/`

```
Microservices → Structured JSON logs → Log aggregation → Search / analysis
```

Example fields: `traceId=abc123`, `spanId=xyz789`, `service=order-service`, `operation=createOrder`, `duration=128ms`

### Learn

- [ ] Structured logging
- [ ] Correlation IDs
- [ ] Trace IDs
- [ ] MDC
- [ ] Log levels
- [ ] Centralized logging
- [ ] ELK / OpenSearch concepts
- [ ] Sensitive-data masking
- [ ] Production logging practices

### Hands-on

- [ ] JSON logs in every service
- [ ] MDC with trace/span/correlation IDs
- [ ] Mask secrets and PII
- [ ] Aggregate and search across services

**Next:** Unite logs, metrics, and traces.

---

# Phase 16 — Observability

**Folder:** `phase-16-observability/`

Three pillars:

```
        Observability
             │
     ┌───────┼────────┐
     ▼       ▼        ▼
   Logs    Metrics   Traces
```

### Learn

- [ ] Micrometer
- [ ] Prometheus
- [ ] Grafana
- [ ] JVM metrics
- [ ] HTTP metrics
- [ ] Business metrics
- [ ] Latency
- [ ] Throughput
- [ ] Error rate
- [ ] Saturation
- [ ] SLIs
- [ ] SLOs
- [ ] Alerting

### Dashboard — Order Service

- [ ] Requests/sec
- [ ] Error rate
- [ ] P95 latency
- [ ] P99 latency
- [ ] JVM memory
- [ ] CPU
- [ ] DB connections
- [ ] Kafka lag
- [ ] Circuit breaker state

**Next:** Containerize the whole local environment.

---

# Phase 17 — Docker & Containerization

**Folder:** `phase-17-docker/`

```
                    Docker
                      │
       ┌──────────────┼──────────────┐
       ▼              ▼              ▼
    Gateway          Order        Inventory
       │              │              │
       └──────────────┼──────────────┘
                      ▼
                   Network
```

Local compose stack:

```
docker-compose.yml
├── gateway
├── order
├── product
├── inventory
├── postgres
├── redis
├── kafka
├── prometheus
├── grafana
└── tracing
```

### Learn

- [ ] Dockerfile
- [ ] Multi-stage builds
- [ ] JVM containerization
- [ ] Image optimization
- [ ] Docker Compose
- [ ] Health checks
- [ ] Environment configuration
- [ ] Container networking
- [ ] Persistent volumes

### Hands-on

- [ ] Multi-stage Dockerfile per service
- [ ] Compose the full local environment
- [ ] Health checks and volumes
- [ ] One command to run the stack

**Next:** Move from Docker Compose to Kubernetes.

---

# Phase 18 — Kubernetes

**Folder:** `phase-18-kubernetes/`

Kubernetes provides service discovery and load balancing. Understand where Eureka still helps and where it is redundant.

```
                 Kubernetes Cluster
                       │
                 ┌─────▼─────┐
                 │ Ingress   │
                 └─────┬─────┘
                       │
                ┌──────▼──────┐
                │   Gateway   │
                └──────┬──────┘
                       │
        ┌──────────────┼──────────────┐
        ▼              ▼              ▼
     Order Pods    Product Pods   Inventory Pods
```

### Learn

- [ ] Pod
- [ ] Deployment
- [ ] Service
- [ ] ConfigMap
- [ ] Secret
- [ ] Namespace
- [ ] Ingress
- [ ] Readiness probe
- [ ] Liveness probe
- [ ] Startup probe
- [ ] HPA
- [ ] Resource requests
- [ ] Resource limits
- [ ] Rolling deployment
- [ ] Service discovery (K8s)
- [ ] Persistent volumes

### Hands-on

- [ ] Deploy gateway + core services
- [ ] ConfigMaps, Secrets, probes, requests/limits
- [ ] Ingress to the gateway
- [ ] Contrast Eureka vs Kubernetes Services

**Next:** Production Kubernetes practices.

---

# Phase 19 — Kubernetes Production Architecture

**Folder:** `phase-19-kubernetes-production/`

Go beyond basic Kubernetes.

### Learn

- [ ] HPA
- [ ] Cluster autoscaling concepts
- [ ] Rolling updates
- [ ] Blue/green deployment
- [ ] Canary deployment
- [ ] PodDisruptionBudget
- [ ] Affinity / anti-affinity
- [ ] Resource management
- [ ] Secrets
- [ ] Network policies
- [ ] Ingress
- [ ] TLS
- [ ] Observability in Kubernetes
- [ ] Graceful shutdown
- [ ] Zero-downtime deployment

### Hands-on

- [ ] Zero-downtime rolling update
- [ ] Compare blue/green vs canary
- [ ] PDB + anti-affinity
- [ ] Network policies and TLS
- [ ] Graceful shutdown (no dropped in-flight requests)

**Next:** Automate build, test, and deploy.

---

# Phase 20 — CI/CD

**Folder:** `phase-20-cicd/`

```
Git Push → GitHub Actions → Build → Unit Tests → Integration Tests
         → Security Scan → Docker Build → Push Image
         → Container Registry → Kubernetes → Production
```

### Learn

- [ ] CI
- [ ] CD
- [ ] Docker image tagging
- [ ] Artifact management
- [ ] Automated testing
- [ ] Deployment strategies
- [ ] Environment promotion
- [ ] Rollback
- [ ] Secrets in CI/CD

### Hands-on

- [ ] GitHub Actions pipeline
- [ ] Tests + security scan gate the image
- [ ] Tag and push images
- [ ] Promote environments with rollback

**Next:** Testing that matches production reality.

---

# Phase 21 — Testing Microservices

**Folder:** `phase-21-testing/`

Production systems require more than unit tests.

```
Unit Tests → Integration Tests → API Tests → Contract Tests
          → Testcontainers → End-to-End Tests → Load Tests
```

### Learn / use

- [ ] JUnit
- [ ] Mockito
- [ ] Spring Boot Test
- [ ] Testcontainers
- [ ] WireMock
- [ ] Contract testing
- [ ] Kafka testing
- [ ] PostgreSQL integration tests
- [ ] API tests
- [ ] End-to-end tests
- [ ] Load tests

### Hands-on

- [ ] Slice/unit tests per layer
- [ ] Testcontainers for Postgres/Kafka
- [ ] WireMock for downstream HTTP
- [ ] Consumer/provider contracts
- [ ] A thin E2E path through the gateway

**Next:** Push the system until something breaks.

---

# Phase 22 — Performance & Scalability

**Folder:** `phase-22-performance/`

Deliberately push the system. Identify what breaks first.

```
10 rps → 100 rps → 1,000 rps → 10,000 rps
```

### Learn

- [ ] Throughput
- [ ] Latency
- [ ] Concurrency
- [ ] Connection pools
- [ ] Thread pools
- [ ] JVM tuning concepts
- [ ] Database bottlenecks
- [ ] Redis bottlenecks
- [ ] Kafka throughput
- [ ] Backpressure
- [ ] Horizontal scaling
- [ ] Load testing
- [ ] Capacity planning

### Hands-on

- [ ] Load-test the order flow
- [ ] Record what saturates first (DB, pool, Kafka, CPU, Redis)
- [ ] Tune pools / scale horizontally
- [ ] Re-test and document capacity

**Next:** Senior/staff-level distributed systems thinking.

---

# Phase 23 — Advanced Distributed Systems

**Folder:** `phase-23-distributed-systems/`

Move toward senior/staff-level architecture.

### Topics

- [ ] CAP theorem
- [ ] Consistency models
- [ ] Availability
- [ ] Partition tolerance
- [ ] Eventual consistency
- [ ] Quorum
- [ ] Leader election
- [ ] Distributed locks
- [ ] Idempotency
- [ ] Exactly-once vs at-least-once
- [ ] Message ordering
- [ ] Duplicate messages
- [ ] Poison messages
- [ ] Backpressure
- [ ] Bulkheads
- [ ] Graceful degradation
- [ ] Distributed rate limiting
- [ ] Clock problems
- [ ] Retries and retry storms

### Hands-on

- [ ] Map each topic to a failure you already caused in earlier phases
- [ ] Document trade-offs in ADRs (not just definitions)

**Next:** Name the patterns you already implemented — and when *not* to use them.

---

# Phase 24 — Production Architecture Patterns

**Folder:** `phase-24-production-patterns/`

Implement and compare. Not every pattern belongs in every application.

**The skill:** *Why would I choose this pattern here?* — not memorizing names.

### Patterns

- [ ] API Gateway
- [ ] Service Discovery
- [ ] Database per Service
- [ ] Saga
- [ ] Transactional Outbox
- [ ] CQRS
- [ ] Event Sourcing
- [ ] Cache Aside
- [ ] Circuit Breaker
- [ ] Bulkhead
- [ ] Retry
- [ ] Rate Limiter
- [ ] Strangler Fig
- [ ] Anti-Corruption Layer
- [ ] Sidecar
- [ ] Backend-for-Frontend

### Hands-on

- [ ] For each pattern: when to use, when not to, and where it already exists in this repo
- [ ] Add only patterns that earn their complexity
- [ ] Write ADRs for CQRS / Event Sourcing if you introduce them

**Next:** Consolidate into the capstone.

---

# Phase 25 — Capstone: Enterprise Commerce Platform

**Folder:** `capstone/`

One substantial production-style application. Evolve the same project through the roadmap; then consolidate reusable pieces here.

```
                         ┌─────────────┐
                         │   React UI  │
                         └──────┬──────┘
                                │
                         ┌──────▼──────┐
                         │API Gateway  │
                         └──────┬──────┘
                                │
        ┌───────────────────────┼────────────────────────┐
        │                       │                        │
        ▼                       ▼                        ▼
   User Service          Product Service           Order Service
                                                       │
                                                     Kafka
                                                       │
                                                Inventory / Payment /
                                                Shipping / Notification
        │
        ▼
 Identity Provider

Every service: PostgreSQL · Metrics · Logs · Distributed Tracing
```

### Capstone microservices

- [ ] `api-gateway`
- [ ] `user-service`
- [ ] `product-service`
- [ ] `inventory-service`
- [ ] `order-service`
- [ ] `payment-service`
- [ ] `shipping-service`
- [ ] `notification-service`

### Capstone infrastructure

- [ ] PostgreSQL (per service)
- [ ] Redis
- [ ] Kafka
- [ ] Identity Provider
- [ ] OpenTelemetry
- [ ] Prometheus
- [ ] Grafana
- [ ] Docker
- [ ] Kubernetes

### Cross-cutting (every service)

- [ ] Own PostgreSQL
- [ ] Metrics
- [ ] Logs
- [ ] Distributed tracing
- [ ] Health/readiness
- [ ] Security
- [ ] Resilience
- [ ] Tests

### Capstone flows

- [ ] Order → Payment
- [ ] Order → Shipping
- [ ] Order → Notification
- [ ] Real-time tracking (WebSocket)
- [ ] Saga + outbox + events
- [ ] Gateway as sole entry
- [ ] React UI (optional client)

### Capstone complete when you can

- [ ] Design the platform
- [ ] Implement the services
- [ ] Test at unit/integration/contract/E2E levels
- [ ] Containerize and run locally
- [ ] Deploy to Kubernetes
- [ ] Observe (logs, metrics, traces)
- [ ] Secure (IdP, JWT, RBAC)
- [ ] Troubleshoot a cross-service failure using traces + logs + metrics

---

## Quick Reference — First Order Flow (Phase 1)

Synchronous REST for now; later phases replace or add mechanisms.

```
POST /api/orders
       │
       ▼
Order Service
       │
       ├──────► Product Service  → validate product
       │
       └──────► Inventory Service → check availability
```

---

## Status Legend

Mark items in this file as you complete them:

- `[ ]` not started
- `[x]` done
- Optionally add a date after the item: `[x] Flyway migrations — 2026-08-25`
