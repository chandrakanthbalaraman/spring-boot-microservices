---
name: generate-rest-api
description: >-
  Design and sketch a REST API surface (resources, verbs, errors, versioning) for Orbit. Use when defining or changing external/internal HTTP APIs.
role: Skill — REST API design procedure
model: sonnet
color: blue
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — REST API design procedure

# Skill: generate-rest-api

Trigger: user asks to design or generate a REST API.

## Method
1. OpenAPI-first: define resources and status codes before implementation detail.
2. Align versioning with /.claude/memory/api-contracts.md.
3. Separate internal vs external concerns when relevant.
4. Update api-contracts.md notes if the shape is meant to be stable.
5. Output: endpoint table + error shapes + OpenAPI sketch path suggestion.
