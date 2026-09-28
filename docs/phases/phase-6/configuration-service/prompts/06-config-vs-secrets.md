# Slide 06

Create a 1:1 square SB-MS interview-notebook slide titled “CONFIGURATION ≠ SECRETS”. Two-column decision guide. “NON-SECRET CONFIG”: ports, feature flags, timeouts, log levels, public URLs → Config Git or Kubernetes ConfigMap. “SECRET”: database passwords, API tokens, private keys, signing keys → Vault, AWS Secrets Manager, or protected Kubernetes delivery. Center test: “Would disclosure create security impact?” yes → secret manager; no → configuration system. Never list actual credential values. Red warning bar: “Base64 encoding is not encryption.” Footer: “6/12 · CLASSIFY BEFORE STORING”.

