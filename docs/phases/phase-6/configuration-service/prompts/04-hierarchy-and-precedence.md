# Slide 04

Create a 1:1 square SB-MS interview-notebook educational slide titled “HIERARCHY & PRECEDENCE”. Show a vertical priority stack, highest first: 1 Environment variable, 2 Profile-specific remote config, 3 Service-specific remote config, 4 Shared remote defaults, 5 Packaged local defaults. Use real example: `SERVER_PORT=8281` overrides `product-service-prod.yml → 8183`; dev profile gives `8182`; base gives `8081`. Add mini merge diagram for shared + product + prod. Callout: “Most specific source wins.” Warning: “Know the order before debugging.” Footer: “4/12 · CONFIGURATION PRECEDENCE”.

