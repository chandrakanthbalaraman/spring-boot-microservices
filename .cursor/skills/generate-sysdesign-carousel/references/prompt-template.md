# Prompt template — one carousel slide

Fill every `{{…}}`. Pass matching `reference_image_paths`:

- **cache family** — `docs/phases/reference/cache/{n}.jpg`
- **api-gateway family** (interview-dense) — `docs/phases/reference/api-gateway/{n}.jpg` plus tokens in [`api-gateway-style.md`](api-gateway-style.md)

Pack catalog: [`docs/phases/README.md`](../../../../docs/phases/README.md).

```
Create a 1:1 LinkedIn/Instagram educational carousel slide.

STYLE (match reference images closely):
- Warm cream off-white paper background (#FDFBF7), soft pastel cards (blue/green/pink/yellow/lavender)
- Bold rounded display title + handwritten subtitle with red squiggle underline
- Pink sticky-note accent top-right; optional handwritten margin notes
- Soft rounded cards, subtle shadows, clean sans body text
- Friendly system-design cheat-sheet aesthetic (NOT dark mode, NOT neon, NOT purple gradient SaaS, NOT dense dashboard)
- Soft green leaf blur in corners OK
- Footer: centered white pill "{{FOOTER_BRAND}}" — no competitor Instagram handles
- Aspect ratio: 1:1 square, high resolution, sharp text, readable at phone size

SERIES: {{SERIES_TAG}}
TITLE: {{TITLE}}
SUBTITLE: {{SUBTITLE}}
STICKY NOTE: {{STICKY}}

LAYOUT TYPE: {{LAYOUT}}  (cover | cheat-sheet | binary-compare | pattern-cards | deep-dive | toolkit | cta)

CONTENT (render faithfully, do not invent APIs):
{{CONTENT_BLOCKS}}

DIAGRAMS (if any):
{{DIAGRAMS}}

KEY TAKEAWAY (optional bar):
{{TAKEAWAY}}

Text must be spelled correctly. Prefer short labels over paragraphs. Leave breathing room between cards.
```

## Example footer brands

- `SB-MS · Phase 02`
- `Follow for more` (no @handle)
- User-supplied `@handle` only when requested
