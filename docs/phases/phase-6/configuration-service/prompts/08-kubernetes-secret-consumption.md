# Slide 08

Create a 1:1 square SB-MS interview-notebook implementation slide titled “KUBERNETES SECRET CONSUMPTION”. Four numbered cards with mini YAML-like snippets and pros/cons. 1 `secretKeyRef`: explicit single key, clear mapping; restart needed for changed env value. 2 `envFrom`: simple bulk import; hidden coupling and collision risk. 3 Secret volume: read-only files, eventual update propagation; application must reread files. 4 Projected volume: combine selected Secret/ConfigMap/service-account sources; more mapping complexity. Warning callouts: “Secret data is base64-encoded, not encrypted by encoding” and “subPath mounts do not receive updates.” Footer: “8/12 · POD DELIVERY PATTERNS”.

