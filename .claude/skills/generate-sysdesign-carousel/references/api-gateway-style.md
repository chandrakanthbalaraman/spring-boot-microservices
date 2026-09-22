# Design tokens — API Gateway notebook style (interview packs)

Extracted from `docs/phases/reference/api-gateway/1.jpg` … `10.jpg`.

Use this family when the user asks for **interview-dense** slides (code, numbered flow, traps, quick revision) — not the cache sticky-note look.

## Canvas

| Token | Value |
|-------|--------|
| Aspect | `1:1` (1080×1080-class) |
| Background | Lined notebook paper — faint blue horizontal rules, **spiral binding on the left** |
| Texture | Paper, marker ink, highlighter; **no** dark mode, **no** neon, **no** Inter-dashboard |
| Density | High — many labeled cards, still 8–12px breathing room between cards |
| Margins | Cards never touch the spiral or the outer edge |

## Palette (highlighter / marker)

| Role | Look | Use |
|------|------|-----|
| Title bar | Yellow highlighter block | Main title |
| Subtitle bar | Pink / lavender rounded pill | One-line interview question or tagline |
| Blue card | `#E3F2FD` | Definitions, architecture, “what” |
| Green card | `#E8F5E9` | Advantages, “with X”, checklists |
| Pink/red card | `#FCE4EC` | Without X, disadvantages, traps |
| Yellow card | `#FFF8E1` | Tips, when-to-use, code callouts |
| Purple card | `#EDE7F6` | Process / Spring-specific / numbered flow |
| Ink | Near-black marker | Titles and body |
| Code box | White + thin border, colored keywords | YAML / Java snippets |

## Recurring chrome (copy the reference)

| Slot | Content |
|------|---------|
| Top-left | `Slide N/10` in a small rounded pill |
| Top-right | `SB-MS Interview Prep` (never `@java_interview_prep` / `@coderz.py`) |
| Footer center | Floppy-disk icon + `Save before your interview !` |
| Footer brand | `SB-MS · Phase NN` — **no Instagram handle** |
| Corners | Small doodles: cloud, coffee, paper plane, lightbulb — light, not a mascot |
| Callouts | Speech bubbles + pink sticky notes with one tip |

## Canonical 10-slide arc (map 1:1 to api-gateway pages)

| # | api-gateway cue | Layout |
|---|-----------------|--------|
| 01 | Cover + central diagram + 5 capability icons | Hero title, question bubble, hub diagram, 5 bottom icons |
| 02 | Interview scenario (without vs with) | Split problem / solution + benefits + interviewer tip |
| 03 | What is X? | Definition quote, position-in-architecture, why, analogy |
| 04 | Request flow | Left vertical pipeline + numbered steps + example + takeaways |
| 05 | Spring implementation | What / how / **code box** / key concepts / when / interview tip |
| 06 | Deep-dive pair (two concerns) | Two columns with mini-flow + code + status table |
| 07 | Pattern (pros/cons) | Before/after + JSON/code example + advantages/challenges |
| 08 | Production architecture | Big diagram + considerations + scaling + analogy |
| 09 | Biggest interview traps | 6–8 Q&A cards + quick tips strip |
| 10 | Quick revision | 9 numbered recap cards — the night-before slide |

Pass `reference_image_paths`: `docs/phases/reference/api-gateway/{n}.jpg` for slide `n`, plus `1.jpg` for chrome.

## SB-MS content rules (same as cache tokens)

- Real names: `order-service :8083`, `InventoryFeignApi`, `productWebClient`, `connect-timeout 500ms`, `read-timeout 2s`
- RestClient → blue · OpenFeign → green · WebClient → purple
- Prefer `Retry-Safe Key` / `IDEM-KEY` on-art (models misspell Idempotency)
- Footer: `SB-MS · Phase NN`
