# Backend Security & Multi-Tenant Isolation Test Report

## 1. Security Architecture Summary
- **Authentication**: Centralized JWT token issuance in `identity-service`. Tokens include `sub`, `tenantId`, `roles`, `iss`, `aud`, `exp`.
- **Authorization**: Enforced locally inside each microservice using Spring Security and explicit ownership verification.
- **Tenant Context**: Passed via `X-Tenant-Id` header and validated against the caller's JWT token context.

## 2. Tested Security Scenarios & Results

| Test Code | Domain | Scenario | Expected Http Code | Actual Code | Result |
|---|---|---|---|---|---|
| `SEC-001` | Auth | Request protected endpoint without JWT | 401 Unauthorized | 401 | **PASS** |
| `SEC-002` | Auth | Request protected endpoint with expired/tampered JWT | 401 Unauthorized | 401 | **PASS** |
| `SEC-003` | Role Auth | Customer user calling `/api/v1/admin/**` | 403 Forbidden | 403 | **PASS** |
| `SEC-004` | Role Auth | Seller user calling `/api/v1/admin/tenants` | 403 Forbidden | 403 | **PASS** |
| `SEC-005` | Tenant Isolation | User from `AURA_AYURVEDA` requesting `FASHION_STORE` resource | 403 / 404 Anti-enumeration | 403 / 404 | **PASS** |
| `SEC-006` | Seller Isolation | Seller A updating Seller B product or fulfillment order | 403 Forbidden | 403 | **PASS** |
| `SEC-007` | Customer Ownership | Customer A attempting to view or clear Customer B cart | 403 Forbidden | 403 | **PASS** |
| `SEC-008` | Medical Privacy | Unrelated customer or seller accessing patient prescription | 403 Forbidden | 403 | **PASS** |
