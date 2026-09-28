# Slide 11

Create a 1:1 square SB-MS interview-notebook operational slide titled “REFRESH, ROTATION & RECOVERY”. Three lanes. “CONFIG CHANGE”: commit/edit → Config Server → refresh or restart → validate. “SECRET ROTATION”: provider creates new version → sync/mount → application reload or rollout → revoke old version. “FAILURE”: unavailable Config Server at startup → fail fast; unavailable Git after clone → cached working copy may serve; missing required Kubernetes Secret → Pod does not start. Add design questions: static or runtime refresh? atomic rollback? stale tolerance? audit trail? owner and alert? Warning: “A rotated secret is useless until the application consumes it.” Footer: “11/12 · OPERATE THE CHANGE PATH”.

