# Configuration Service Overview

`00-configuration-service-overview.png` is a 16:9 Phase 06 architecture map covering:

- the implemented native filesystem Config Server on port `8888`;
- the shared, service-specific, and profile-specific hierarchy;
- environment-variable precedence and the current `.env` secrets boundary;
- production Git, Vault, AWS Secrets Manager, and Kubernetes options;
- refresh, fail-fast, availability, and secret-placement decisions.

The image is educational architecture. Production integrations are labeled as future options aligned with Phases 18–19 rather than current implementation status.

