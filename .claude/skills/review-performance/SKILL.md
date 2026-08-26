---
name: review-performance
description: >-
  Performance review and benchmark plan for Orbit hot paths. Use when investigating latency, throughput, or regressions.
role: Skill — performance review procedure
model: sonnet
color: yellow
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Skill — performance review procedure

# Skill: review-performance

Trigger: user asks for performance review or optimization advice.

## Method
1. Require baseline measurements before recommending changes.
2. Follow /.claude/rules/performance.md and qa-performance-engineer agent.
3. Plan before/after numbers for /docs/benchmarks.
4. Output: findings + benchmark plan + sign-off.
