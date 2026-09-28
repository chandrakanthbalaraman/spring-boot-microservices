---
name: generate-openapi
description: >-
  Produce or update OpenAPI annotations/spec fragments for Orbit public APIs. Use when documenting endpoints or enforcing OpenAPI-first design.
role: Skill — OpenAPI annotation/spec generator
model: sonnet
color: blue
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — OpenAPI annotation/spec generator

# Skill: generate-openapi

Trigger: user asks for OpenAPI annotations or a spec update.

## Method
1. Every public API method must have OpenAPI annotations (project constraint).
2. Document request/response schemas and error responses.
3. Prefer annotations colocated with controllers unless a separate spec file is chosen via ADR.
4. Output: annotation/spec fragment + list of undocumented endpoints if any.
