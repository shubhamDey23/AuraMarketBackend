# AuraMarket Integration Test Matrix

| Test ID | Service | Scenario / Feature | Expected Result | Actual Result | Status | Notes / Evidence |
|---|---|---|---|---|---|---|
| `AUTH-001` | Identity | Customer Login with valid credentials | 200 OK + JWT Tokens | 200 OK + JWT Tokens | **PASS** | Validated JWT claims (`sub`, `tenantId`, `roles`) |
| `AUTH-002` | Identity | Refresh Token Rotation | New Access & Refresh Tokens issued, old revoked | Old token rejected on reuse | **PASS** | Revocation checked |
| `AUTH-003` | Identity | Password Change | Old password invalidated, new password accepted | Password updated | **PASS** | BCrypt hash verification |
| `TENANT-001` | Tenant | Fetch Storefront Config (`AURA_AYURVEDA`) | 200 OK + Branding & Features | 200 OK + Branding & Features | **PASS** | Verified JSON payload |
| `CACHE-001` | Tenant | Redis Cache Hit on 2nd Request | Second call served from Redis | Redis HIT | **PASS** | Key: `tenant:storefront:AURA_AYURVEDA` |
| `CACHE-002` | Tenant | Redis Invalidation on Branding Update | Cache evicted, new branding returned | Cache Evicted & Reloaded | **PASS** | Cache evict annotation |
| `CACHE-003` | Tenant | Redis Unavailable Fallback | Application reads PostgreSQL directly | Serves DB response | **PASS** | Fallback catch handler |
| `CATALOG-001` | Catalog | Seller Product Creation | Draft product created, status `PENDING_APPROVAL` | Status `PENDING_APPROVAL` | **PASS** | Seller ID attached |
| `CATALOG-002` | Catalog | Admin Product Approval | Product status updated to `ACTIVE` | Status `ACTIVE` | **PASS** | Published `product.approved.v1` |
| `SELLER-001` | Seller | Seller Isolation Check | Seller A cannot access Seller B store details | 403 Forbidden / Restricted | **PASS** | Ownership check enforced |
| `INVENTORY-001`| Inventory | Stock Reservation | Available stock reduced, reserved increased | Quantities updated | **PASS** | Prevents available < 0 |
| `CART-001` | Cart | Multi-Seller Cart Add | Items from multiple sellers added | Subtotals calculated | **PASS** | SellerId per cart item |
| `ORDER-001` | Order | Checkout & Fulfillment Groups | Order created with per-seller FulfillmentGroups | Groups created | **PASS** | Published `order.created.v1` |
| `PAYMENT-001` | Payment | Initiate & Verify Payment | Payment transaction created, event published | Payment COMPLETED | **PASS** | Published `payment.completed.v1` |
| `CONSULT-001` | Consultation | Book Appointment & Issue Prescription | Appointment created, doctor issues prescription | Prescription saved | **PASS** | HIPAA privacy check passed |
| `NOTIF-001` | Notification | Kafka Event Processing & Idempotency | Event consumed, duplicate `eventId` skipped | Notification logged once | **PASS** | Event deduplication verified |
| `ADMIN-001` | Admin | Audit Logging | Operations logged in `audit_logs` | Audit record saved | **PASS** | Passwords/tokens excluded |
| `SEC-001` | Gateway | Customer accessing `/api/v1/admin/**` | 403 Forbidden | 403 Forbidden | **PASS** | Gateway & Backend security |
