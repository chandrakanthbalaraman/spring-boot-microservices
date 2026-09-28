---
name: write-slice-lesson
description: >-
  Author a step-by-step SB-MS phase-slice learning guide (core lesson, numbered
  steps with sketches, break-it table, verify checklist). Pass phase + slice as
  $ARGUMENTS (e.g. "2 D", "2 E"). Deliver the full lesson in chat. Learner
  implements; agent does not dump finished service code unless pair is requested.
role: Command — phase-slice lesson author
model: inherit
color: teal
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Command — phase-slice lesson author

# /write-slice-lesson

Write the **next learning walkthrough** for one phase slice in the Slice D style: core insight → numbered steps with sketches → break-it observation table → verify checklist.

**Primary output: the full lesson in the chat window.** Do not create docs files unless the user explicitly asks.

**Scope:** `$ARGUMENTS`  
Examples: `2 D` · `2 E` · `D` · empty → Current phase + next open slice from `sb-roadmap.md`

Append `file` **only** if the user wants a saved copy. Default is **chat-only**.

## Steps

1. **Load skill** — follow `/.claude/skills/write-slice-lesson/SKILL.md` end-to-end.
2. **Template** — `/.claude/skills/write-slice-lesson/references/template.md`.
3. **Tone** — match `docs/tutor/webclient.md` (canonical). Skill copy: `/.claude/skills/write-slice-lesson/references/example-slice-d-webclient.md`.
4. **Evidence** — read Current phase, that phase in `sb-roadmap.md`, phase README, real client/YAML/exception names in the phase folder, `decisions.md` / ADRs.
5. **Output** — paste the **full lesson in chat**. Write a file **only** if `file` was requested.
6. **Stop** — do **not** implement the slice into `src/` unless the user says `pair` / `write it` / `implement this`.

## Do not

- Create `docs/phases/.../slices/*.md` (or similar) by default — chat is enough
- Dump an entire phase (multiple slices) in one lesson
- Mark the slice DONE or run a full `/sync-phase-status` as a side effect
- Add Eureka / Gateway / Resilience4j / Kafka unless that is the slice
- Confuse this with `/generate-phase-infographic` (poster) or carousel skills
