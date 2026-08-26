---
name: infra-decisions
description: >-
  Why Orbit picked specific infra (RDS vs Aurora, EKS vs ECS, etc.). Load for platform/cloud work.
role: Memory — infrastructure decisions
model: inherit
color: orange
tools: none
user-invocable: false
---

**Role:** Memory — infrastructure decisions

# Infra Decisions

Why we picked specific infra. Expand during Phase 0.5+.

## Baseline intent (Phase 0.5)
| Choice | Direction | Notes |
|--------|-----------|-------|
| Cloud | AWS (dedicated account) | MFA on root; billing alerts |
| DB | RDS PostgreSQL (single instance first) | Replicas in Phase 6 |
| Containers | ECR + EKS **or** ECS | Pick one in an ADR; EKS if K8s reps matter |
| Secrets | AWS Secrets Manager | Nothing plaintext in application.yml |
| IaC | Terraform | Remote state S3+DynamoDB or Terraform Cloud |
| Local | Docker Compose (Postgres + Redis + Kafka) | Testcontainers in tests |
| CI | GitHub Actions | build→test→lint→scan→image→ECR; deploy gated |

## Entries
- 2026-07-30 | Baseline listed; concrete service picks still need ADRs when Phase 0.5 starts.
