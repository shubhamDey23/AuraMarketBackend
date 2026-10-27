# AuraMarket Service Boundaries & Responsibilities

| Microservice | Port | Database | Primary Responsibilities |
| :--- | :--- | :--- | :--- |
| `api-gateway` | 8080 | N/A (Stateless) | Entry point for Angular frontend. Route matching, CORS, rate limiting, and request routing. |
| `identity-service` | 8081 | `auramarket_identity` | User authentication, JWT issuance, roles (`CUSTOMER`, `SELLER`, `DOCTOR`, `ADMIN`), OAuth2/Google preparation. |
| `tenant-service` | 8082 | `auramarket_tenant` | Multi-tenant engine managing branding, logos, themes, dynamic navigation menus, feature flags, currency, and locale. |
| `catalog-service` | 8083 | `auramarket_catalog` | Product catalog, categories, subcategories, brands, variant metadata, and product status lifecycle. |
| `seller-service` | 8084 | `auramarket_seller` | Seller onboarding, store profiles, verification status, and seller configuration. |
| `inventory-service` | 8085 | `auramarket_inventory` | Real-time stock counts, reserved stock, damaged goods tracking, stock adjustments, batch/expiry metadata. |
| `cart-service` | 8086 | `auramarket_cart` | Customer shopping cart state, cart line items, multi-seller cart grouping, and price totals. |
| `order-service` | 8087 | `auramarket_order` | Single customer checkout supporting multi-seller order splitting (fulfillment groups), order status lifecycle. |
| `payment-service` | 8088 | `auramarket_payment` | Payment intent abstraction, payment provider gateway adapters, refund tracking, and payment receipts. |
| `consultation-service` | 8089 | `auramarket_consultation` | Doctor profiles, availability slots, appointment booking, medical records, tenant-scoped feature enablement. |
| `notification-service` | 8090 | `auramarket_notification` | Email, SMS, and in-app notification consumers listening to Kafka domain events. |
| `admin-service` | 8091 | `auramarket_admin` | Back-office management, seller approval, product moderation, doctor verification, platform audits. |
