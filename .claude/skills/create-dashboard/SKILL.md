---
name: create-dashboard
description: >-
  Propose metrics and dashboard panels for Orbit SLIs (JVM + business metrics). Use when adding observability dashboards.
role: Skill — observability dashboard planner
model: sonnet
color: pink
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — observability dashboard planner

# Skill: create-dashboard

Trigger: user asks for a monitoring dashboard or SLI panels.

## Method
1. Include golden signals + business metrics (e.g. orders/min, payment failure rate).
2. Tie panels to alerts and runbooks where possible.
3. Output: panel list + metric names + alert sketch.
