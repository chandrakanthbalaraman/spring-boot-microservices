---
name: write-slice-lesson
description: >-
  Slice lessons belong in chat, in the WebClient step template. Apply when writing
  a phase-slice walkthrough, running /write-slice-lesson, or when tempted to dump
  a lesson into docs/phases/.../slices. One slice. Learner implements.
role: Rule — slice lessons in chat
model: inherit
color: teal
tools: none
user-invocable: false
---

**Role:** Rule — slice lessons in chat

# Rule: Write Slice Lesson

When teaching the next phase slice, follow skill `write-slice-lesson` and command `/write-slice-lesson`.

## Deliverable

- Paste the **full lesson in the chat window**. That is the product.
- Do **not** create `docs/phases/.../slices/*.md` (or similar) unless the user explicitly says `file` / “save the lesson”.
- Canonical tone: `docs/tutor/webclient.md` (Slice D). Template: `.claude/skills/write-slice-lesson/references/template.md`.

## Shape (every slice)

1. Core insight (2–4 sentences)
2. After diagram / table
3. Numbered steps — why → path → sketch → pitfalls
4. Happy path (`curl` against real ports)
5. Break-it observation table (empty “Your answer”)
6. What you just learned
7. Done? checkbox verify list

## Hard constraints

- **One slice** per lesson. Do not dump Feign + WebClient + idempotency together.
- Agent **sketches**. Learner **implements** in the phase folder. No finished client/service dump unless they say `pair` / `write it` / `implement this`.
- Do not mark the slice DONE or run `/sync-phase-status` as a side effect of writing the lesson.
- Do not sneak Eureka / Gateway / Resilience4j / Kafka into a slice that is not that phase.
- `localhost` until Phase 3 unless the slice *is* discovery.

This is **not** `/generate-phase-infographic` (poster) and **not** the sysdesign carousel skill.
