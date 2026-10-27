# AuraMarket Postman API Test Suite

This folder contains the complete Postman v2.1.0 Collection and Environment files for executing automated integration and security tests against the AuraMarket microservices system.

## Collection Files
- `AuraMarket-Backend.postman_collection.json`: Collection with 14 organized folders containing API endpoints, assertions, and automated variable extraction scripts.
- `AuraMarket-Local.postman_environment.json`: Postman environment configuration containing base URLs, tenant codes, and dynamic token placeholders.

## Folders Included
1. `01 - Health`
2. `02 - Authentication`
3. `03 - Tenant`
4. `04 - Catalog`
5. `05 - Seller`
6. `06 - Inventory`
7. `07 - Cart`
8. `08 - Orders`
9. `09 - Payments`
10. `10 - Consultation`
11. `11 - Notifications`
12. `12 - Admin`
13. `13 - Security Tests`
14. `14 - Integration Flows`

## How to Import & Run
1. Open Postman.
2. Click **Import** -> Select `AuraMarket-Backend.postman_collection.json` and `AuraMarket-Local.postman_environment.json`.
3. Select **AuraMarket Local Environment** from the top-right environment selector.
4. Run the Collection via Postman Collection Runner or Newman CLI:
   ```bash
   newman run postman/AuraMarket-Backend.postman_collection.json -e postman/AuraMarket-Local.postman_environment.json
   ```
