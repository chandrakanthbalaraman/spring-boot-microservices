# config-server (Phase 06)

Spring Cloud Config Server on the **native** (filesystem) backend. It serves the shared files in [`infrastructure/config-repo/`](../../infrastructure/config-repo/).

| | |
|--|--|
| Port | `8888` |
| Profile | `native` |
| Backend | `search-locations: ${CONFIG_REPO_LOCATION:file:../../infrastructure/config-repo}` |
| First client | `product-service` (Slice B) |
| Out of scope | `@RefreshScope`, Git URI, Eureka registration, encrypted values |

`file:../../infrastructure/config-repo` is resolved from the working directory. `mvn spring-boot:run -pl config-server` runs from the module folder, so that relative path lands at the repo root. From another working directory, set `CONFIG_REPO_LOCATION` to an absolute `file:` URI.

## Config repo

| File | Served to |
|------|-----------|
| `application.yml` | every application (shared defaults) |
| `product-service.yml` | `product-service`, any profile (`server.port: 8081`) |
| `product-service-dev.yml` | `product-service` with profile `dev` (`server.port: 8182`) |
| `product-service-prod.yml` | `product-service` with profile `prod` (`server.port: 8183`) |
| `order-service.yml` | `order-service` (not a Config Client yet) |

Keep secrets out of this repo. Database passwords stay in the gitignored repo-root `.env`.

## Run

```bash
# from services/
mvn spring-boot:run -pl config-server

# or, from the repo root, in its own terminal tab
./scripts/dev-services.sh config-server
```

## Probe

```bash
curl http://localhost:8888/product-service/default
curl http://localhost:8888/product-service/dev
curl http://localhost:8888/product-service/prod
```

`propertySources` lists the matching files, most specific first. With `dev`, `product-service-dev.yml` appears above `product-service.yml`, so `server.port` resolves to `8182`.

## Checklist

- [x] POM: `spring-cloud-config-server` + `spring-boot-starter-web`
- [x] `@EnableConfigServer` on `ConfigServerApplication`
- [x] `application.yml`: port `8888`, profile `native`, filesystem `search-locations`
- [x] Seed `infrastructure/config-repo/` (shared `application.yml` + `product-service` base and profile files)
- [x] Probe `product-service/default` returns the repo files
- [x] Break-it: stop the server, then start `product-service` and confirm it fails fast (`ConfigClientFailFastException`)
- [ ] Break-it: request an unknown application name and inspect `propertySources`
