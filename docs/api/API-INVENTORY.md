# AuraMarket Complete API Inventory

This document serves as the authoritative source of truth for all microservice REST API endpoints in the AuraMarket backend. It will be used for API contract verification and Angular frontend integration.

---

## 1. API Gateway (Port: 8080)
- `/*` -> Routes requests to downstream microservices based on URL prefix.

---

## 2. Identity Service (Port: 8081)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `POST` | `/api/v1/auth/register` | No | Public | Register new customer or seller user | `RegisterRequest` | `AuthResponse` | `201 Created`, `400 Bad Request` |
| `POST` | `/api/v1/auth/login` | No | Public | Authenticate user and issue JWT pair | `LoginRequest` | `AuthResponse` | `200 OK`, `401 Unauthorized` |
| `POST` | `/api/v1/auth/refresh` | No | Public | Refresh access token using refresh token | `RefreshTokenRequest` | `AuthResponse` | `200 OK`, `401 Unauthorized` |
| `POST` | `/api/v1/auth/logout` | Yes | Authenticated | Revoke refresh token | `LogoutRequest` | `ApiResponse<Void>` | `200 OK`, `401 Unauthorized` |
| `GET` | `/api/v1/auth/me` | Yes | Authenticated | Fetch authenticated user profile | None | `UserDto` | `200 OK`, `401 Unauthorized` |
| `POST` | `/api/v1/auth/change-password` | Yes | Authenticated | Change user password | `ChangePasswordRequest` | `ApiResponse<Void>` | `200 OK`, `400 Bad Request` |

---

## 3. Tenant Service (Port: 8082)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/storefront/config` | No | Public | Fetch tenant storefront branding & feature config | Query: `tenantCode` | `StorefrontConfigDto` | `200 OK`, `404 Not Found` |
| `GET` | `/api/v1/admin/tenants` | Yes | `ADMIN` | List all registered tenants | Pagination | `PageResponse<TenantDto>` | `200 OK`, `403 Forbidden` |
| `GET` | `/api/v1/admin/tenants/{tenantId}` | Yes | `ADMIN` | Get tenant details by ID | None | `TenantDto` | `200 OK`, `404 Not Found` |
| `POST` | `/api/v1/admin/tenants` | Yes | `ADMIN` | Register new multi-tenant instance | `TenantDto` | `TenantDto` | `201 Created`, `400 Bad Request` |
| `PUT` | `/api/v1/admin/tenants/{tenantId}` | Yes | `ADMIN` | Update tenant information | `TenantDto` | `TenantDto` | `200 OK`, `404 Not Found` |
| `PUT` | `/api/v1/admin/tenants/{tenantId}/branding` | Yes | `ADMIN` | Update tenant branding colors & assets | `TenantBrandingDto` | `TenantBrandingDto` | `200 OK` |
| `GET` | `/api/v1/admin/tenants/{tenantId}/menus` | Yes | `ADMIN` | Fetch tenant navigation menus | None | `List<TenantMenuDto>` | `200 OK` |
| `POST` | `/api/v1/admin/tenants/{tenantId}/menus` | Yes | `ADMIN` | Add navigation menu item | `TenantMenuDto` | `TenantMenuDto` | `201 Created` |

---

## 4. Catalog Service (Port: 8083)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/catalog/products` | No | Public | Search/filter active product catalog | Query params | `PageResponse<ProductDto>` | `200 OK` |
| `GET` | `/api/v1/catalog/products/{id}` | No | Public | Get product details by ID | None | `ProductDto` | `200 OK`, `404 Not Found` |
| `GET` | `/api/v1/catalog/products/slug/{slug}` | No | Public | Get product details by SEO slug | None | `ProductDto` | `200 OK`, `404 Not Found` |
| `GET` | `/api/v1/catalog/categories` | No | Public | Fetch category hierarchy tree | None | `List<CategoryDto>` | `200 OK` |
| `POST` | `/api/v1/catalog/products` | Yes | `SELLER`, `ADMIN` | Create new product draft | `CreateProductRequest` | `ProductDto` | `201 Created` |
| `PUT` | `/api/v1/catalog/products/{id}` | Yes | `SELLER`, `ADMIN` | Update existing product | `UpdateProductRequest` | `ProductDto` | `200 OK`, `403 Forbidden` |
| `DELETE` | `/api/v1/catalog/products/{id}` | Yes | `SELLER`, `ADMIN` | Archive product | None | `ApiResponse<Void>` | `200 OK` |
| `PUT` | `/api/v1/catalog/products/{id}/approve` | Yes | `ADMIN` | Moderate & approve product | None | `ProductDto` | `200 OK` |

