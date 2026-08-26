---
name: review-security
description: >-
  OWASP-aligned security review for Orbit code or designs. Use when reviewing auth, input validation, secrets, or SQL. Do not implement fixes unsupervised.
role: Skill — security review procedure
model: opus
color: red
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Skill — security review procedure

# Skill: review-security

Trigger: user asks for a security review or OWASP pass.

## Method
1. Follow /.claude/agents/qa-security-engineer.md checklist.
2. Apply /.claude/rules/security.md.
3. Suggest direction, not full unsupervised fixes.
4. Output: ranked findings + Security review sign-off line.
