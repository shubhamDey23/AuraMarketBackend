# Tenant Service API & Multi-Tenant Architecture Specification

## Overview

The `tenant-service` (Port 8082) manages all tenant-level business configurations for the AuraMarket microservices platform. It enables the Angular storefront to become generic, dynamic, and configuration-driven without hardcoding store names, logos, colors, menus, or enabled features.

---

## Data Architecture & Storage

- **Primary Database**: PostgreSQL (`auramarket_tenant`) - Single Source of Truth
- **Cache Layer**: Redis (`tenant:storefront:{tenantCode}`) - TTL: 600s (10 min)
- **Cache Strategy**: Cache-aside with automatic eviction on admin updates and graceful fallback to PostgreSQL if Redis is unavailable.

---

## 1. Public Storefront API

### GET `/api/v1/storefront/config`

Retrieves storefront configuration (branding, menus, features, and settings) for the given tenant code.

- **Authentication**: None (Public)
- **Query Parameters**:
  - `tenantCode` (string, optional, default: `AURA_AYURVEDA`)
- **Headers**: None required
- **Response**: `200 OK`

```json
{
  "tenant": {
    "id": "e8d7c6b5-a493-4210-9f8e-7d6c5b4a3f2e",
    "tenantCode": "AURA_AYURVEDA",
    "name": "Aura Ayurveda",
    "slug": "aura-ayurveda",
    "businessType": "AYURVEDA",
    "status": "ACTIVE",
    "currency": "INR",
    "locale": "en-IN",
    "timezone": "Asia/Kolkata"
  },
  "branding": {
    "logoUrl": "/assets/tenants/aura-ayurveda/logo.svg",
    "faviconUrl": "/assets/tenants/aura-ayurveda/favicon.ico",
    "primaryColor": "#2F6B4F",
    "secondaryColor": "#8BAF91",
    "accentColor": "#C69A45",
    "backgroundColor": "#F8FAF7",
    "surfaceColor": "#FFFFFF",
    "textColor": "#1F2933",
    "fontFamily": "Inter, sans-serif",
    "borderRadius": "8px"
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
    "PRESCRIPTIONS": true,
    "HEALTH_GUIDES": true
  },
  "menus": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "parentId": null,
      "label": "Home",
      "route": "/",
      "icon": "home",
      "displayOrder": 1,
      "isVisible": true,
      "requiredFeature": null,
      "openInNewTab": false,
      "children": []
    },
    {
      "id": "b2c3d4e5-f6a7-8901-bcde-f23456789012",
      "parentId": null,
      "label": "Ayurvedic Products",
      "route": "/products",
      "icon": "package",
      "displayOrder": 2,
      "isVisible": true,
      "requiredFeature": "PRODUCTS",
      "openInNewTab": false,
      "children": []
    }
  ],
  "settings": {
    "STORE_EMAIL": "support@auraayurveda.com",
    "STORE_PHONE": "+91-1800-123-4567"
  }
}
```

---

## 2. Admin Tenant Management APIs

All endpoints require `ROLE_ADMIN` authentication.

### `GET /api/v1/admin/tenants`
Lists all registered tenants.

### `GET /api/v1/admin/tenants/{tenantId}`
Gets details for a single tenant by UUID.

### `POST /api/v1/admin/tenants`
Creates a new tenant.
- **Request Body**:
```json
{
  "tenantCode": "ELECTRONICS_STORE",
  "name": "TechWorld",
  "slug": "techworld",
  "businessType": "ELECTRONICS",
  "defaultCurrency": "INR",
  "defaultLocale": "en-IN",
  "timezone": "Asia/Kolkata"
}
```

### `PUT /api/v1/admin/tenants/{tenantId}`
Updates tenant details. Invalidates tenant Redis cache.

### `PUT /api/v1/admin/tenants/{tenantId}/branding`
Updates tenant branding colors, logo, and fonts. Invalidates tenant Redis cache.
- **Request Body**:
```json
{
  "logoUrl": "/assets/tenants/techworld/logo.svg",
  "primaryColor": "#0F172A",
  "secondaryColor": "#3B82F6",
  "accentColor": "#F59E0B"
}
```

### `GET /api/v1/admin/tenants/{tenantId}/menus`
Gets all menu items for the specified tenant.

### `POST /api/v1/admin/tenants/{tenantId}/menus`
Creates a new menu item. Invalidates tenant Redis cache.

### `PUT /api/v1/admin/tenants/{tenantId}/menus/{menuId}`
Updates an existing menu item. Invalidates tenant Redis cache.

### `DELETE /api/v1/admin/tenants/{tenantId}/menus/{menuId}`
Deletes a menu item. Invalidates tenant Redis cache.

### `GET /api/v1/admin/tenants/{tenantId}/features`
Gets feature flags for tenant.

### `PUT /api/v1/admin/tenants/{tenantId}/features/{featureCode}`
Toggles or updates feature flag. Invalidates tenant Redis cache.

### `GET /api/v1/admin/tenants/{tenantId}/settings`
Gets tenant key/value settings.

### `PUT /api/v1/admin/tenants/{tenantId}/settings/{settingKey}`
Updates tenant setting key/value. Invalidates tenant Redis cache.

---

## 3. Cache Eviction Endpoint

### POST `/api/v1/tenants/{tenantCode}/cache/invalidate`
Manually evicts Redis cache entry for the specified tenant code.
