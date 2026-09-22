# Regeneration notes — phase-3

| File | Ref | Lock |
|------|-----|------|
| 01-cover | 1 | Call `inventory-service`, not localhost:8082 |
| 02-scenario | 2 | Hard-coded YAML vs registry |
| 03-what | 3 | Registry / registration / lookup |
| 04-flow | 4 | Register → heartbeat → lookup → call |
| 05-client-vs-server | 5 | Caller picks vs DNS/VIP picks |
| 06-eureka | 6 | Teaching registry; do not over-invest |
| 07-feign-name | 7 | Drop `url`, keep `name` |
| 08-k8s | 8 | Eureka vs Kubernetes Service DNS |
| 09-traps | 9 | Gateway ≠ discovery ≠ load balancer |
| 10-revision | 10 | 9 recap cards |

Bridge from Phase 2: Feign already has the name. Timeouts 500ms/2s still apply after lookup.
