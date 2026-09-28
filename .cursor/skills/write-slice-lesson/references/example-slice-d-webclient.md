# Gold-standard example — Phase 02 · Slice D — WebClient

Canonical learner notes live at `docs/tutor/webclient.md`. This file is the skill’s bundled copy — keep them in sync. Do not regenerate Slice D into `docs/phases/.../slices/` unless the learner asks `file`.

---

# Phase 02 · Slice D — WebClient

The core lesson here is this: reactive client ≠ reactive application. WebClient is Spring's non-blocking HTTP client from the WebFlux stack. You're running a servlet MVC app — synchronous from top to bottom. When you call `.block()` at the service boundary (and you will), you are saying: "run this reactive pipeline, but make the current thread wait for the result." The thread still blocks. Nothing magical happens. But you will have learned the API, seen exactly where the impedance mismatch lives, and understood why you'd reach for WebClient in a fully reactive app but not here.

Here is how the three clients look next to each other after you finish Slice D:

```
order-service :8083
     /                    \
WebClient (GET)            OpenFeign (reserve/release)
product :8081              inventory :8082
```

| Neighbor | Client | Why |
|----------|--------|-----|
| Product | WebClient (one GET) | Compare vs RestClient |
| Inventory | Feign (unchanged) | Slice C stays |

---

## Step 1 — Add the dependency

In `services/order-service/pom.xml`, add one dependency. Do not add `spring-boot-starter-webflux` to the other services — the reactive runtime stays in order-service only.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

Why not a standalone webclient jar? WebClient lives inside `spring-webflux`. You're pulling in the whole WebFlux module, but since `spring-boot-starter-web` is already on the classpath, Spring Boot keeps the Netty reactor from starting. Your app stays on Tomcat. Verify this after adding the dependency:

```bash
mvn spring-boot:run -pl order-service
# You should see: Tomcat started on port 8083
# NOT: Netty started on port 8083
```

If you see Netty, you accidentally removed `spring-boot-starter-web`. Put it back.

---

## Step 2 — Create the WebClient bean

You already have a `RestClientConfig` from Slices A+B. Add the WebClient bean there — same timeouts from `clients.product-service` — or a new `WebClientConfig.java` if you prefer separation.

```java
@Bean
public WebClient productWebClient(ProductClientProperties properties) {
    HttpClient httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                (int) properties.connectTimeout().toMillis())
        .responseTimeout(properties.readTimeout());

    return WebClient.builder()
        .baseUrl(properties.baseUrl())
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
}
```

Reuse the same YAML keys Slice A already wired. No second millisecond copy.

---

## Step 3 — Write the client class

Create something like `ProductWebClient` under `client/` (or `client/web/`). Mirror RestClient’s domain vs transport split.

```java
public ProductResponse getProductByProductId(Long productId) {
    try {
        return webClient.get()
            .uri("/api/v1/products/{productId}", productId)
            .retrieve()
            .onStatus(status -> status.value() == 404, response ->
                Mono.error(new ProductNotFoundException("Product not found: " + productId)))
            .bodyToMono(ProductResponse.class)
            .block(); // ← the lesson lives here
    } catch (WebClientRequestException e) {
        throw new DownstreamServiceUnavailableException(
                "product-service", "product-service unavailable", e);
    }
}
```

Three things about `.block()`:

1. It turns `Mono<ProductResponse>` into `ProductResponse` — the servlet thread waits.
2. Exceptions from `onStatus` / `Mono.error` still surface as normal Java exceptions.
3. Transport failures are **not** `ResourceAccessException` — catch `WebClientRequestException` (connect refused, timeout, DNS).

---

## Step 4 — Wire it into OrderServiceImpl

Replace the product RestClient call with the WebClient client. Keep Feign inventory untouched.

```java
// Before
ProductResponse product = productClient.getProductByProductId(...);

// After
ProductResponse product = productWebClient.getProductByProductId(...);
```

---

## Step 5 — Run the happy path

```bash
curl -X POST http://localhost:8083/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"items":[{"productId":5,"quantity":1}]}'
```

Expect **201**. Log should show the product GET via WebClient; inventory still Feign.

---

## Step 6 — Break it

Stop **product-service**, repeat the POST.

| Question | Your answer |
|----------|-------------|
| What HTTP status did order-service return? | |
| How long did the request take? | |
| What exception appeared in the log? | |
| Is it ResourceAccessException or something else? | |

Correct: **503** inside the connect/read budget (~500ms refused, ≤2s read), `WebClientRequestException` (or wrapped), not a hang. If it hangs >2s, timeouts are not wired.

---

## What you just learned

- Threading with `.block()` in MVC is the same as RestClient — the thread still waits.
- API style and error type differ (`WebClientRequestException` vs `ResourceAccessException`).
- Timeout config moves to Reactor Netty `HttpClient`, not `ClientHttpRequestFactory`.
- “WebClient is faster” only matters in a fully reactive stack without holding a servlet thread.

---

## Done? What to verify before pinging for review

- [ ] `spring-boot-starter-webflux` in `order-service/pom.xml` only
- [ ] App starts on Tomcat, not Netty
- [ ] Product WebClient bean named distinctly from `productRestClient`
- [ ] `.block()` exactly once in the client class, not in the service layer
- [ ] Transport failure → 503 ProblemDetail (stable detail, not a raw Netty dump)
- [ ] 404 from product-service → `ProductNotFoundException` → 404 to the caller
- [ ] Product stopped → fail ≤ timeout budget, status 503
- [ ] Feign inventory path untouched

When those are green, ping for review — paste the new client + `OrderServiceImpl` wiring diff.
