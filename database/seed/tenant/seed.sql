-- Tenant Service Seed Data (auramarket_tenant)

-- Seed Tenants
INSERT INTO tenant (id, tenant_code, name, slug, business_type, status, default_currency, default_locale, timezone)
VALUES 
('11111111-1111-1111-1111-111111111111', 'AURA_AYURVEDA', 'Aura Ayurveda', 'aura-ayurveda', 'AYURVEDA', 'ACTIVE', 'INR', 'en-IN', 'Asia/Kolkata'),
('22222222-2222-2222-2222-222222222222', 'FASHION_STORE', 'Demo Fashion Store', 'demo-fashion-store', 'FASHION', 'ACTIVE', 'INR', 'en-IN', 'Asia/Kolkata')
ON CONFLICT (id) DO UPDATE SET 
  name = EXCLUDED.name, 
  slug = EXCLUDED.slug, 
  business_type = EXCLUDED.business_type, 
  status = EXCLUDED.status;

-- Seed Branding
INSERT INTO tenant_branding (id, tenant_id, logo_url, favicon_url, primary_color, secondary_color, accent_color, background_color, surface_color, text_color, font_family, border_radius)
VALUES 
('11111111-2222-3333-4444-555555555555', '11111111-1111-1111-1111-111111111111', '/assets/tenants/aura-ayurveda/logo.svg', '/assets/tenants/aura-ayurveda/favicon.ico', '#2F6B4F', '#8BAF91', '#C69A45', '#F8FAF7', '#FFFFFF', '#1F2933', 'Inter', '8px'),
('22222222-3333-4444-5555-666666666666', '22222222-2222-2222-2222-222222222222', '/assets/tenants/fashion-store/logo.svg', '/assets/tenants/fashion-store/favicon.ico', '#D946EF', '#8B5CF6', '#F59E0B', '#FAF5FF', '#FFFFFF', '#1E1B4B', 'Inter', '12px')
ON CONFLICT (id) DO UPDATE SET 
  primary_color = EXCLUDED.primary_color,
  secondary_color = EXCLUDED.secondary_color,
  accent_color = EXCLUDED.accent_color,
  background_color = EXCLUDED.background_color,
  surface_color = EXCLUDED.surface_color,
  text_color = EXCLUDED.text_color;

-- Seed Menus for AURA_AYURVEDA
INSERT INTO tenant_menu (id, tenant_id, label, route, icon, display_order, is_visible) VALUES
('a0000001-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'Home', '/', 'home', 1, true),
('a0000001-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'Ayurvedic Products', '/products', 'leaf', 2, true),
('a0000001-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 'Medicines', '/products/medicines', 'capsule', 3, true),
('a0000001-0000-0000-0000-000000000004', '11111111-1111-1111-1111-111111111111', 'Wellness', '/products/wellness', 'smile', 4, true),
('a0000001-0000-0000-0000-000000000005', '11111111-1111-1111-1111-111111111111', 'Personal Care', '/products/personal-care', 'user', 5, true),
('a0000001-0000-0000-0000-000000000006', '11111111-1111-1111-1111-111111111111', 'Herbal Oils', '/products/herbal-oils', 'droplet', 6, true),
('a0000001-0000-0000-0000-000000000007', '11111111-1111-1111-1111-111111111111', 'Skin & Hair', '/products/skin-hair', 'sparkles', 7, true),
('a0000001-0000-0000-0000-000000000008', '11111111-1111-1111-1111-111111111111', 'Nutrition', '/products/nutrition', 'heart', 8, true)
ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, route = EXCLUDED.route, display_order = EXCLUDED.display_order;

INSERT INTO tenant_menu (id, tenant_id, label, route, icon, display_order, is_visible, required_feature) VALUES
('a0000001-0000-0000-0000-000000000009', '11111111-1111-1111-1111-111111111111', 'Ayurveda Doctors', '/doctors', 'stethoscope', 9, true, 'DOCTORS'),
('a0000001-0000-0000-0000-000000000010', '11111111-1111-1111-1111-111111111111', 'Consultation', '/consultation', 'calendar', 10, true, 'CONSULTATION'),
('a0000001-0000-0000-0000-000000000011', '11111111-1111-1111-1111-111111111111', 'Health Guides', '/health-guides', 'book-open', 11, true, 'HEALTH_GUIDES'),
('a0000001-0000-0000-0000-000000000012', '11111111-1111-1111-1111-111111111111', 'Offers', '/offers', 'tag', 12, true, null)
ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, route = EXCLUDED.route, display_order = EXCLUDED.display_order;

-- Seed Features for AURA_AYURVEDA
INSERT INTO tenant_feature (id, tenant_id, feature_code, is_enabled) VALUES
('f0000001-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'PRODUCTS', true),
('f0000001-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'SELLERS', true),
('f0000001-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 'ORDERS', true),
('f0000001-0000-0000-0000-000000000004', '11111111-1111-1111-1111-111111111111', 'INVENTORY', true),
('f0000001-0000-0000-0000-000000000005', '11111111-1111-1111-1111-111111111111', 'WISHLIST', true),
('f0000001-0000-0000-0000-000000000006', '11111111-1111-1111-1111-111111111111', 'CART', true),
('f0000001-0000-0000-0000-000000000007', '11111111-1111-1111-1111-111111111111', 'CHECKOUT', true),
('f0000001-0000-0000-0000-000000000008', '11111111-1111-1111-1111-111111111111', 'PAYMENTS', true),
('f0000001-0000-0000-0000-000000000009', '11111111-1111-1111-1111-111111111111', 'RETURNS', true),
('f0000001-0000-0000-0000-000000000010', '11111111-1111-1111-1111-111111111111', 'CONSULTATION', true),
('f0000001-0000-0000-0000-000000000011', '11111111-1111-1111-1111-111111111111', 'DOCTORS', true),
('f0000001-0000-0000-0000-000000000012', '11111111-1111-1111-1111-111111111111', 'PRESCRIPTIONS', true),
('f0000001-0000-0000-0000-000000000013', '11111111-1111-1111-1111-111111111111', 'HEALTH_GUIDES', true),
('f0000001-0000-0000-0000-000000000014', '11111111-1111-1111-1111-111111111111', 'REVIEWS', true),
('f0000001-0000-0000-0000-000000000015', '11111111-1111-1111-1111-111111111111', 'COUPONS', true)
ON CONFLICT (tenant_id, feature_code) DO UPDATE SET is_enabled = EXCLUDED.is_enabled;

-- Seed Settings
INSERT INTO tenant_setting (id, tenant_id, setting_key, setting_value, value_type) VALUES
('s0000001-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'STORE_EMAIL', 'support@auraayurveda.com', 'STRING'),
('s0000001-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'STORE_PHONE', '+91 1800-123-4567', 'STRING')
ON CONFLICT (tenant_id, setting_key) DO UPDATE SET setting_value = EXCLUDED.setting_value;
