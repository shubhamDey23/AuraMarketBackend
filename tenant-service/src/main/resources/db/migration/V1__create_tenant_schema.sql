CREATE TABLE tenant (
    id UUID PRIMARY KEY,
    tenant_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    business_type VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    default_currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    default_locale VARCHAR(10) NOT NULL DEFAULT 'en-IN',
    timezone VARCHAR(50) DEFAULT 'Asia/Kolkata',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tenant_code ON tenant(tenant_code);
CREATE INDEX idx_tenant_slug ON tenant(slug);
CREATE INDEX idx_tenant_status ON tenant(status);

CREATE TABLE tenant_branding (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenant(id) ON DELETE CASCADE,
    logo_url VARCHAR(255),
    favicon_url VARCHAR(255),
    primary_color VARCHAR(20) NOT NULL DEFAULT '#2F6B4F',
    secondary_color VARCHAR(20) NOT NULL DEFAULT '#8BAF91',
    accent_color VARCHAR(20) DEFAULT '#C69A45',
    background_color VARCHAR(20) DEFAULT '#F8FAF7',
    surface_color VARCHAR(20) DEFAULT '#FFFFFF',
    text_color VARCHAR(20) DEFAULT '#1F2933',
    font_family VARCHAR(100) DEFAULT 'Inter',
    border_radius VARCHAR(20) DEFAULT '8px',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tenant_menu (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenant(id) ON DELETE CASCADE,
    parent_id UUID REFERENCES tenant_menu(id) ON DELETE CASCADE,
    label VARCHAR(100) NOT NULL,
    route VARCHAR(255) NOT NULL,
    icon VARCHAR(50),
    display_order INT DEFAULT 0,
    is_visible BOOLEAN DEFAULT TRUE,
    required_feature VARCHAR(50),
    open_in_new_tab BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tenant_feature (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenant(id) ON DELETE CASCADE,
    feature_code VARCHAR(50) NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE,
    configuration_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_feature UNIQUE (tenant_id, feature_code)
);

CREATE TABLE tenant_setting (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenant(id) ON DELETE CASCADE,
    setting_key VARCHAR(100) NOT NULL,
    setting_value TEXT,
    value_type VARCHAR(30) DEFAULT 'STRING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_setting UNIQUE (tenant_id, setting_key)
);

-- ==================================================
-- SEED TENANT 1: AURA_AYURVEDA
-- ==================================================

INSERT INTO tenant (id, tenant_code, name, slug, business_type, status, default_currency, default_locale, timezone)
VALUES ('11111111-1111-1111-1111-111111111111', 'AURA_AYURVEDA', 'Aura Ayurveda', 'aura-ayurveda', 'AYURVEDA', 'ACTIVE', 'INR', 'en-IN', 'Asia/Kolkata');

INSERT INTO tenant_branding (id, tenant_id, logo_url, favicon_url, primary_color, secondary_color, accent_color, background_color, surface_color, text_color, font_family, border_radius)
VALUES ('11111111-2222-3333-4444-555555555555', '11111111-1111-1111-1111-111111111111', '/assets/tenants/aura-ayurveda/logo.svg', '/assets/tenants/aura-ayurveda/favicon.ico', '#2F6B4F', '#8BAF91', '#C69A45', '#F8FAF7', '#FFFFFF', '#1F2933', 'Inter', '8px');

-- Aura Ayurveda Menus
INSERT INTO tenant_menu (id, tenant_id, label, route, icon, display_order, is_visible) VALUES
('a0000001-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'Home', '/', 'home', 1, true),
('a0000001-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'Ayurvedic Products', '/products', 'leaf', 2, true),
('a0000001-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 'Medicines', '/products/medicines', 'capsule', 3, true),
('a0000001-0000-0000-0000-000000000004', '11111111-1111-1111-1111-111111111111', 'Wellness', '/products/wellness', 'smile', 4, true),
('a0000001-0000-0000-0000-000000000005', '11111111-1111-1111-1111-111111111111', 'Personal Care', '/products/personal-care', 'user', 5, true),
('a0000001-0000-0000-0000-000000000006', '11111111-1111-1111-1111-111111111111', 'Herbal Oils', '/products/herbal-oils', 'droplet', 6, true),
('a0000001-0000-0000-0000-000000000007', '11111111-1111-1111-1111-111111111111', 'Skin & Hair', '/products/skin-hair', 'sparkles', 7, true),
('a0000001-0000-0000-0000-000000000008', '11111111-1111-1111-1111-111111111111', 'Nutrition', '/products/nutrition', 'heart', 8, true);

INSERT INTO tenant_menu (id, tenant_id, label, route, icon, display_order, is_visible, required_feature) VALUES
('a0000001-0000-0000-0000-000000000009', '11111111-1111-1111-1111-111111111111', 'Ayurveda Doctors', '/doctors', 'stethoscope', 9, true, 'DOCTORS'),
('a0000001-0000-0000-0000-000000000010', '11111111-1111-1111-1111-111111111111', 'Consultation', '/consultation', 'calendar', 10, true, 'CONSULTATION'),
('a0000001-0000-0000-0000-000000000011', '11111111-1111-1111-1111-111111111111', 'Health Guides', '/health-guides', 'book-open', 11, true, 'HEALTH_GUIDES'),
('a0000001-0000-0000-0000-000000000012', '11111111-1111-1111-1111-111111111111', 'Offers', '/offers', 'tag', 12, true, null);

-- Aura Ayurveda Features
INSERT INTO tenant_feature (id, tenant_id, feature_code, is_enabled) VALUES
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'PRODUCTS', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'SELLERS', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'ORDERS', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'INVENTORY', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'WISHLIST', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'CART', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'CHECKOUT', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'PAYMENTS', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'RETURNS', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'CONSULTATION', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'DOCTORS', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'PRESCRIPTIONS', true),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'HEALTH_GUIDES', true);

-- Aura Ayurveda Settings
INSERT INTO tenant_setting (id, tenant_id, setting_key, setting_value, value_type) VALUES
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'STORE_EMAIL', 'support@auraayurveda.com', 'STRING'),
(random_uuid(), '11111111-1111-1111-1111-111111111111', 'STORE_PHONE', '+91 1800-123-4567', 'STRING');

-- ==================================================
-- SEED TENANT 2: FASHION_STORE
-- ==================================================

INSERT INTO tenant (id, tenant_code, name, slug, business_type, status, default_currency, default_locale, timezone)
VALUES ('22222222-2222-2222-2222-222222222222', 'FASHION_STORE', 'Demo Fashion Store', 'demo-fashion-store', 'FASHION', 'ACTIVE', 'INR', 'en-IN', 'Asia/Kolkata');

INSERT INTO tenant_branding (id, tenant_id, logo_url, favicon_url, primary_color, secondary_color, accent_color, background_color, surface_color, text_color, font_family, border_radius)
VALUES ('22222222-3333-4444-5555-666666666666', '22222222-2222-2222-2222-222222222222', '/assets/tenants/fashion-store/logo.svg', '/assets/tenants/fashion-store/favicon.ico', '#D946EF', '#8B5CF6', '#F59E0B', '#FAF5FF', '#FFFFFF', '#1E1B4B', 'Inter', '12px');

-- Fashion Store Menus
INSERT INTO tenant_menu (id, tenant_id, label, route, icon, display_order, is_visible) VALUES
('b0000001-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222', 'Home', '/', 'home', 1, true),
('b0000001-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', 'Men''s Fashion', '/category/men', 'user', 2, true),
('b0000001-0000-0000-0000-000000000003', '22222222-2222-2222-2222-222222222222', 'Women''s Collection', '/category/women', 'heart', 3, true),
('b0000001-0000-0000-0000-000000000004', '22222222-2222-2222-2222-222222222222', 'Kids', '/category/kids', 'smile', 4, true),
('b0000001-0000-0000-0000-000000000005', '22222222-2222-2222-2222-222222222222', 'Accessories', '/category/accessories', 'sparkles', 5, true),
('b0000001-0000-0000-0000-000000000006', '22222222-2222-2222-2222-222222222222', 'Flash Deals', '/deals', 'tag', 6, true);

-- Fashion Store Features
INSERT INTO tenant_feature (id, tenant_id, feature_code, is_enabled) VALUES
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'PRODUCTS', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'SELLERS', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'ORDERS', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'INVENTORY', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'WISHLIST', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'CART', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'CHECKOUT', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'PAYMENTS', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'RETURNS', true),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'CONSULTATION', false),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'DOCTORS', false),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'PRESCRIPTIONS', false),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'HEALTH_GUIDES', false);

-- Fashion Store Settings
INSERT INTO tenant_setting (id, tenant_id, setting_key, setting_value, value_type) VALUES
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'STORE_EMAIL', 'contact@demofashion.com', 'STRING'),
(random_uuid(), '22222222-2222-2222-2222-222222222222', 'STORE_PHONE', '+91 1800-999-8888', 'STRING');
