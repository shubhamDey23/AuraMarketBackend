-- Identity Service Seed Data (auramarket_identity)

-- Insert / Ensure Roles
INSERT INTO roles (id, name, description) VALUES
(1, 'CUSTOMER', 'Retail shopper role'),
(2, 'SELLER', 'Merchant and storefront owner role'),
(3, 'DOCTOR', 'Ayurveda doctor and practitioner role'),
(4, 'ADMIN', 'Platform super administrator role')
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, description = EXCLUDED.description;

-- Insert / Ensure Users
INSERT INTO users (id, tenant_id, email, password_hash, first_name, last_name, phone, status, email_verified, provider) VALUES 
('11111111-1111-1111-1111-111111111111', 'AURA_AYURVEDA', 'customer@auramarket.local', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Aura', 'Customer', '9876543210', 'ACTIVE', true, 'LOCAL'),
('11111111-1111-1111-1111-111111111112', 'AURA_AYURVEDA', 'customer.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Demo', 'Customer', '9876543290', 'ACTIVE', true, 'LOCAL'),
('22222222-2222-2222-2222-222222222222', 'AURA_AYURVEDA', 'seller@auramarket.local', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Aura', 'Seller', '9876543211', 'ACTIVE', true, 'LOCAL'),
('22222222-2222-2222-2222-222222222223', 'AURA_AYURVEDA', 'seller2.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Prakriti', 'Seller', '9876543214', 'ACTIVE', true, 'LOCAL'),
('22222222-2222-2222-2222-222222222224', 'AURA_AYURVEDA', 'seller3.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Sattva', 'Seller', '9876543215', 'ACTIVE', true, 'LOCAL'),
('22222222-2222-2222-2222-222222222225', 'AURA_AYURVEDA', 'seller4.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'NatureRoot', 'Seller', '9876543216', 'ACTIVE', true, 'LOCAL'),
('33333333-3333-3333-3333-333333333333', 'AURA_AYURVEDA', 'doctor@auramarket.local', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Dr. Vaidya', 'Ayush', '9876543212', 'ACTIVE', true, 'LOCAL'),
('33333333-3333-3333-3333-333333333334', 'AURA_AYURVEDA', 'doctor2.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Dr. Ananya', 'Sharma', '9876543217', 'ACTIVE', true, 'LOCAL'),
('33333333-3333-3333-3333-333333333335', 'AURA_AYURVEDA', 'doctor3.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Dr. Rajesh', 'Verma', '9876543218', 'ACTIVE', true, 'LOCAL'),
('33333333-3333-3333-3333-333333333336', 'AURA_AYURVEDA', 'doctor4.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Dr. Sunita', 'Patel', '9876543219', 'ACTIVE', true, 'LOCAL'),
('33333333-3333-3333-3333-333333333337', 'AURA_AYURVEDA', 'doctor5.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Dr. Vikram', 'Joshi', '9876543220', 'ACTIVE', true, 'LOCAL'),
('44444444-4444-4444-4444-444444444444', 'AURA_AYURVEDA', 'admin@auramarket.local', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'System', 'Admin', '9876543213', 'ACTIVE', true, 'LOCAL'),
('44444444-4444-4444-4444-444444444445', 'AURA_AYURVEDA', 'admin.demo@auraayurveda.test', '$2a$10$e7x6B5o6p8q9r0s1t2u3v4w5x6y7z8A9B0C1D2E3F4G5H6I7J8K9L', 'Demo', 'Admin', '9876543299', 'ACTIVE', true, 'LOCAL')
ON CONFLICT (id) DO UPDATE SET 
  email = EXCLUDED.email, 
  first_name = EXCLUDED.first_name, 
  last_name = EXCLUDED.last_name, 
  status = EXCLUDED.status;

-- Map roles
INSERT INTO user_roles (user_id, role_id) VALUES
('11111111-1111-1111-1111-111111111111', 1),
('11111111-1111-1111-1111-111111111112', 1),
('22222222-2222-2222-2222-222222222222', 2),
('22222222-2222-2222-2222-222222222223', 2),
('22222222-2222-2222-2222-222222222224', 2),
('22222222-2222-2222-2222-222222222225', 2),
('33333333-3333-3333-3333-333333333333', 3),
('33333333-3333-3333-3333-333333333334', 3),
('33333333-3333-3333-3333-333333333335', 3),
('33333333-3333-3333-3333-333333333336', 3),
('33333333-3333-3333-3333-333333333337', 3),
('44444444-4444-4444-4444-444444444444', 4),
('44444444-4444-4444-4444-444444444445', 4)
ON CONFLICT (user_id, role_id) DO NOTHING;
