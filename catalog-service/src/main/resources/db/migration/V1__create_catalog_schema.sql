CREATE TABLE brands (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL,
    description TEXT,
    logo_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_brands_tenant_slug UNIQUE (tenant_id, slug)
);

CREATE INDEX idx_brands_tenant ON brands(tenant_id);

CREATE TABLE categories (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    parent_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_categories_tenant_slug UNIQUE (tenant_id, slug)
);

CREATE INDEX idx_categories_tenant ON categories(tenant_id);
CREATE INDEX idx_categories_parent ON categories(parent_id);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    seller_id UUID NOT NULL,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    subcategory_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    brand_id UUID REFERENCES brands(id) ON DELETE SET NULL,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(200) NOT NULL,
    description TEXT,
    short_description VARCHAR(500),
    product_type VARCHAR(50) NOT NULL DEFAULT 'PHYSICAL',
    sku VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    price DECIMAL(12,2) NOT NULL,
    discount_price DECIMAL(12,2),
    currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    images_json TEXT,
    attributes_json TEXT,
    tags_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_products_tenant_slug UNIQUE (tenant_id, slug),
    CONSTRAINT uq_products_tenant_sku UNIQUE (tenant_id, sku)
);

CREATE INDEX idx_products_tenant_status ON products(tenant_id, status);
CREATE INDEX idx_products_seller ON products(seller_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_brand ON products(brand_id);

-- Seed Categories for AURA_AYURVEDA
INSERT INTO categories (id, tenant_id, parent_id, name, slug, description, icon, display_order) VALUES
('c0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', NULL, 'Ayurvedic Medicines', 'ayurvedic-medicines', 'Classical herbal formulations & churnas', 'capsule', 1),
('c0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', NULL, 'Wellness & Immunity', 'wellness-immunity', 'Rasayanas and daily health boosters', 'heart', 2),
('c0000001-0000-0000-0000-000000000003', 'AURA_AYURVEDA', NULL, 'Skin & Hair Care', 'skin-hair-care', 'Herbal oils, creams & hair tonics', 'sparkles', 3);

-- Seed Brands for AURA_AYURVEDA
INSERT INTO brands (id, tenant_id, name, slug, description, is_active) VALUES
('b0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', 'Aura Herbals', 'aura-herbals', 'Authentic in-house Ayurvedic formulations', true),
('b0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', 'Dabur', 'dabur', 'Trusted traditional Ayurveda brand', true);

-- Seed Sample Products for AURA_AYURVEDA
INSERT INTO products (id, tenant_id, seller_id, category_id, brand_id, name, slug, description, short_description, product_type, sku, status, price, discount_price, currency, images_json, attributes_json, tags_json) VALUES
('e0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222', 'c0000001-0000-0000-0000-000000000002', 'b0000001-0000-0000-0000-000000000001', 'Organic Triphala Churna 250g', 'organic-triphala-churna-250g', 'Pure organic Triphala powder for digestive health and rejuvenation.', 'Digestive health and detox powder', 'PHYSICAL', 'AURA-TRIP-250', 'ACTIVE', 350.00, 299.00, 'INR', '["/assets/products/triphala-1.jpg"]', '{"weight": "250g", "dosage": "1 tsp at bedtime", "expiryMonths": 24}', '["digestive", "detox", "herbal"]'),
('e0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222', 'c0000001-0000-0000-0000-000000000002', 'b0000001-0000-0000-0000-000000000001', 'Ashwagandha Capsules 60s', 'ashwagandha-capsules-60s', 'High potency Ashwagandha extract capsules for stress relief and energy.', 'Stress relief and vitality capsules', 'PHYSICAL', 'AURA-ASHW-60', 'ACTIVE', 499.00, 449.00, 'INR', '["/assets/products/ashwagandha-1.jpg"]', '{"capsuleCount": 60, "potency": "500mg", "requiresPrescription": false}', '["stress", "vitality", "adaptogen"]');
