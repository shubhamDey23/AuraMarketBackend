# AuraMarket Kafka Event Architecture

## Event Envelope Schema
All domain events published across services utilize a standardized JSON envelope structure:

```json
{
  "eventId": "uuid-v4-string",
  "eventType": "order.created.v1",
  "eventVersion": "1.0",
  "occurredAt": "2026-09-25T18:35:00Z",
  "correlationId": "corr-uuid-12345",
  "tenantId": "AURA_AYURVEDA",
  "producer": "order-service",
  "payload": {}
}
```

## Configured Topics

1. `user.registered.v1`: Triggered when a new user signs up (consumed by `notification-service`).
2. `product.created.v1`: Triggered when a new product is added/approved (consumed by `inventory-service`, `search-index`).
3. `inventory.updated.v1`: Triggered when stock allocation or adjustment occurs (consumed by `catalog-service`, `cart-service`).
4. `order.created.v1`: Triggered on checkout submission (consumed by `inventory-service`, `payment-service`, `notification-service`).
5. `payment.completed.v1`: Triggered when payment succeeds (consumed by `order-service`, `notification-service`).
6. `appointment.booked.v1`: Triggered when an Ayurveda doctor consultation is booked (consumed by `notification-service`).
7. `notification.requested.v1`: General async trigger for sending email/SMS/in-app notifications.

## Reliability Mechanisms
- **Idempotence**: Every consumer implements idempotent processing using `eventId` deduping.
- **Retry Mechanism**: Non-transient errors trigger backoff retries.
- **Dead Letter Topics (DLT)**: Messages failing max retry attempts are pushed to `{topic}.DLT` for inspection.
