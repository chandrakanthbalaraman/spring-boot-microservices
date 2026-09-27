# API Standards Reference

## Response Contract

```typescript
interface ApiResponse<T> {
  success: boolean;
  data: T | null;
  errors: ApiError[] | null;
  meta: {
    timestamp: string;       // ISO 8601
    page?: number;
    size?: number;
    totalElements?: number;
    totalPages?: number;
  };
}

interface ApiError {
  code: string;
  message: string;
  field?: string;            // only for field-level validation errors
}
```

---

## Success Responses

### Single Resource

```json
{
  "success": true,
  "data": { "id": 1, "name": "Savings Account", "balance": 1500.00 },
  "errors": null,
  "meta": { "timestamp": "2026-01-29T12:00:00Z" }
}
```

### Paginated List

```json
{
  "success": true,
  "data": [
    { "id": 1, "name": "Account 1" },
    { "id": 2, "name": "Account 2" }
  ],
  "errors": null,
  "meta": {
    "timestamp": "2026-01-29T12:00:00Z",
    "page": 0,
    "size": 20,
    "totalElements": 45,
    "totalPages": 3
  }
}
```

---

## Error Responses

### Validation Error (400)

```json
{
  "success": false,
  "data": null,
  "errors": [
    { "code": "VALIDATION_ERROR", "message": "Amount must be positive", "field": "amount" },
    { "code": "VALIDATION_ERROR", "message": "Category is required", "field": "categoryId" }
  ],
  "meta": { "timestamp": "2026-01-29T12:00:00Z" }
}
```

### Business Error (e.g. 422)

```json
{
  "success": false,
  "data": null,
  "errors": [
    { "code": "INSUFFICIENT_FUNDS", "message": "Account balance is insufficient for this transfer" }
  ],
  "meta": { "timestamp": "2026-01-29T12:00:00Z" }
}
```

---

## Error Codes

| Code               | HTTP | Usage                    |
| ------------------ | ---- | ------------------------ |
| `VALIDATION_ERROR` | 400  | Field validation failed  |
| `BAD_REQUEST`      | 400  | Invalid request format   |
| `UNAUTHORIZED`     | 401  | Missing/invalid JWT      |
| `FORBIDDEN`        | 403  | Insufficient permissions |
| `NOT_FOUND`        | 404  | Resource doesn't exist   |
| `CONFLICT`         | 409  | Duplicate resource       |
| `INTERNAL_ERROR`   | 500  | Server-side failure      |

---

## Endpoint Naming

Base path: `/api/v1/{resource}`

```
GET    /api/v1/transactions           # List (paginated)
POST   /api/v1/transactions           # Create
GET    /api/v1/transactions/{id}      # Get one
PUT    /api/v1/transactions/{id}      # Update
DELETE /api/v1/transactions/{id}      # Delete

# Sub-resources
GET    /api/v1/accounts/{id}/transactions
POST   /api/v1/goals/{id}/contributions

# Actions (verb after resource)
POST   /api/v1/imports/{id}/commit
POST   /api/v1/recurring/{id}/pause
```

Rules:
- Plural nouns for all resources
- Lowercase, hyphen-separated words
- Actions use a trailing verb segment, not a query param

---

## Query Parameters

```
# Pagination (zero-based page index)
?page=0&size=20&sort=createdAt,desc

# Filtering
?accountId=1&type=EXPENSE&startDate=2026-01-01&endDate=2026-01-31

# Full-text search
?q=groceries
```
