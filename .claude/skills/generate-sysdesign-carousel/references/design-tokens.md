# Design tokens — System Design carousel (cache-reference style)

Extracted from `docs/phases/reference/cache/*.jpg` for SB-MS LinkedIn/Instagram packs.

## Canvas

| Token | Value |
|-------|--------|
| Aspect | `1:1` (1080×1080-class) |
| Background | Warm off-white / cream paper `#FDFBF7` – `#F7F5F0` |
| Texture | Soft paper grain OK; **no** heavy noise, **no** dark mode |
| Margins | Comfortable padding; cards never touch edges |
| Corner radius | ~12–20px on all cards |
| Shadows | Soft, subtle; not multi-layer neon |

## Palette

| Role | Hex (approx) | Use |
|------|----------------|-----|
| Ink | `#1A1A1A` | Titles, body |
| Muted | `#555555` | Secondary labels |
| Blue panel | `#E3F2FD` | Definitions, info |
| Green panel | `#E8F5E9` | Success, advantages, “how it works” |
| Pink/red panel | `#FFEBEE` / accent `#E74C3C` | Warnings, disadvantages, cache/miss |
| Yellow panel | `#FFF8E1` / highlighter `#F1C40F` | Tips, “when to use”, title underline |
| Purple panel | `#EDE7F6` | Process / numbered flow headers |
| Sticky pink | `#F8BBD9` | Corner sticky notes |
| CTA pill | White with thin border | Footer social bar |

**Avoid:** purple-on-white gradients, cyberpunk neon, glassmorphism, dashboard chrome.

## Typography

| Role | Style |
|------|--------|
| Series tag | Small caps / letter-spaced sans: `SYSTEM DESIGN SERIES` or `SB-MS PHASE 02` |
| Title | Large bold rounded / marker-feel display (not Inter/Roboto as hero) |
| Subtitle | Handwritten or soft script + **red squiggle underline** |
| Body | Clean readable sans |
| Margin notes | Handwritten marker; short phrases + optional `:)` |
| Sticky notes | Handwritten on pink Post-it |

## Recurring chrome

- Pink sticky (top-right) with one tip
- Floating handwritten phrase on a side edge
- Soft green leaf / plant blur in corners (optional, light)
- Center footer pill: brand or “Follow for more” — **no handle unless user supplied**
- Bottom handwritten left + yellow blob right (“Same Concepts Bigger Opportunities” class)
- Key takeaway bar (full width) with lightbulb icon on deep-dive slides

## Icon language

- Flat / soft-3D isometric for services: laptop (client), cylinders (DB/cache), server racks, HTTP arrows
- Line icons for checklist rows
- Numbered circles on cards (`1` … `8`)
- Speech bubbles for callouts (“Frequently accessed data lives here!” style)

## Layout recipes

### Cover (slide 01)
Header → central flow diagram → 2×2 benefit circles → footer

### Cheat sheet (slide 02)
Dense grid: what / how / where / types / advantages / keep in mind + takeaway

### Binary compare (slide 03)
Two colored columns + “Key Differences” table + why-it-matters box

### Pattern cards (slides 04–05)
Numbered cards with mini diagram + “Best for:” footer bar

### Deep dive (slides 06–08)
What + How (flow) + advantages / disadvantages / when + takeaway

### Toolkit (slide 09)
Pastel tech cards + quick comparison table + how-to-choose

### CTA (slide 10)
Like / Share / Save cards; optional books stack; thank-you line — skip anime mascot unless asked

## SB-MS content rules

- Prefer real names from the phase: `order-service :8083`, `productRestClient`, `InventoryFeignApi`, `connect-timeout 500ms`, `read-timeout 2s`, ProblemDetail 503
- Color-code clients consistently across the pack:
  - RestClient → blue
  - OpenFeign → green
  - WebClient → purple/lavender
  - Idempotency / retry-safe key → yellow/amber
- Do not sneak Phase 3+ (Eureka, Gateway, Resilience4j) into a Phase 2 pack
- Footer brand: `SB-MS · Phase NN` — never copy reference handles (`@coderz.py`)

## Hard word workarounds (image gen)

Models often garble **Idempotency**. Prefer on-art labels:

| On slide | Meaning (README / caption) |
|----------|----------------------------|
| `Retry-Safe Key` | Idempotency-Key pattern |
| `IDEM-KEY` | HTTP header `Idempotency-Key` |
| `Safe retries (header)` | Slice E teaching point |

Put the correct spelling in the caption and README, not only on the bitmap.
