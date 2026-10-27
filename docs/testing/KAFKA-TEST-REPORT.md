# Kafka Event Messaging & Idempotency Test Report

## 1. Overview
Apache Kafka is employed for asynchronous, event-driven inter-service communication across domain boundaries (Notifications, Audit, Fulfillment, Inventory Triggers).

## 2. Tested Event Topics & Flow Verification

| Topic Name | Producer Service | Consumer Service(s) | Event Payload & Action Verified | Status |
|---|---|---|---|---|
| `user.registered.v1` | `identity-service` | `notification-service` | Welcome notification dispatched | **PASS** |
| `seller.approved.v1` | `seller-service` | `notification-service` | Seller store approval notification sent | **PASS** |
| `product.approved.v1` | `catalog-service` | `notification-service`, `admin-service` | Audit record logged & notification sent | **PASS** |
| `order.created.v1` | `order-service` | `notification-service`, `inventory-service` | Stock reserved & customer order notification sent | **PASS** |
| `payment.completed.v1` | `payment-service` | `order-service`, `notification-service` | Order status updated to CONFIRMED | **PASS** |
| `appointment.booked.v1` | `consultation-service` | `notification-service` | Appointment confirmation notification sent | **PASS** |
| `prescription.created.v1` | `consultation-service` | `notification-service` | Digital prescription notification sent | **PASS** |

## 3. Idempotency Test Results
- **Deduplication Strategy**: `notification-service` tracks processed `eventId` values using `existsByEventId(eventId)`.
- **Test**: The same Kafka event payload containing `eventId: "EVT-TEST-100"` was dispatched twice sequentially.
- **Outcome**: The first attempt saved and dispatched the notification. The second duplicate attempt was intercepted and skipped without duplicate record insertion.
- **Status**: **PASSED**
