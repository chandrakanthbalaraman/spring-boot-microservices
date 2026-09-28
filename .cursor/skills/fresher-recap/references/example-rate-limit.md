# Gold-standard example — Gateway product rate limit

Use this density and shape when running `fresher-recap`. Topic was: `RateLimitConfig` beans + product-service `RequestRateLimiter` filter.

---

## The big idea (one sentence)

**Rate limiting** = the gateway says: “each client may only make N requests in a time window.” Too many → **429 Too Many Requests**. Product traffic is protected at the **door** (gateway), not inside product-service.

---

## 1. `RateLimitConfig.java` — two helper beans

Think of a **ticket booth**:

| Bean | Job in plain English |
|------|----------------------|
| `caffeineProxyManager()` | **Where** we remember “how many tickets this client still has” (in-memory cache) |
| `clientIpKeyResolver()` | **Who** we count — “use their IP as the customer ID” |

### `clientIpKeyResolver` (easier one)

- Every request has a remote address (e.g. `127.0.0.1`).
- That IP becomes the **bucket key**.
- So: IP `A` and IP `B` get **separate** limits.
- `Mono.just(key)` = “I’m reactive (WebFlux); here’s the key when you need it.”

### `caffeineProxyManager` (storage)

- Bucket4j needs a place to store each IP’s token count.
- Caffeine = fast local cache (good for learning; Redis comes later for multi-instance).
- `maximumSize(1000)` ≈ remember up to 1000 IPs.
- Idle buckets can expire after the configured duration.

```
★ Insight ─────────────────────────────────────
• KeyResolver answers “rate-limit whom?” — IP, user id, or API key.
• ProxyManager answers “store counters where?” — memory now, Redis when you scale.
• Without a key resolver, everyone would share one bucket (unfair / useless).
─────────────────────────────────────────────────
```

---

## 2. Product route filters in `application.yml`

| Setting | Meaning |
|---------|---------|
| `Path=/api/v1/products/**` | Only product URLs use this rule |
| `RequestRateLimiter` | Turn rate limiting **on** for this route |
| `key-resolver: #{@clientIpKeyResolver}` | Use **your** bean → count by IP |
| `capacity: 3` | Bucket holds **3** tokens (max burst) |
| `refillTokens: 3` + `refillPeriod: 10s` | Every **10 seconds**, add **3** tokens back |
| `requestedTokens: 1` | Each HTTP call costs **1** token |

**Everyday analogy:** You get **3 tickets**. Each product API call costs 1 ticket. Every 10 seconds you get 3 new tickets. No tickets left → gateway rejects you before calling product-service.

`#{@clientIpKeyResolver}` = Spring SpEL: “inject the bean named `clientIpKeyResolver`.”

---

## Tiny flow

```
Browser/curl
    → api-gateway (:8080)
         → Is path /api/v1/products/** ?
              → Yes: check IP’s token bucket
                   → tokens left? forward to product-service
                   → empty? return 429
         → order/inventory routes: no this filter (unlimited for now)
```

---

## Mental model

1. **YAML** = policy: “3 requests / 10s per client on products.”
2. **`clientIpKeyResolver`** = “client” = IP.
3. **`caffeineProxyManager`** = memory notepad for remaining tokens.

---

## Why this is the gold standard

- One sentence big idea before details
- Tables for “who does what” and “what each knob means”
- One metaphor (tickets) reused consistently
- Short flow + 3-bullet mental model
- Insight box tied to *this* codebase, not generic theory
- Offers a next step without dumping a full lesson
