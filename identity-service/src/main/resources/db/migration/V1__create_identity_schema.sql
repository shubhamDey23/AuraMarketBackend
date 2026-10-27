CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

INSERT INTO roles (id, name, description) VALUES
(1, 'CUSTOMER', 'Retail shopper role'),
(2, 'SELLER', 'Merchant and storefront owner role'),
(3, 'DOCTOR', 'Ayurveda doctor and practitioner role'),
(4, 'ADMIN', 'Platform super administrator role');

CREATE TABLE users (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(30),
    profile_image_url VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    provider VARCHAR(30) NOT NULL DEFAULT 'LOCAL',
    provider_user_id VARCHAR(150),
    last_login_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_users_email_tenant UNIQUE (email, tenant_id)
);

CREATE INDEX idx_users_email_tenant ON users(email, tenant_id);
CREATE INDEX idx_users_provider_user ON users(provider, provider_user_id);
CREATE INDEX idx_users_tenant ON users(tenant_id);

CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITH TIME ZONE,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_hash ON refresh_tokens(token_hash);

-- Seed Development Users under tenant AURA_AYURVEDA (Password: Password@123)
INSERT INTO users (id, tenant_id, email, password_hash, first_name, last_name, phone, status, email_verified, provider)
VALUES 
('11111111-1111-1111-1111-111111111111', 'AURA_AYURVEDA', 'customer@auramarket.local', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Aura', 'Customer', '9876543210', 'ACTIVE', true, 'LOCAL'),
('22222222-2222-2222-2222-222222222222', 'AURA_AYURVEDA', 'seller@auramarket.local', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Aura', 'Seller', '9876543211', 'ACTIVE', true, 'LOCAL'),
('33333333-3333-3333-3333-333333333333', 'AURA_AYURVEDA', 'doctor@auramarket.local', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Dr. Vaidya', 'Ayush', '9876543212', 'ACTIVE', true, 'LOCAL'),
('44444444-4444-4444-4444-444444444444', 'AURA_AYURVEDA', 'admin@auramarket.local', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'System', 'Admin', '9876543213', 'ACTIVE', true, 'LOCAL');

-- Map roles to seeded dev users
INSERT INTO user_roles (user_id, role_id) VALUES
('11111111-1111-1111-1111-111111111111', 1),
('22222222-2222-2222-2222-222222222222', 2),
('33333333-3333-3333-3333-333333333333', 3),
('44444444-4444-4444-4444-444444444444', 4);
