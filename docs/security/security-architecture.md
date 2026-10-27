# Security Architecture & Authorization Model

## Roles & Authorities
The system recognizes 4 fundamental role tiers:

1. `CUSTOMER`: Retail shoppers purchasing products or booking appointments.
2. `SELLER`: Merchants managing products, stores, and fulfillment groups.
3. `DOCTOR`: Healthcare professionals managing schedules, consultation slots, and patient notes.
4. `ADMIN`: System administrators overseeing multi-tenancy, seller approvals, doctor onboarding, and system metrics.

## Authentication & JWT Mechanics
- JWT tokens are issued exclusively by `identity-service`.
- The token payload includes: `sub` (userId), `email`, `roles`, `tenantCode`, `iat`, and `exp`.
- Verification and RBAC checks occur both at the `api-gateway` level and within each downstream microservice via Spring Security standard filters.

## Multitenancy Isolation
- Every request header contains `X-Tenant-ID`.
- Backend services enforce tenant isolation by scoping repository queries to `tenantId`.
