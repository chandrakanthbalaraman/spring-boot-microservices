# discovery-server (Phase 03 scaffold)

Eureka registry for SB-MS. You implement Slice A — this folder is structure only.

## Checklist (you fill)

- [ ] POM: `spring-cloud-starter-netflix-eureka-server`
- [ ] `@EnableEurekaServer` on `DiscoveryServerApplication`
- [ ] `application.yml`: port `8761`; client register/fetch **false**
- [ ] Run: `mvn spring-boot:run -pl discovery-server` from `services/`
- [ ] UI: http://localhost:8761

Do **not** change Feign/WebClient `localhost` URLs in this slice.
