# config-server (Phase 06 · Slice A)

Spring Cloud Config Server skeleton. Fill in the TODOs, then prove the Environment API.

| | |
|--|--|
| Port (target) | `8888` |
| Backend (Slice A) | Native — `classpath:/config-repo` |
| Out of scope | Config Client, `@RefreshScope`, Git URI, Eureka registration |

## Checklist

- [ ] POM: `spring-cloud-config-server` + `spring-boot-starter-web` (already in skeleton)
- [ ] `@EnableConfigServer` on `ConfigServerApplication`
- [ ] `application.yml`: port `8888`; `spring.profiles.active: native`; `search-locations: classpath:/config-repo`
- [ ] Seed `config-repo/` (`application.yml` + at least one app file, e.g. `order-service.yml`)
- [ ] Run: `mvn spring-boot:run -pl config-server` from `services/`
- [ ] Probe: `curl http://localhost:8888/order-service/default`
- [ ] Break-it: stop server → connection refused; unknown app name → inspect `propertySources`

Do not wire Config Client into order/product/inventory in this slice.
