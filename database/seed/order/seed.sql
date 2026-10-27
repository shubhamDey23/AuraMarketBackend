-- Order Service Seed Data (auramarket_order)

-- 1. Orders
INSERT INTO orders (id, tenant_id, customer_id, order_number, status, total_amount, tax_amount, shipping_amount, discount_amount, currency, shipping_address_json, billing_address_json) VALUES
-- Order 1: DELIVERED (Multi-seller: Aarogya Herbals & Prakriti Wellness)
('o0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', 'ORD-2026-0001', 'DELIVERED', 1297.00, 58.00, 50.00, 0.00, 'INR', 
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}'),

-- Order 2: PROCESSING
('o0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', 'ORD-2026-0002', 'PROCESSING', 874.00, 40.00, 50.00, 0.00, 'INR',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}'),

-- Order 3: SHIPPED
('o0000001-0000-0000-0000-000000000003', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', 'ORD-2026-0003', 'SHIPPED', 610.00, 30.00, 50.00, 0.00, 'INR',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}'),

-- Order 4: PENDING
('o0000001-0000-0000-0000-000000000004', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', 'ORD-2026-0004', 'PENDING', 499.00, 25.00, 50.00, 0.00, 'INR',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}'),

-- Order 5: CANCELLED
('o0000001-0000-0000-0000-000000000005', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', 'ORD-2026-0005', 'CANCELLED', 349.00, 15.00, 50.00, 0.00, 'INR',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}',
 '{"fullName":"Demo Customer","street":"108 Veda Marg","city":"Bengaluru","state":"Karnataka","postalCode":"560001","country":"India","phone":"9876543290"}')
ON CONFLICT (id) DO UPDATE SET status = EXCLUDED.status, total_amount = EXCLUDED.total_amount;

-- 2. Fulfillment Groups
INSERT INTO fulfillment_groups (id, order_id, seller_id, status, shipping_method, tracking_number, carrier) VALUES
-- Order 1: Seller 1 fulfillment group (Aarogya Herbals)
('fg000001-0000-0000-0000-000000000001', 'o0000001-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222', 'DELIVERED', 'Standard Express', 'AURA-TRK-1001', 'BlueDart'),
-- Order 1: Seller 2 fulfillment group (Prakriti Wellness)
('fg000001-0000-0000-0000-000000000002', 'o0000001-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222223', 'DELIVERED', 'Standard Express', 'AURA-TRK-1002', 'DTDC'),

-- Order 2: Seller 1 fulfillment group
('fg000001-0000-0000-0000-000000000003', 'o0000001-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', 'PROCESSING', 'Standard Express', NULL, 'Delhivery'),

-- Order 3: Seller 3 fulfillment group (Sattva Ayurveda)
('fg000001-0000-0000-0000-000000000004', 'o0000001-0000-0000-0000-000000000003', '22222222-2222-2222-2222-222222222224', 'SHIPPED', 'Standard Express', 'AURA-TRK-1003', 'India Post')
ON CONFLICT (id) DO UPDATE SET status = EXCLUDED.status;

-- 3. Order Items
INSERT INTO order_items (id, order_id, fulfillment_group_id, product_id, seller_id, product_name, sku, quantity, unit_price, subtotal) VALUES
-- Order 1 items
('oi000001-0000-0000-0000-000000000001', 'o0000001-0000-0000-0000-000000000001', 'fg000001-0000-0000-0000-000000000001', 'e0000001-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222', 'Organic Ashwagandha Extract Capsules 60s', 'AYU-ASHW-60', 1, 449.00, 449.00),
('oi000001-0000-0000-0000-000000000002', 'o0000001-0000-0000-0000-000000000001', 'fg000001-0000-0000-0000-000000000001', 'e0000001-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', 'Organic Triphala Churna 250g', 'AYU-TRIP-250', 1, 299.00, 299.00),
('oi000001-0000-0000-0000-000000000003', 'o0000001-0000-0000-0000-000000000001', 'fg000001-0000-0000-0000-000000000002', 'e0000001-0000-0000-0000-000000000003', '22222222-2222-2222-2222-222222222223', 'Turmeric Curcumin Herbal Capsules', 'AYU-TURM-60', 1, 441.00, 441.00),

-- Order 2 items
('oi000001-0000-0000-0000-000000000004', 'o0000001-0000-0000-0000-000000000002', 'fg000001-0000-0000-0000-000000000003', 'e0000001-0000-0000-0000-000000000007', '22222222-2222-2222-2222-222222222222', 'Intensive Bhringraj Hair Oil 200ml', 'AYU-BHRING-200', 1, 425.00, 425.00),
('oi000001-0000-0000-0000-000000000005', 'o0000001-0000-0000-0000-000000000002', 'fg000001-0000-0000-0000-000000000003', 'e0000001-0000-0000-0000-000000000016', '22222222-2222-2222-2222-222222222222', 'Giloy (Guduchi) Immunity Tablets 60s', 'AYU-GILO-60', 1, 249.00, 249.00),

-- Order 3 items
('oi000001-0000-0000-0000-000000000006', 'o0000001-0000-0000-0000-000000000003', 'fg000001-0000-0000-0000-000000000004', 'e0000001-0000-0000-0000-000000000004', '22222222-2222-2222-2222-222222222224', 'Purifying Neem & Tea Tree Face Wash 150ml', 'AYU-NEEM-FW', 1, 240.00, 240.00),
('oi000001-0000-0000-0000-000000000007', 'o0000001-0000-0000-0000-000000000003', 'fg000001-0000-0000-0000-000000000004', 'e0000001-0000-0000-0000-000000000005', '22222222-2222-2222-2222-222222222224', 'Pure Organic Aloe Vera Gel 200g', 'AYU-ALOE-200', 1, 290.00, 290.00)
ON CONFLICT (id) DO NOTHING;

-- 4. Status History
INSERT INTO order_status_history (id, order_id, old_status, new_status, notes) VALUES
('h0000001-0000-0000-0000-000000000001', 'o0000001-0000-0000-0000-000000000001', 'PENDING', 'CONFIRMED', 'Payment verified'),
('h0000001-0000-0000-0000-000000000002', 'o0000001-0000-0000-0000-000000000001', 'CONFIRMED', 'DELIVERED', 'Delivered successfully')
ON CONFLICT (id) DO NOTHING;
