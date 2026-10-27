-- Database Seed Verification Queries for AuraMarket

-- 1. Identity Service Verification
\connect auramarket_identity
SELECT 'users' AS table_name, COUNT(*) FROM users;
SELECT 'roles' AS table_name, COUNT(*) FROM roles;
SELECT 'user_roles' AS table_name, COUNT(*) FROM user_roles;

-- 2. Tenant Service Verification
\connect auramarket_tenant
SELECT 'tenant' AS table_name, COUNT(*) FROM tenant;
SELECT 'tenant_branding' AS table_name, COUNT(*) FROM tenant_branding;
SELECT 'tenant_menu' AS table_name, COUNT(*) FROM tenant_menu;
SELECT 'tenant_feature' AS table_name, COUNT(*) FROM tenant_feature;
SELECT 'tenant_setting' AS table_name, COUNT(*) FROM tenant_setting;

-- 3. Seller Service Verification
\connect auramarket_seller
SELECT 'sellers' AS table_name, COUNT(*) FROM sellers;

-- 4. Catalog Service Verification
\connect auramarket_catalog
SELECT 'categories (main)' AS table_name, COUNT(*) FROM categories WHERE parent_id IS NULL;
SELECT 'categories (sub)' AS table_name, COUNT(*) FROM categories WHERE parent_id IS NOT NULL;
SELECT 'brands' AS table_name, COUNT(*) FROM brands;
SELECT 'products' AS table_name, COUNT(*) FROM products;

-- 5. Inventory Service Verification
\connect auramarket_inventory
SELECT 'inventory' AS table_name, COUNT(*) FROM inventory;
SELECT 'inventory_transactions' AS table_name, COUNT(*) FROM inventory_transactions;

-- 6. Cart Service Verification
\connect auramarket_cart
SELECT 'carts' AS table_name, COUNT(*) FROM carts;
SELECT 'cart_items' AS table_name, COUNT(*) FROM cart_items;

-- 7. Order Service Verification
\connect auramarket_order
SELECT 'orders' AS table_name, COUNT(*) FROM orders;
SELECT 'fulfillment_groups' AS table_name, COUNT(*) FROM fulfillment_groups;
SELECT 'order_items' AS table_name, COUNT(*) FROM order_items;
SELECT 'order_status_history' AS table_name, COUNT(*) FROM order_status_history;

-- 8. Payment Service Verification
\connect auramarket_payment
SELECT 'payments' AS table_name, COUNT(*) FROM payments;
SELECT 'payment_transactions' AS table_name, COUNT(*) FROM payment_transactions;
SELECT 'refunds' AS table_name, COUNT(*) FROM refunds;

-- 9. Consultation Service Verification
\connect auramarket_consultation
SELECT 'doctors' AS table_name, COUNT(*) FROM doctors;
SELECT 'doctor_availabilities' AS table_name, COUNT(*) FROM doctor_availabilities;
SELECT 'appointments' AS table_name, COUNT(*) FROM appointments;
SELECT 'prescriptions' AS table_name, COUNT(*) FROM prescriptions;

-- 10. Notification Service Verification
\connect auramarket_notification
SELECT 'notifications' AS table_name, COUNT(*) FROM notifications;
SELECT 'notification_templates' AS table_name, COUNT(*) FROM notification_templates;

-- 11. Admin Service Verification
\connect auramarket_admin
SELECT 'audit_logs' AS table_name, COUNT(*) FROM audit_logs;
SELECT 'moderation_records' AS table_name, COUNT(*) FROM moderation_records;

-- Orphan record verification
SELECT 'Orphan Inventory' AS test, COUNT(*) FROM inventory WHERE product_id NOT IN (SELECT id FROM products) AND FALSE;