---

## 5. Seller Service (Port: 8084)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `POST` | `/api/v1/sellers` | Yes | Authenticated | Register seller application | `RegisterSellerRequest` | `SellerDto` | `201 Created` |
| `GET` | `/api/v1/sellers/me` | Yes | `SELLER` | Get seller profile for current user | None | `SellerDto` | `200 OK` |
| `GET` | `/api/v1/sellers/{id}` | Yes | Authenticated | Get public seller store profile | None | `SellerDto` | `200 OK` |
| `PUT` | `/api/v1/sellers/{id}` | Yes | `SELLER`, `ADMIN` | Update seller store details | `UpdateSellerRequest` | `SellerDto` | `200 OK` |
| `GET` | `/api/v1/admin/sellers` | Yes | `ADMIN` | List seller applications | Pagination | `PageResponse<SellerDto>` | `200 OK` |
| `PUT` | `/api/v1/admin/sellers/{id}/approve` | Yes | `ADMIN` | Approve seller account | None | `SellerDto` | `200 OK` |
| `PUT` | `/api/v1/admin/sellers/{id}/reject` | Yes | `ADMIN` | Reject seller application | Reason | `SellerDto` | `200 OK` |

---

## 6. Inventory Service (Port: 8085)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/inventory/products/{productId}` | No | Public | Check available stock level | None | `InventoryDto` | `200 OK` |
| `POST` | `/api/v1/inventory/adjustments` | Yes | `SELLER`, `ADMIN` | Add or adjust stock quantity | `StockAdjustmentRequest` | `InventoryDto` | `200 OK` |
| `POST` | `/api/v1/inventory/reservations` | Yes | Authenticated | Reserve stock for checkout | `ReserveStockRequest` | `InventoryReservationDto` | `200 OK`, `400 Bad Request` |
| `POST` | `/api/v1/inventory/reservations/{id}/confirm` | Yes | Authenticated | Confirm reservation on payment | None | `InventoryReservationDto` | `200 OK` |
| `POST` | `/api/v1/inventory/reservations/{id}/release` | Yes | Authenticated | Release reservation on payment fail | None | `InventoryReservationDto` | `200 OK` |

---

## 7. Cart Service (Port: 8086)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/cart` | Yes | `CUSTOMER` | Get current customer active cart | None | `CartDto` | `200 OK` |
| `POST` | `/api/v1/cart/items` | Yes | `CUSTOMER` | Add item to cart | `AddToCartRequest` | `CartDto` | `200 OK` |
| `PUT` | `/api/v1/cart/items/{itemId}` | Yes | `CUSTOMER` | Update cart item quantity | `UpdateCartItemRequest` | `CartDto` | `200 OK` |
| `DELETE` | `/api/v1/cart/items/{itemId}` | Yes | `CUSTOMER` | Remove item from cart | None | `CartDto` | `200 OK` |
| `DELETE` | `/api/v1/cart` | Yes | `CUSTOMER` | Clear entire cart | None | `ApiResponse<Void>` | `200 OK` |

---

## 8. Order Service (Port: 8087)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `POST` | `/api/v1/orders` | Yes | `CUSTOMER` | Place multi-seller order | `CreateOrderRequest` | `OrderDto` | `201 Created` |
| `GET` | `/api/v1/orders` | Yes | `CUSTOMER` | List customer order history | Pagination | `PageResponse<OrderDto>` | `200 OK` |
| `GET` | `/api/v1/orders/{id}` | Yes | Authenticated | Get order details | None | `OrderDto` | `200 OK`, `403 Forbidden` |
| `PUT` | `/api/v1/orders/{id}/cancel` | Yes | Authenticated | Cancel order | None | `OrderDto` | `200 OK` |
| `POST` | `/api/v1/orders/{id}/return` | Yes | `CUSTOMER` | Request return for items | `ReturnRequest` | `OrderDto` | `200 OK` |
| `GET` | `/api/v1/seller/orders` | Yes | `SELLER` | List seller fulfillment orders | Pagination | `PageResponse<FulfillmentGroupDto>` | `200 OK` |

---

