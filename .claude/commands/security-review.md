---
name: security-review
description: >-
  OWASP-aligned Orbit security review: agent flags issues, human fixes, re-review required. Use before merge when auth/input/SQL/secrets are touched.
role: Command — security review workflow
model: opus
color: red
tools: Read, Grep, Glob, Bash
allowed-tools: Read, Grep, Glob, Bash
---

**Role:** Command — security review workflow

# /security-review

OWASP-aligned security review. Agents suggest direction; humans implement fixes; re-review required.

## Steps
1. **qa-security-engineer**: apply checklist in `/.claude/rules/security.md` and agent file
2. Use skill `review-security` for scoped code/design review
3. **(human)** implement fixes — agent does not self-approve its own fixes
4. **qa-security-engineer**: re-review
5. **docs-adr-generator**: ADR for auth/crypto/boundary decisions
6. Sign-off required: `PASS` / `PASS WITH NOTES` / `BLOCKED`
