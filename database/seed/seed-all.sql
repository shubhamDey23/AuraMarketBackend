# AuraMarket Seed Master SQL Script
-- Includes all microservice seeds sequentially for manual execution or psql import

\echo 'Seeding Identity Service...'
\connect auramarket_identity
\i database/seed/identity/seed.sql

\echo 'Seeding Tenant Service...'
\connect auramarket_tenant
\i database/seed/tenant/seed.sql

\echo 'Seeding Seller Service...'
\connect auramarket_seller
\i database/seed/seller/seed.sql

\echo 'Seeding Catalog Service...'
\connect auramarket_catalog
\i database/seed/catalog/seed.sql

\echo 'Seeding Inventory Service...'
\connect auramarket_inventory
\i database/seed/inventory/seed.sql

\echo 'Seeding Cart Service...'
\connect auramarket_cart
\i database/seed/cart/seed.sql

\echo 'Seeding Order Service...'
\connect auramarket_order
\i database/seed/order/seed.sql

\echo 'Seeding Payment Service...'
\connect auramarket_payment
\i database/seed/payment/seed.sql

\echo 'Seeding Consultation Service...'
\connect auramarket_consultation
\i database/seed/consultation/seed.sql

\echo 'Seeding Notification Service...'
\connect auramarket_notification
\i database/seed/notification/seed.sql

\echo 'Seeding Admin Service...'
\connect auramarket_admin
\i database/seed/admin/seed.sql

\echo 'Seeding process complete!'
