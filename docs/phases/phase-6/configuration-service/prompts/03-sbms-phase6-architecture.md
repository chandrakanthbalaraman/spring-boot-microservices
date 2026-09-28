# Slide 03

Create a 1:1 square SB-MS interview-notebook architecture slide. Title: “SB-MS PHASE 06 ARCHITECTURE”. Draw `infrastructure/config-repo/` containing `application.yml`, `product-service.yml`, `product-service-dev.yml`, `product-service-prod.yml` → `config-server :8888` with native filesystem backend → `product-service` Config Client. Separate locked box: environment variables `PRODUCT_DB_URL`, `PRODUCT_DB_USERNAME`, `PRODUCT_DB_PASSWORD` feed product-service directly, never Config Server or Git. Show request `/product-service/{profile}` and packaged import `configserver:http://localhost:8888`. Callouts: “non-secret config is central” and “secrets remain external”. Footer: “3/12 · CURRENT IMPLEMENTATION”. No Git/Vault/AWS boxes on this current-architecture slide.

