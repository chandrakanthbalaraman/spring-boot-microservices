# Slice lesson template (canonical)

Use this **section order** for every phase-slice lesson. Fill every section. Do not skip the break-it table or the final checklist.

Tone target: `docs/tutor/webclient.md` (Slice D). Deliver the filled lesson **in chat**. Do not write a docs file unless the user says `file`.

```markdown
# Phase {NN} · Slice {LETTER} — {Short title}

{2–4 sentences: the core lesson. What should change in the learner’s mental model.
Call out the impedance mismatch / trade-off / product decision if there is one.}

Here is how things look **after** you finish this slice:

```
{ASCII or short table: who talks to whom with which client}
```

| Neighbor / concern | Client / approach | Notes |
|--------------------|-------------------|-------|
| … | … | … |

---

## Step 1 — {First concrete action, e.g. dependency / config / interface}

{Why this step exists — 1–3 sentences.}

{Where to edit — exact path under the phase folder.}

```xml|java|yaml|bash
{Illustrative sketch — enough to learn from, not a paste-complete dump of the whole service}
```

{Pitfalls / verify after this step.}

---

## Step 2 — …

---

## Step N — Run the happy path

```bash
{curl or mvn commands against real ports from the phase README}
```

{What success looks like (status, log line to watch for).}

---

## Step N+1 — Break it

{How to break the dependency (stop a service, black-hole port, bad payload).}

| Question | Your answer |
|----------|-------------|
| What HTTP status did order-service (or caller) return? | |
| How long did the request take? | |
| What exception appeared in the log? | |
| Which typed exception / ProblemDetail title did you get? | |

{What “correct” looks like — timeout budget, status code, no hang.}

---

## What you just learned

- {Bullet: concept}
- {Bullet: API / error-type difference vs prior slice}
- {Bullet: what this is *not* (later phase)}

---

## Done? What to verify before pinging for review

- [ ] {Concrete evidence item}
- [ ] …
- [ ] Break-it row filled (status + approximate timing)
- [ ] Out-of-scope items untouched (list them)

When those are green, ping for review (same format as the previous slice). Paste the main new class + the wiring diff.
```

## Density rules

- Prefer **one lesson per slice**. Split if the slice has two unrelated concepts.
- Code blocks are **sketches**: real package names from the repo, but leave room for the learner to type.
- Always name the **prior-slice analog** (e.g. WebClient’s `WebClientRequestException` ≈ RestClient’s `ResourceAccessException`).
- Always end with a **checkbox** verify list, not a prose “you’re done.”
- Never instruct the learner to jump phases or add Eureka/Gateway/CB unless that is the slice.
