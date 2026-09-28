---
name: generate-helm
description: >-
  Sketch Helm chart values for Orbit services (probes, resources, config). Use when deploying to Kubernetes.
role: Skill — Helm chart sketch generator
model: sonnet
color: blue
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — Helm chart sketch generator

# Skill: generate-helm

Trigger: user asks for Helm charts or K8s deploy templates.

## Method
1. Separate liveness/readiness; set requests/limits.
2. ConfigMaps/Secrets for config — not image rebuilds for env changes.
3. Align with platform-kubernetes agent expectations.
4. Output: chart/values sketch + probe notes.
