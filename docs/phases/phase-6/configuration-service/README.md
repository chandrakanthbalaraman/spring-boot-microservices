# Configuration Management — Phase 06 Carousel

Twelve square slides for LinkedIn/Instagram. The series uses the SB-MS interview-notebook visual family and separates the running Phase 06 system from later production choices.

| # | File | Topic |
|---|---|---|
| 01 | `01-configuration-management-cover.png` | One build, many environments |
| 02 | `02-why-centralized-configuration.png` | Why configuration must leave the artifact |
| 03 | `03-sbms-phase6-architecture.png` | Current Config Server and Config Client flow |
| 04 | `04-hierarchy-and-precedence.png` | Shared, service, profile, environment precedence |
| 05 | `05-native-vs-git-backend.png` | Local learning backend versus production Git |
| 06 | `06-config-vs-secrets.png` | Classification boundary |
| 07 | `07-vault-vs-aws-secrets.png` | Secret-manager trade-offs |
| 08 | `08-kubernetes-secret-consumption.png` | `secretKeyRef`, `envFrom`, and volumes |
| 09 | `09-kubernetes-production-integrations.png` | ESO, CSI, Sealed Secrets, workload identity |
| 10 | `10-where-should-it-live.png` | Placement decision tree |
| 11 | `11-refresh-rotation-recovery.png` | Change propagation and failure behavior |
| 12 | `12-production-revision.png` | Comparison matrix and quick revision |

## Caption seed

Configuration is not just YAML. A production design separates non-secret settings from credentials, establishes a clear precedence model, defines how changes reach running workloads, and limits every workload to the smallest secret scope it needs. SB-MS Phase 06 begins with Spring Cloud Config's native filesystem backend; the later Kubernetes phases provide the deployment boundary for ConfigMaps, Secrets, external secret stores, and workload identity.

## Accuracy notes

- Kubernetes Secret data is base64-encoded, not encrypted by that encoding. Enable encryption at rest and least-privilege RBAC.
- Secret-backed environment variables are read when a container starts; rotation normally requires a restart.
- Secret volume updates are eventually consistent; a `subPath` mount does not receive updates.
- External Secrets Operator synchronizes provider values into Kubernetes Secret objects.
- Secrets Store CSI Driver can mount provider values as files without requiring a native Secret unless optional synchronization is enabled.
- Sealed Secrets stores encrypted declarations in Git but produces native Secrets in the cluster; controller key recovery is an operational responsibility.
- Prefer workload identity over long-lived cloud access keys.

## Sources

- [SB-MS roadmap](../../../sb-roadmap.md)
- [Spring Cloud Config Git backend](https://docs.spring.io/spring-cloud-config/reference/server/environment-repository/git-backend.html)
- [Kubernetes Secrets](https://kubernetes.io/docs/concepts/configuration/secret/)
- [Kubernetes volumes](https://kubernetes.io/docs/concepts/storage/volumes/)
- [HashiCorp Vault on Kubernetes](https://developer.hashicorp.com/vault/docs/deploy/kubernetes)
- [AWS Secrets Manager](https://docs.aws.amazon.com/secretsmanager/)
- [External Secrets Operator](https://external-secrets.io/)
- [Bitnami Sealed Secrets](https://github.com/bitnami/sealed-secrets)

