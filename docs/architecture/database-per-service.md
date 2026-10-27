# Database-per-Service Pattern & Isolation Rules

## Strict Isolation Policy
In AuraMarket, each microservice exclusively owns its dedicated database schema:

- `auramarket_identity` owned by `identity-service`
- `auramarket_tenant` owned by `tenant-service`
- `auramarket_catalog` owned by `catalog-service`
- `auramarket_seller` owned by `seller-service`
- `auramarket_inventory` owned by `inventory-service`
- `auramarket_cart` owned by `cart-service`
- `auramarket_order` owned by `order-service`
- `auramarket_payment` owned by `payment-service`
- `auramarket_consultation` owned by `consultation-service`
- `auramarket_notification` owned by `notification-service`
- `auramarket_admin` owned by `admin-service`

## Forbidden Practices

1. **NO Cross-Database Queries**: Direct SQL queries or foreign keys referencing tables in another service's database are strictly prohibited.
2. **NO Shared JPA Entities**: Services do not import ORM entities or mapping classes from other services.
3. **NO Shared Distributed Transactions**: Two-phase commit (2PC) is replaced by eventual consistency powered by Kafka domain events or saga patterns.

## Inter-Service Communication

- **Synchronous Reads**: Performed via HTTP REST calls exposed through client interfaces or gateway endpoints.
- **Asynchronous Writes & Events**: State changes publish event payloads to Apache Kafka (e.g. `order.created.v1`), allowing subscribing services to update local projections or send notifications independently.
