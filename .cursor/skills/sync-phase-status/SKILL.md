---
name: sync-phase-status
description: >-
  Diff repo (and optional Trello/Notion) against orbit-roadmap.md / AGENTS.md Current phase, then update status for completed and in-progress work. Use after finishing a phase task, closing a slice of Phase 0.5+, or when the user says sync phase status / update roadmap progress.
role: Skill — phase progress sync
model: inherit
color: cyan
tools: Read, Grep, Glob, Bash, Write, Edit
allowed-tools: Read, Grep, Glob, Bash, Write, Edit
---

**Role:** Skill — phase progress sync

# Skill: sync-phase-status

Trigger: user wants status docs updated to match what is actually done. Prefer `/sync-phase-status`.

## Sources of truth

| Source | Role |
|--------|------|
| `orbit-roadmap.md` | Phase deliverables + Board Status table |
| `AGENTS.md` + `CLAUDE.md` | **Current phase** one-liner (must match) |
| Repo + `git log` | Evidence of completion |
| Trello (optional) | Card desc/checklists when user asked |
| Notion (optional) | **Orbit Second Brain** tree when user asked |

## Notion second brain (required when updating Notion)

```
Spring Boot  https://app.notion.com/p/ad0a18db9137426280b2d1604001740c
└── Orbit — Second Brain (Roadmap)  https://app.notion.com/p/3b4f60e0d3ef81dfa102e3aafac69f7c
    ├── Board snapshot (Trello mirror)  https://app.notion.com/p/3b3f60e0d3ef814aa4effff69ca53589
    ├── Phase N — …                    goal · status · full task table
    │   └── Task TN — …                 Decision · Why · Config/Code · How to run
    └── …
```

**Going forward (always):**
1. Update **Now** + **visual checklists** (`- [x]` / `- [ ]`) on Second Brain from `orbit-roadmap.md`.
2. Ensure **phase page** exists under Second Brain; enrich with goal + task checklist (not broken table links).
3. Create/update **task child** under that phase (template: Status · Decision · Why · Configuration & code · How to run local/prod · Evidence · Gaps).
4. Keep **Board snapshot** short (Trello table only) — do not re-dump full phase narratives there.
5. Keep **Spring Boot** parent to a one-block Orbit pointer → Second Brain.

**Notion link rule (critical):** Never put `<page>` tags inside tables — Notion mangles them. Use markdown links `[label](https://app.notion.com/p/…)` for inline navigation. Use `<page url="…">` only as real child blocks in a Children section.

## Status vocabulary

Use only: `DONE` | `PARTIAL` | `MISSING` | `N/A (intentional)`.

- **DONE** — evidence on disk (preferably committed); note uncommitted when applicable.
- **PARTIAL** — started; name the gap in one phrase.
- Do **not** advance **Current phase** early.

## Method

1. Read Current phase + matching `orbit-roadmap.md` section.
2. Scope via `$ARGUMENTS` if given; else Current phase.
3. Flatten criteria; inventory repo evidence.
4. Build matrix (criterion → status + path/commit).
5. Edit repo status surfaces (AGENTS + CLAUDE identical; roadmap Progress; phase-progress.md; sync checklist dates).
6. Do not invent AWS/CI/Kafka completion.
7. If user asked for external trackers: Trello card updates; Notion Second Brain + Board snapshot + phase/task pages per hierarchy above.
8. Output matrix + files + links + next task. Sign-off: `Phase sync: UPDATED / DRY-RUN / BLOCKED`.

## Out of scope

- Implementing missing deliverables
- Marking a whole phase complete from one slice
- Moving Trello lists without ask
- Inventing checklist items without repo evidence
