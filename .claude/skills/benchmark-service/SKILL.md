---
name: benchmark-service
description: >-
  Define and record a service benchmark for Orbit with commit-hash before/after results in docs/benchmarks.
role: Skill — service benchmark recorder
model: sonnet
color: yellow
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — service benchmark recorder

# Skill: benchmark-service

Trigger: user wants to benchmark an endpoint or service path.

## Method
1. Define workload, SLI, and environment.
2. Capture baseline with commit hash.
3. After change, capture after numbers in /docs/benchmarks.
4. Update /.claude/memory/benchmarks.md terse entry.
5. Output: benchmark doc draft + commands used.
