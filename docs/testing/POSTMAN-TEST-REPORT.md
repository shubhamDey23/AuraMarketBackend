# Postman API Collection Test Report

## 1. Overview
The Postman test suite provides comprehensive API verification for all 12 microservices, covering positive execution paths, multi-step integration scenarios, and security negative tests.

- **Collection File**: `postman/AuraMarket-Backend.postman_collection.json` (Postman Collection v2.1.0)
- **Environment File**: `postman/AuraMarket-Local.postman_environment.json`
- **Total Request Definitions**: 25+ Endpoint Test Definitions across 14 Folders.

## 2. Test Execution & Folder Coverage

| Folder # | Folder Name | Test Focus | Assertion Checks | Status |
|---|---|---|---|---|
| `01` | Health | Service actuators health checks | Status 200, `status == UP` | **PASS** |
| `02` | Authentication | Register, Login, Refresh | JWT token extraction & variables setting | **PASS** |
| `03` | Tenant | Storefront configuration | Dynamic features, branding, menus | **PASS** |
| `04` | Catalog | Catalog search & product details | Pagination, product structures | **PASS** |
| `05` | Seller | Seller store profile | Authentication & seller details | **PASS** |
| `06` | Inventory | Stock checks & reservations | Quantity checks & status | **PASS** |
| `07` | Cart | Customer cart operations | Multi-seller items, cart subtotals | **PASS** |
| `08` | Orders | Order checkout & history | Multi-seller fulfillment groups | **PASS** |
| `09` | Payments | Payment initiation & verification | Intent status & transactions | **PASS** |
| `10` | Consultation | Doctors & Appointments | Verified doctors & schedules | **PASS** |
| `11` | Notifications | In-app notifications | Recipient notification list | **PASS** |
| `12` | Admin | Audit log trail | Action records & resource IDs | **PASS** |
| `13` | Security Tests | Role & unauthenticated checks | Assert 401 & 403 status codes | **PASS** |
| `14` | Integration Flows | End-to-end purchasing | Dynamic variable passing across steps | **PASS** |
