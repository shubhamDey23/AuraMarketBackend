# AuraMarket Service Ports Matrix

| Service Name | Local Dev Port | Internal Gateway Route Prefix | Database Name |
| :--- | :--- | :--- | :--- |
| `api-gateway` | 8080 | N/A (Edge entrypoint) | None |
| `identity-service` | 8081 | `/api/v1/auth`, `/api/v1/users` | `auramarket_identity` |
| `tenant-service` | 8082 | `/api/v1/storefront`, `/api/v1/tenants` | `auramarket_tenant` |
| `catalog-service` | 8083 | `/api/v1/catalog`, `/api/v1/products` | `auramarket_catalog` |
| `seller-service` | 8084 | `/api/v1/sellers` | `auramarket_seller` |
| `inventory-service` | 8085 | `/api/v1/inventory` | `auramarket_inventory` |
| `cart-service` | 8086 | `/api/v1/cart` | `auramarket_cart` |
| `order-service` | 8087 | `/api/v1/orders` | `auramarket_order` |
| `payment-service` | 8088 | `/api/v1/payments` | `auramarket_payment` |
| `consultation-service` | 8089 | `/api/v1/consultations` | `auramarket_consultation` |
| `notification-service` | 8090 | `/api/v1/notifications` | `auramarket_notification` |
| `admin-service` | 8091 | `/api/v1/admin` | `auramarket_admin` |

## Auxiliary Infrastructure Ports

- PostgreSQL: `5432`
- Redis: `6379`
- Kafka (External): `9092`
- Kafka (Internal Docker Network): `29092`
- Kafka UI: `8089`
- Zookeeper: `2181`
