# Slide 05

Create a 1:1 square SB-MS interview-notebook comparison slide titled “NATIVE VS GIT BACKEND”. Left blue card “NATIVE FILESYSTEM — LEARNING”: fast local setup, no network dependency, direct file edits; cons: one machine, weak collaboration, no production distribution. Use path `file:../../infrastructure/config-repo`. Right green card “GIT BACKEND — PRODUCTION OPTION”: review history, branches/labels, rollback, shared remote source; cons: availability dependency, repository credentials, clone/cache operations. Bottom production checklist: private repo, deploy key or workload identity, `cloneOnStart`, health checks, multiple Config Server replicas, never store plaintext secrets. Footer: “5/12 · CHOOSE THE BACKEND”.

