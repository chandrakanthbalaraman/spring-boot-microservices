---
name: generate-dockerfile
description: >-
  Sketch a production-minded Dockerfile for an Orbit Spring Boot module. Use when containerizing a service.
role: Skill — Dockerfile sketch generator
model: sonnet
color: orange
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — Dockerfile sketch generator

# Skill: generate-dockerfile

Trigger: user asks for a Dockerfile or container image definition.

## Method
1. Multi-stage build; non-root user; minimal runtime image.
2. Do not bake secrets into the image.
3. JVM flags as config, not hard-coded without rationale.
4. Output: Dockerfile sketch + build/run notes.
