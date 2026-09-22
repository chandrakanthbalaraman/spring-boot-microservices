# Phase Infographic — Prompt Template

Use this when calling the image generator. Fill `{placeholders}` from `orbit-roadmap.md`.

## System visual contract (always paste)

```
Hand-drawn sketchbook/whiteboard architecture infographic for Orbit.
Light cream paper background, thin black ink outlines, soft teal/blue/coral
watercolor accents, informal hand lettering, optional friendly blue-white
robot mascots. Landscape 16:9, dense but readable, portfolio quality.

NO status tracking: no DONE/PARTIAL/MISSING badges, no green completion
checklists, no commit hashes, no git branch names, no "Last synced" ribbons,
no progress bars. Pure architecture / entity / flow representation.

Avoid: purple-on-white AI clichés, dark-mode neon, flat corporate slides,
emoji spam, photorealism.
```

## Structure (always paste)

```
TITLE: "{PHASE_TITLE}"
Subtitle: "{ONE_LINE_PURPOSE}"
Robot speech bubble: "{SHORT_CATCHPHRASE}"

Five numbered panels:
1. {ENTITY_OR_SCAFFOLD_MAP} — folders, packages, or compose files with real names
2. {CAPABILITY_OR_PERSONA_MODEL} — agents, L1 components, or aggregates
3. {KNOWLEDGE_OR_LAYER_MODEL} — rules/skills/memory OR Compose L1–L8 with phase tags
4. {FLOW} — orchestration or runtime/data path with labeled arrows
5. {BOUNDARY} — deferred / out-of-scope plane for this phase

Footer sticky notes: "{STICKY_LEFT}" | "{STICKY_RIGHT}"
Tiny caption: "Orbit v3 · Phase {ID} architecture · see orbit-roadmap.md"
```

## Detail density

- Prefer concrete paths (`docker/compose.layer1-data.yml`, `com.orbit.service.impl`)
- Prefer concrete ports/services when they exist in the roadmap (5432, orbit-net, Flyway `V001`)
- Prefer named personas/skills/rules when Phase 0
- Do **not** invent Kafka/Keycloak/AWS boxes inside a phase that has not introduced them, except inside an explicit “boundary / later phase” panel

## Reference images

When available, pass as `reference_image_paths`:

- `docs/phases/posters/phase-1.png`
- `docs/phases/posters/phase-2.png`

## Output path

Save as `docs/phases/posters/phase-{ID}.png` (use `phase-0.5.png` for Phase 0.5).
