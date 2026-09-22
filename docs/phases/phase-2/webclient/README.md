# Phase 2 topic — WebClient (planned)

Stub so the catalog stays easy to extend. **Do not generate images until this pack is requested.**

Style: [`../../reference/api-gateway/`](../../reference/api-gateway/). Pair with [`../clients/`](../clients/).

**Source of truth:** `WebClientConfig` + `ProductWebClient` (live product GET on `POST /orders`).

Suggested 10-slide arc (when generated):

| # | File | Page |
|---|------|------|
| 01 | `01-cover.png` | WebClient — reactive HTTP, used for one GET |
| 02 | `02-scenario.png` | Why not Feign on both neighbors |
| 03 | `03-what.png` | WebFlux client vs MVC `.block()` |
| 04 | `04-flow.png` | retrieve → onStatus → bodyToMono → block |
| 05 | `05-netty.png` | Reactor Netty `HttpClient` + `ChannelOption` |
| 06 | `06-timeouts.png` | CONNECT_TIMEOUT_MILLIS + responseTimeout |
| 07 | `07-errors.png` | 4xx domain / 5xx + `WebClientRequestException` → 503 |
| 08 | `08-pooling.png` | `ConnectionProvider.maxConnections` (pooling slice) |
| 09 | `09-traps.png` | `.block()` on the event loop; MVC vs WebFlux |
| 10 | `10-revision.png` | Recap |

Generate with: “create phase-2/webclient using generate-sysdesign-carousel + api-gateway style.”
