# AuraMarket Frontend API Contract Specification

This document provides the definitive contract specification for connecting the Angular frontend (`E:\AuraMarket\ECommerce\AuraMarket`) to the AuraMarket microservices backend via the API Gateway (`http://localhost:8080`).

---

## 1. Authentication Header Standard
All authenticated requests must supply:
```http
Authorization: Bearer <accessToken>
X-Tenant-Id: <tenantCode>
```

---

## 2. Dynamic Storefront Initialization
**Endpoint**: `GET /api/v1/storefront/config?tenantCode=AURA_AYURVEDA`

**Response (`200 OK`)**:
```json
{
  "success": true,
  "message": "Storefront configuration retrieved successfully",
  "data": {
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
      "fontFamily": "Inter, sans-serif"
    },
    "features": {
      "PRODUCTS": true,
      "SELLERS": true,
      "ORDERS": true,
      "INVENTORY": true,
      "WISHLIST": true,
      "CART": true,
      "CHECKOUT": true,
      "PAYMENTS": true,
      "RETURNS": true,
      "CONSULTATION": true,
      "DOCTORS": true,
      "PRESCRIPTIONS": true
    },
    "menus": [
      { "id": "m1", "label": "Home", "route": "/home", "icon": "home", "displayOrder": 1, "isVisible": true },
      { "id": "m2", "label": "Products", "route": "/products", "icon": "package", "displayOrder": 2, "isVisible": true },
      { "id": "m3", "label": "Ayurvedic Doctors", "route": "/doctors", "icon": "stethoscope", "displayOrder": 3, "isVisible": true }
    ]
  }
}
```

---

## 3. Product Catalog APIs

### Search Products
**Endpoint**: `GET /api/v1/catalog/products?page=0&size=12`

**Response (`200 OK`)**:
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": "p0000001-0000-0000-0000-000000000001",
        "tenantId": "AURA_AYURVEDA",
        "name": "Organic Ashwagandha Powder",
        "slug": "organic-ashwagandha-powder",
        "price": 499.00,
        "discountPrice": 399.00,
        "sku": "ASHWA-POWDER-500G",
        "status": "ACTIVE"
      }
    ],
    "page": 0,
    "size": 12,
    "totalElements": 1,
    "totalPages": 1,
    "last": true
  }
}
```

---

## 4. Shopping Cart & Checkout APIs

### Add Item to Cart
**Endpoint**: `POST /api/v1/cart/items`

**Request Body**:
```json
{
  "productId": "p0000001-0000-0000-0000-000000000001",
  "sellerId": "s0000001-0000-0000-0000-000000000001",
  "quantity": 2
}
```

---

## 5. Doctor Consultations & Digital Prescriptions

### List Doctors
**Endpoint**: `GET /api/v1/doctors`

**Response (`200 OK`)**:
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": "e0000001-0000-0000-0000-000000000001",
        "name": "Dr. Vaidya Ayush",
        "specializations": "Ayurveda, Panchakarma",
        "consultationFee": 500.00,
        "verificationStatus": "VERIFIED"
      }
    ]
  }
}
```
