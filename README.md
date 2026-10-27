# AuraMarket Microservices Backend

AuraMarket is an enterprise-grade multi-tenant microservices backend platform engineered using **Java 21**, **Spring Boot 3.3.3**, **Spring Cloud Gateway**, **PostgreSQL**, **Redis**, **Apache Kafka**, **Flyway**, and **Docker**.

> **Note on Root Architecture**:
> This backend resides entirely inside `E:\AuraMarket\AuraMarket_backend`.
> The frontend is preserved untouched under `E:\AuraMarket\ECommerce\AuraMarket`.

---

## 1. Microservice Matrix & Database Ownership

| Microservice | Local Port | Database Name | Primary Domain |
| :--- | :--- | :--- | :--- |
| `api-gateway` | **8080** | N/A (Stateless Gateway) | Edge Routing & CORS for Angular Frontend |
| `identity-service` | **8081** | `auramarket_identity` | Users, JWT Auth, Roles (CUSTOMER, SELLER, DOCTOR, ADMIN) |
| `tenant-service` | **8082** | `auramarket_tenant` | Multi-Tenant Engine, Dynamic Theme, Navigation & Feature Flags |
| `catalog-service` | **8083** | `auramarket_catalog` | Product Catalog, Categories, Brands, Metadata |
| `seller-service` | **8084** | `auramarket_seller` | Seller Accounts, Store Profiles, Onboarding |
| `inventory-service` | **8085** | `auramarket_inventory` | Real-time Stock, Reservations, Damaged Stock & Adjustments |
| `cart-service` | **8086** | `auramarket_cart` | Shopping Carts & Multi-Seller Cart Line Items |
| `order-service` | **8087** | `auramarket_order` | Single Customer Checkout with Multi-Seller Fulfillment Groups |
| `payment-service` | **8088** | `auramarket_payment` | Payment Abstraction, Intent Status, Refund Tracking |
| `consultation-service` | **8089** | `auramarket_consultation` | Doctor Profiles, Schedules, Appointments, Consultations |
| `notification-service` | **8090** | `auramarket_notification` | In-app, Email, SMS & Kafka Event Consumers |
| `admin-service` | **8091** | `auramarket_admin` | Back-Office Moderation, Seller Approval, Audit Logs |

---

## 2. Infrastructure Setup (Docker Compose)

Start PostgreSQL, Redis, Apache Kafka, and Kafka UI using Docker Compose:

```bash
cd E:\AuraMarket\AuraMarket_backend
docker compose up -d
```

### Infrastructure Endpoints
- **PostgreSQL**: `localhost:5432` (user: `auramarket_user`, password: `auramarket_secure_password_2026`)
- **Redis**: `localhost:6379`
- **Kafka**: `localhost:9092`
- **Kafka UI**: `http://localhost:8089`

---

## 3. How to Start Microservices Locally

### Option A: Using Maven Command Line

#### 1. Start Tenant Service (Port 8082)
```bash
cd E:\AuraMarket\AuraMarket_backend\tenant-service
mvn spring-boot:run
```

#### 2. Start API Gateway (Port 8080)
```bash
cd E:\AuraMarket\AuraMarket_backend\api-gateway
mvn spring-boot:run
```

---

## 4. First Working Business Capability (Storefront Config API)

Angular frontend requests dynamic storefront branding via API Gateway:

```http
GET http://localhost:8080/api/v1/storefront/config?tenantCode=AURA_AYURVEDA
```

### Sample JSON Response

```json
{
  "tenant": {
    "code": "AURA_AYURVEDA",
    "name": "Aura Ayurveda",
    "businessType": "AYURVEDA"
  },
  "branding": {
    "logoUrl": "/assets/images/logo.svg",
    "faviconUrl": "/assets/images/favicon.ico",
    "primaryColor": "#2F6B4F",
    "secondaryColor": "#8BAF91",
    "backgroundColor": "#F8FAF7",
    "fontFamily": "Inter, sans-serif"
  },
  "currency": {
    "code": "INR",
    "locale": "en-IN"
  },
  "features": {
    "PRODUCTS": true,
    "SELLERS": true,
    "ORDERS": true,
    "INVENTORY": true,
    "WISHLIST": true,
    "CONSULTATION": true,
    "DOCTORS": true,
    "PRESCRIPTIONS": true,
    "RETURNS": true
  },
  "menus": [
    { "id": 1, "label": "Home", "route": "/home", "icon": "home", "displayOrder": 1, "isVisible": true },
    { "id": 2, "label": "Ayurvedic Products", "route": "/category/products", "icon": "leaf", "displayOrder": 2, "isVisible": true },
    { "id": 9, "label": "Ayurveda Doctors", "route": "/doctors", "icon": "stethoscope", "displayOrder": 9, "isVisible": true },
    { "id": 10, "label": "Consultation", "route": "/consultation", "icon": "calendar", "displayOrder": 10, "isVisible": true }
  ]
}
```

---

## 5. Verification & Testing

Every service can be verified independently:

```bash
cd E:\AuraMarket\AuraMarket_backend\tenant-service
mvn clean verify
```

```bash
cd E:\AuraMarket\AuraMarket_backend\api-gateway
mvn clean verify
```

---

## 6. Architecture & DSA Documentation

Detailed architectural specs are stored in the `/docs` folder:
- [System Architecture](file:///E:/AuraMarket/AuraMarket_backend/docs/architecture/system-architecture.md)
- [Service Boundaries](file:///E:/AuraMarket/AuraMarket_backend/docs/architecture/service-boundaries.md)
- [Database-per-Service Rules](file:///E:/AuraMarket/AuraMarket_backend/docs/architecture/database-per-service.md)
- [Kafka Event Architecture](file:///E:/AuraMarket/AuraMarket_backend/docs/events/kafka-events.md)
- [DSA Practicals](file:///E:/AuraMarket/AuraMarket_backend/docs/dsa/README.md)