## 9. Payment Service (Port: 8088)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `POST` | `/api/v1/payments/initiate` | Yes | `CUSTOMER` | Initiate payment transaction | `InitiatePaymentRequest` | `PaymentTransactionDto` | `201 Created` |
| `POST` | `/api/v1/payments/{id}/verify` | Yes | `CUSTOMER` | Verify payment status from gateway | `VerifyPaymentRequest` | `PaymentTransactionDto` | `200 OK` |
| `GET` | `/api/v1/payments/{id}` | Yes | Authenticated | Get payment transaction details | None | `PaymentTransactionDto` | `200 OK` |
| `POST` | `/api/v1/payments/{id}/refund` | Yes | `ADMIN` | Process refund transaction | `RefundRequest` | `PaymentTransactionDto` | `200 OK` |

---

## 10. Consultation Service (Port: 8089)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/doctors` | No | Public | List verified doctors | Pagination | `PageResponse<DoctorDto>` | `200 OK` |
| `GET` | `/api/v1/doctors/{id}` | No | Public | Get doctor profile | None | `DoctorDto` | `200 OK` |
| `GET` | `/api/v1/doctors/{id}/availability` | No | Public | Get doctor weekly schedule | None | `List<DoctorAvailabilityDto>` | `200 OK` |
| `POST` | `/api/v1/consultations/appointments` | Yes | `CUSTOMER` | Book doctor consultation | `BookAppointmentRequest` | `AppointmentDto` | `201 Created` |
| `GET` | `/api/v1/consultations/appointments` | Yes | `CUSTOMER` | Customer appointments history | Pagination | `PageResponse<AppointmentDto>` | `200 OK` |
| `GET` | `/api/v1/consultations/appointments/{id}` | Yes | Authenticated | Get appointment details | None | `AppointmentDto` | `200 OK`, `403 Forbidden` |
| `PUT` | `/api/v1/consultations/appointments/{id}/cancel` | Yes | Authenticated | Cancel appointment | None | `AppointmentDto` | `200 OK` |
| `POST` | `/api/v1/consultations/{id}/prescriptions` | Yes | `DOCTOR` | Issue digital prescription | `CreatePrescriptionRequest` | `PrescriptionDto` | `201 Created` |
| `GET` | `/api/v1/consultations/{id}/prescription` | Yes | Authenticated | View prescription | None | `PrescriptionDto` | `200 OK`, `403 Forbidden` |
| `PUT` | `/api/v1/admin/doctors/{doctorId}/verify` | Yes | `ADMIN` | Verify/reject doctor credentials | Status param | `DoctorDto` | `200 OK` |

---

## 11. Notification Service (Port: 8090)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `GET` | `/api/v1/notifications` | Yes | Authenticated | Fetch user in-app notifications | Pagination | `PageResponse<NotificationDto>` | `200 OK` |
| `PUT` | `/api/v1/notifications/{id}/read` | Yes | Authenticated | Mark notification as read | None | `NotificationDto` | `200 OK` |
| `POST` | `/api/v1/notifications/send` | Yes | `ADMIN` | Dispatch custom notification | `SendNotificationRequest` | `NotificationDto` | `201 Created` |
| `GET` | `/api/v1/notifications/preferences` | Yes | Authenticated | Get notification channel preferences | None | `NotificationPreferenceDto` | `200 OK` |
| `PUT` | `/api/v1/notifications/preferences` | Yes | Authenticated | Update channel preferences | `NotificationPreferenceDto` | `NotificationPreferenceDto` | `200 OK` |

---

## 12. Admin Service (Port: 8091)

| Method | Path | Auth Required | Roles | Purpose | Request Body | Response | Status Codes |
|---|---|---|---|---|---|---|---|
| `POST` | `/api/v1/admin/audit-logs` | Yes | `ADMIN` | Log administrative audit entry | `CreateAuditLogRequest` | `AuditLogDto` | `201 Created` |
| `GET` | `/api/v1/admin/audit-logs` | Yes | `ADMIN` | Query platform audit log trail | Pagination | `PageResponse<AuditLogDto>` | `200 OK` |
| `POST` | `/api/v1/admin/moderations` | Yes | `ADMIN` | Record moderation action | `CreateModerationRequest` | `ModerationRecordDto` | `201 Created` |
| `GET` | `/api/v1/admin/moderations` | Yes | `ADMIN` | List moderation actions | Pagination | `PageResponse<ModerationRecordDto>` | `200 OK` |
