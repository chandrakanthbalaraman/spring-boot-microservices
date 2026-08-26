---
name: arch-review
description: >-
  Cross-module + NFR architecture review before structural Orbit work. Use before large refactors or new modules.
role: Command — architecture review workflow
model: opus
color: purple
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Command — architecture review workflow

# /arch-review

Architecture review before significant structural work.

## Steps
1. **arch-solution-architect**: cross-module impact
2. **arch-system-design-reviewer**: NFRs, failure modes, consistency
3. **arch-backend-architect** + **arch-domain-architect**: boundaries and aggregates
4. **docs-adr-generator**: draft ADR(s) for decisions to accept
5. **(human)** accept/reject decisions; update `/.claude/memory/decisions.md`
6. Sign-off: `READY FOR HUMAN` / `BLOCKED`
