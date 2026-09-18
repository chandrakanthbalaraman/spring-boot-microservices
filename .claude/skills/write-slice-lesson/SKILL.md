---
name: write-slice-lesson
description: >-
  Write a step-by-step SB-MS phase-slice learning guide (core lesson, numbered
  steps with code sketches, break-it table, verify checklist). Use when starting
  a slice, the user asks for a lesson / walkthrough / “like Slice D”, or when
  running /write-slice-lesson. Guides the learner — does not dump a finished
  implementation into service code unless they say pair / write it / implement.
role: Skill — phase-slice lesson author
model: inherit
color: teal
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — phase-slice lesson author

# Skill: write-slice-lesson

Trigger: start next slice, “write the lesson”, “step by step like Slice D”, `/write-slice-lesson`.

## Goal

Produce a **learner-owned** walkthrough for **one** phase slice. Same structure every time (see [references/template.md](references/template.md)).

**Gold-standard tone:** learner notes at `docs/tutor/webclient.md` (Slice D). Skill copy of that lesson: [references/example-slice-d-webclient.md](references/example-slice-d-webclient.md). If they drift, **tutor wins** — do not invent a second style.

The agent **teaches and sketches**. The human **implements** in the phase folder. Do not paste a complete working client into `src/` unless the user says `pair` / `write it` / `implement this`.

## Inputs

Parse from `$ARGUMENTS` or the user message:

| Input | Default |
|-------|---------|
| Phase number | Current phase from `AGENTS.md` |
| Slice letter | Next open slice in `sb-roadmap.md` for that phase |
| Target service(s) | Infer from roadmap (usually `order-service`) |
| Output mode | **`chat` only (default).** Never write a docs file unless the user explicitly says `file` / “save the lesson”. |

Examples: `2 D` · `phase 2 slice E` · empty → Current phase + next open slice.

## Method

1. **Lock context**
   - Read `AGENTS.md` Current phase.
   - Read **only** that phase section in `sb-roadmap.md` (slices table + hands-on).
   - Read phase `README.md` exercises.
   - Check `/.claude/memory/decisions.md` and any ADR for this phase — do not contradict.
   - Inventory real paths/types in the phase folder (controllers, existing clients, YAML keys, exception names). Prefer evidence over invented APIs.

2. **Define the one lesson**
   - One core insight in 2–4 sentences (what changes in the mind, not just which class to add).
   - What stays unchanged (other clients, ports, localhost, out-of-scope phases).
   - Optional ASCII “after this slice” diagram.

3. **Write the guide** using [references/template.md](references/template.md) **verbatim section order**. Match `docs/tutor/webclient.md` density (short why, one sketch, one pitfall, empty break-it table, checkbox verify).
   - Numbered **Steps** with: why → what to create/edit → illustrative code sketch → imports/pitfalls.
   - Sketches may use the learner’s real package names and exception types from the repo.
   - Include a **Break it** step with an observation table (empty “Your answer” column).
   - End with **Done? What to verify** checklist (markdown `- [ ]`).
   - End with “when green, ping for review — same format as prior slice.”

4. **Deliver in chat** — paste the full lesson in the reply. That is the primary deliverable.
   - **Do not** create `docs/phases/.../slices/*.md` (or any other lesson file) by default.
   - Write a file **only** if the user explicitly says `file` / “save the lesson” / `/write-slice-lesson … file`.
   - Do **not** mark the slice DONE in `sb-roadmap.md` / `AGENTS.md` from this skill alone.

5. **Agent behavior after publishing the lesson**
   - Stop. Do not implement Steps into Java/POM unless asked.
   - On review request: load `review-pr` / qa-senior-code-reviewer; check against the lesson’s verify checklist + `.claude/rules/`.

## Hard rules (SB-MS)

- Java 21 · Boot 3.x · Maven · constructor injection · no secrets · Flyway if DB.
- One Git repo / phase folder — do not invent Phase N+1 folders.
- Do not sneak Eureka / Gateway / Resilience4j / Kafka into a slice that is not that phase.
- `localhost` URLs until Phase 3 unless the slice *is* discovery.
- Learning contract (`cursor.md`): guide ≠ dump.

## Out of scope

- Implementing the slice in `src/` (unless pair override)
- Full phase dump (multiple slices in one lesson)
- Carousel / infographic (use those skills)
- `/sync-phase-status` (status docs) — may *remind* to sync after the learner finishes

## Output to the user

1. The **full lesson markdown in the chat window** (required).
2. One line: “You implement Steps 1–N; I review when you ping.”
3. Suggested next command after they finish: review, then `/sync-phase-status`.
4. File path **only** if they asked to save — otherwise no new docs files.
