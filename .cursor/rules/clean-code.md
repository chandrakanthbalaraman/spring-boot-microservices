---
name: clean-code
description: >-
  Orbit clean-code constraints: clarity, naming, no dead code, intentional error handling. Always apply when writing or reviewing code.
role: Rule — clean code standards
model: inherit
color: green
tools: none
user-invocable: false
---

**Role:** Rule — clean code standards

# Rule: Clean Code

- Clarity over cleverness.
- Lombok: follow `lombok.md` / ADR-0004 — major decorators; never `@Data` on aggregate roots.
- Small, focused methods and classes with one reason to change.
- Meaningful names aligned with the domain glossary.
- No dead code or commented-out blocks in main.
- Handle errors intentionally — no empty catch blocks.
