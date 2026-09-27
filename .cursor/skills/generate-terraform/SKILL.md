---
name: generate-terraform
description: >-
  Sketch Terraform modules for Orbit platform infra under /platform/terraform. Use when adding AWS/VPC/RDS/ECR/EKS resources.
role: Skill — Terraform module sketch generator
model: sonnet
color: purple
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — Terraform module sketch generator

# Skill: generate-terraform

Trigger: user asks for Terraform for Orbit infrastructure.

## Method
1. Place modules under /platform/terraform.
2. Remote state + locking; no plaintext secrets in git.
3. Document service choices in /.claude/memory/infra-decisions.md.
4. Output: module sketch + inputs/outputs + cost flags.
