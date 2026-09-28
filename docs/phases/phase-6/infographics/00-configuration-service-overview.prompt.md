# Prompt — Configuration Management Overview

Create a detailed 16:9 landscape educational architecture infographic titled “CONFIGURATION MANAGEMENT” with subtitle “One build. Many environments. Secrets stay out of Git.” Match the supplied SB-MS Phase 05 overview: dark navy header/footer, white background, cyan panel outlines, numbered blue circles, pastel blue/green/pink/yellow/lavender cards, clean system-design icons, bold navy typography, dense but readable. Footer: “SB-MS · PHASE 06 · CONFIGURATION MANAGEMENT”. No progress badges, no competitor handles, no invented implementation status.

Use 10 numbered panels:
1. “What is Externalized Configuration?” — settings live outside the application artifact; same JAR/container moves across environments.
2. “Current SB-MS Architecture” — Config Server `:8888` using native filesystem → `infrastructure/config-repo/`; `product-service` imports `configserver:http://localhost:8888`; Product DB credentials come from environment variables.
3. “Config Repository Hierarchy” — `application.yml` shared defaults; `product-service.yml` service defaults; `product-service-dev.yml` dev overrides; `product-service-prod.yml` prod overrides.
4. “Resolution Order” — environment variable → profile-specific remote → service-specific remote → shared remote → packaged local default. Show `SERVER_PORT=8281` winning over prod remote `8183`.
5. “Implementation Flow” — Config Client starts → requests `{application}/{profile}` → Config Server merges property sources → client environment created → service starts or fails fast.
6. “Production Non-Secret Backends” — Git-backed Config Server: version history, review, rollback; protect repository and Config Server. ConfigMap later in Kubernetes for non-confidential runtime settings.
7. “Secret Backends” — local `.env` for development only; Vault for multi-cloud and dynamic credentials; AWS Secrets Manager for AWS-managed rotation; Kubernetes Secret is a delivery object, not a complete security strategy.
8. “Where Should It Live?” — public build constant→code; non-secret shared setting→Config Git; environment deployment value→env/ConfigMap; credential/key/token→Vault or cloud secret manager; GitOps secret→Sealed Secret only when the operating model fits.
9. “Kubernetes Delivery Patterns” — `secretKeyRef` / `envFrom`; read-only Secret volume; External Secrets Operator sync; Secrets Store CSI mount; workload identity. Warning: Base64 is NOT encryption.
10. “Operations & Trade-offs” — refresh versus restart, secret rotation, fail-fast, cached Git clone, high availability, audit, least-privilege RBAC, encryption at rest. Key takeaway: “Version configuration. Broker secrets. Authenticate workloads.”

Distinguish two bands: “CURRENT — PHASE 06” for native config server and “PRODUCTION EVOLUTION — PHASES 18–19” for Kubernetes and external secret stores. Spell service/path names exactly. Avoid tiny paragraphs, status dashboards, dark mode, neon, photorealism, and sequential arrows that imply secrets pass through Git.

