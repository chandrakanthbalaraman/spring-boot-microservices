---
name: api-design-standards
description: REST API design patterns, response contracts, and error handling for Finance Tracker services. Use when designing new API endpoints, reviewing API contracts, implementing controllers/handlers, or verifying response structures. Triggers on tasks involving endpoint creation, error response formatting, pagination, HTTP status codes, or API versioning.
---

# API Design Standards

All Finance Tracker APIs must follow these standards. Load [references/api-standards.md](references/api-standards.md) for the full spec: `ApiResponse<T>` interfaces, JSON examples, error codes, endpoint naming patterns, and query parameter conventions.

## Quick Checklist

- [ ] Wrap every response in `ApiResponse<T>` — never return bare data or bare errors
- [ ] Set `success: true` and `errors: null` on success; `success: false` and `data: null` on failure
- [ ] Always include `meta.timestamp` (ISO 8601); add pagination fields for list endpoints
- [ ] Use correct HTTP status codes (see error codes table in references)
- [ ] Use `VALIDATION_ERROR` + `field` for field-level failures; domain-specific codes for business logic errors
- [ ] Name endpoints with plural nouns under `/api/v1/{resource}`
- [ ] Support `?page=&size=&sort=` for all list endpoints
