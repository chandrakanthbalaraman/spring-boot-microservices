---
name: generate-controller
description: >-
  Sketch a Spring MVC/WebFlux controller for an Orbit module with constructor injection and OpenAPI annotations. Use when adding or reshaping HTTP controllers.
role: Skill — controller sketch generator
model: sonnet
color: green
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — controller sketch generator

# Skill: generate-controller

Trigger: user asks for a controller or HTTP adapter in a module.

## Method
1. Confirm resource model with REST rules (/.claude/rules/rest.md).
2. Constructor injection only (`@RequiredArgsConstructor` allowed — `lombok.md`); no field `@Autowired`; no business logic beyond delegation.
3. Annotate every public endpoint with OpenAPI annotations.
4. Map errors to the stable error contract.
5. Output: controller sketch + required tests (happy/validation/authz).
