# Redis Caching & Resiliency Test Report

## 1. Caching Overview & Architecture
Redis is utilized as a high-performance secondary cache layer for high-throughput read operations. PostgreSQL remains the sole persistent source of truth.

## 2. Tested Cache Namespaces & Keys
- **Tenant Configuration**: `tenant:storefront:{tenantCode}`
- **Catalog Product Details**: `catalog:product:{tenantId}:{productId}`
- **Catalog Categories**: `catalog:categories:{tenantId}`
- **Customer Cart**: `cart:{tenantId}:{customerId}`
- **Inventory Availability**: `inventory:availability:{tenantId}:{productId}`

## 3. Test Scenarios & Results

### Scenario A: Cache Hit / Miss Cycle
1. Request: `GET /api/v1/storefront/config?tenantCode=AURA_AYURVEDA`
2. **First Request**: Cache Miss. Application queries `auramarket_tenant` database and populates Redis key `tenant:storefront:AURA_AYURVEDA`.
3. **Second Request**: Cache Hit. Application returns response directly from Redis without hitting PostgreSQL.
4. **Status**: **PASSED**

### Scenario B: Cache Invalidation on Mutation
1. Request: `PUT /api/v1/admin/tenants/{tenantId}/branding` to update primary color.
2. Result: Application mutates PostgreSQL database record and executes `@CacheEvict` for key `tenant:storefront:AURA_AYURVEDA`.
3. Subsequent `GET /api/v1/storefront/config`: Re-queries database and populates updated branding in Redis.
4. **Status**: **PASSED**

### Scenario C: Redis Service Failure & Graceful Degradation
1. Scenario: Redis connection forcibly disconnected / unavailable.
2. Request: `GET /api/v1/storefront/config`
3. Result: Spring Cache / Redis fallback handler catches connection exception, logs warning, and queries PostgreSQL directly. The application remains 100% operational.
4. **Status**: **PASSED**
