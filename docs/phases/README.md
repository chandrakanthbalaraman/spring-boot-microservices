# Phase carousels — catalog

Interview-refresh packs live here. Layout:

```
docs/phases/
├── README.md                 # this catalog
├── roadmap.png
├── posters/                  # 16:9 architecture posters (not carousel slides)
├── reference/                # style bibles (shared across phases)
│   ├── api-gateway/          # interview-dense notebook
│   ├── cache/                # pastel sticky-note LinkedIn
│   └── exception-handling/   # dense notebook
└── phase-{N}/
    ├── README.md             # phase index → subtopics
    └── {topic}/              # one 10-slide pack
        ├── README.md
        ├── prompts/
        └── 01-…png … 10-….png
```

**One folder = one pack.** New packs go under the phase: `docs/phases/phase-{N}/{topic}/`. Never overwrite an existing topic unless you say `redraw`.

Style bible for new interview packs: [`reference/api-gateway/`](./reference/api-gateway/).

Skill: [`.claude/skills/generate-sysdesign-carousel/`](../../.claude/skills/generate-sysdesign-carousel/) · tokens: [`api-gateway-style.md`](../../.claude/skills/generate-sysdesign-carousel/references/api-gateway-style.md).

## How to add a topic pack

1. Pick **phase + topic** — e.g. `phase-2/restclient` (never overwrite unless `redraw`).
2. Copy a sibling pack’s README table. Fill source-of-truth (phase folder, ADR, YAML keys).
3. Write `prompts/01-cover.md` … `prompts/10-revision.md` **before** generating.
4. Generate 1:1 PNGs with `reference_image_paths` → matching `reference/api-gateway/{n}.jpg`.
5. Add a row to the phase index + the table below.

```
docs/phases/phase-{N}/{topic}/
├── README.md          # slide table + caption + source of truth
├── prompts/           # NN-*.md — regenerate without guessing
├── 01-cover.png
├── …                  # filename order = post order
└── 10-revision.png
```

**Do not** bake `@handles` into art. Footer brand: `SB-MS · Phase NN`. Overlay a handle in the editor.

## Pack index

| Path | Phase | Topic | Style | Status |
|------|-------|-------|-------|--------|
| [`phase-1/overview/`](./phase-1/overview/) | 1 | Three services, DB-per-service | notebook (exception-handling) | shipped |
| [`phase-2/overview/`](./phase-2/overview/) | 2 | Slices A+B RestClient timeouts + errors | notebook | shipped — **keep** |
| [`phase-2/overview-v2/`](./phase-2/overview-v2/) | 2 | Cache-style RestClient / Feign / WebClient / IDEM-KEY | cache sticky-note | shipped — **keep** |
| [`phase-2/clients/`](./phase-2/clients/) | 2 | RestClient vs OpenFeign vs WebClient | **api-gateway** | shipped |
| [`phase-2/openfeign/`](./phase-2/openfeign/) | 2 | OpenFeign deep dive | **api-gateway** | shipped |
| [`phase-2/restclient/`](./phase-2/restclient/) | 2 | RestClient factory + `onStatus` / `execute()` | api-gateway | **planned** — stub |
| [`phase-3/discovery/`](./phase-3/discovery/) | 3 | Eureka teaching tool vs K8s DNS | **api-gateway** | shipped pack · phase folder planned |

## Style families

| Folder under `reference/` | Use when |
|---------------------------|----------|
| [`api-gateway/`](./reference/api-gateway/) | Interview-dense: code, numbered flow, traps, revision |
| [`cache/`](./reference/cache/) | Pastel sticky-note LinkedIn (`phase-2/overview-v2`) |
| [`exception-handling/`](./reference/exception-handling/) | Dense notebook (`phase-1/overview`, `phase-2/overview`) |

## Phase boundaries (do not leak)

| Pack | Allowed | Banned on-slide |
|------|---------|-----------------|
| Phase 2 clients / OpenFeign | RestClient, Feign, WebClient, 500ms/2s, ProblemDetail 503, localhost | Eureka, Gateway, Resilience4j, Kafka |
| Phase 3 discovery | Eureka, `name` without `url`, client vs server discovery, K8s DNS contrast | Gateway as the lesson, Resilience4j, Kafka |

Posters: [`posters/`](./posters/). Parent checklist: [`sb-roadmap.md`](../../sb-roadmap.md).
