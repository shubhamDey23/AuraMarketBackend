CREATE TABLE sellers (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL,
    store_name VARCHAR(150) NOT NULL,
    slug VARCHAR(150) NOT NULL,
    contact_email VARCHAR(150) NOT NULL,
    contact_phone VARCHAR(30),
    business_name VARCHAR(200),
    tax_id VARCHAR(50),
    business_address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100) DEFAULT 'India',
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    logo_url VARCHAR(255),
    banner_url VARCHAR(255),
    description TEXT,
    rating DECIMAL(3,2) DEFAULT 5.0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_sellers_tenant_user UNIQUE (tenant_id, user_id),
    CONSTRAINT uq_sellers_tenant_slug UNIQUE (tenant_id, slug)
);

CREATE INDEX idx_sellers_tenant_status ON sellers(tenant_id, status);
CREATE INDEX idx_sellers_user ON sellers(user_id);

-- Seed Development Seller for AURA_AYURVEDA
INSERT INTO sellers (id, tenant_id, user_id, store_name, slug, contact_email, contact_phone, business_name, tax_id, status, description)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    'AURA_AYURVEDA',
    '22222222-2222-2222-2222-222222222222',
    'Aura Official Store',
    'aura-official-store',
    'seller@auramarket.local',
    '9876543211',
    'Aura Organics & Herbals Pvt Ltd',
    'GSTIN29ABCDE1234F1ZH',
    'ACTIVE',
    'Official store for certified organic Ayurvedic wellness and healthcare products.'
);
