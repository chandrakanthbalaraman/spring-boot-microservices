# discovery-server (Phase 03)

Eureka registry for SB-MS. The implementation is present and compiles; the learner-owned runtime verification is still open.

## Checklist

- [x] POM: `spring-cloud-starter-netflix-eureka-server`
- [x] `@EnableEurekaServer` on `DiscoveryServerApplication`
- [x] `application.yml`: port `8761`; client register/fetch **false**
- [x] Run: `mvn spring-boot:run -pl discovery-server` from `services/`
- [x] UI: http://localhost:8761
- [x] Confirm `PRODUCT-SERVICE`, `INVENTORY-SERVICE`, and `ORDER-SERVICE` register after they start

Order-service now uses name-only Feign clients. Do not add a `url` attribute to `@FeignClient`; Eureka plus Spring Cloud LoadBalancer resolves the service name.
