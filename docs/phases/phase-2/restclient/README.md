# Phase 2 topic — RestClient (planned)

Stub so the catalog stays easy to extend. **Do not generate images until this pack is requested.**

Style: [`../../reference/api-gateway/`](../../reference/api-gateway/). Pair with [`../clients/`](../clients/).

**Source of truth:** `RestClientConfig` + `productRestClient` / `inventoryRestClient` · ADR [`0001`](../../../adr/0001-restclient-timeouts-and-downstream-mapping.md).

Suggested 10-slide arc (when generated):

| # | File | Page |
|---|------|------|
| 01 | `01-cover.png` | RestClient as the Boot 3.2+ HTTP client |
| 02 | `02-scenario.png` | RestTemplate vs RestClient |
| 03 | `03-what.png` | Fluent API, named beans |
| 04 | `04-flow.png` | builder → factory → retrieve |
| 05 | `05-factory.png` | `ClientHttpRequestFactoryBuilder.detect()` |
| 06 | `06-timeouts.png` | 500ms connect / 2s read from YAML |
| 07 | `07-errors.png` | `onStatus` vs `execute()` wrap |
| 08 | `08-pooling.png` | `detect()` may pool; explicit max is the open slice |
| 09 | `09-traps.png` | Interview traps |
| 10 | `10-revision.png` | Recap |

Generate with: “redraw/create phase-2/restclient using generate-sysdesign-carousel + api-gateway style.”
