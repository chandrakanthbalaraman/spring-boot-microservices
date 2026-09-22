# Regeneration notes — phase-2/openfeign

| File | Ref | Lock |
|------|-----|------|
| 01-cover | 1 | Declarative inventory client; five concerns |
| 02-scenario | 2 | Imperative RestClient vs one interface |
| 03-what | 3 | Proxy + contacts analogy |
| 04-flow | 4 | encoder → Client → decoder / RetryableException |
| 05-spring | 5 | `@FeignClient` + NOT `@Configuration` |
| 06-timeouts | 6 | `Request.Options` from same YAML 500ms/2s |
| 07-errors | 7 | ErrorDecoder vs RetryableException → 503 |
| 08-pooling | 8 | HttpURLConnection vs HC5; NEVER_RETRY |
| 09-traps | 9 | url vs name; global config leak |
| 10-revision | 10 | 9 recap cards |

Facts: `name=inventory-service`, `url=${clients.inventory-service.base-url}`, `@EnableFeignClients(clients = InventoryFeignApi.class)`.
