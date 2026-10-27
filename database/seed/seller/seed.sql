-- Seller Service Seed Data (auramarket_seller)

INSERT INTO sellers (id, tenant_id, user_id, store_name, slug, contact_email, contact_phone, business_name, tax_id, status, description, rating) VALUES
(
    '22222222-2222-2222-2222-222222222222',
    'AURA_AYURVEDA',
    '22222222-2222-2222-2222-222222222222',
    'Aarogya Herbals (Aura Official Store)',
    'aarogya-herbals',
    'seller@auramarket.local',
    '9876543211',
    'Aura Organics & Herbals Pvt Ltd',
    'GSTIN29ABCDE1234F1ZH',
    'ACTIVE',
    'Official store for certified organic Ayurvedic wellness and healthcare products.',
    4.9
),
(
    '22222222-2222-2222-2222-222222222223',
    'AURA_AYURVEDA',
    '22222222-2222-2222-2222-222222222223',
    'Prakriti Wellness',
    'prakriti-wellness',
    'seller2.demo@auraayurveda.test',
    '9876543214',
    'Prakriti Ayurvedic Care LLP',
    'GSTIN27ABCDE5678F1ZI',
    'ACTIVE',
    'Specialist supplier of authentic Kerala Ayurvedic oils, churnas, and herbal teas.',
    4.8
),
(
    '22222222-2222-2222-2222-222222222224',
    'AURA_AYURVEDA',
    '22222222-2222-2222-2222-222222222224',
    'Sattva Ayurveda Store',
    'sattva-ayurveda-store',
    'seller3.demo@auraayurveda.test',
    '9876543215',
    'Sattva Botanical Remedies',
    'GSTIN07ABCDE9012F1ZJ',
    'ACTIVE',
    'Pure herbal supplements and classical Ayurvedic rasayanas produced under strict GMP standards.',
    4.7
),
(
    '22222222-2222-2222-2222-222222222225',
    'AURA_AYURVEDA',
    '22222222-2222-2222-2222-222222222225',
    'NatureRoot Organics',
    'natureroot-organics',
    'seller4.demo@auraayurveda.test',
    '9876543216',
    'NatureRoot Eco Wellness Pvt Ltd',
    'GSTIN33ABCDE3456F1ZK',
    'ACTIVE',
    'Ethically sourced organic herbs, eco-friendly personal care, and traditional wellness guides.',
    4.9
)
ON CONFLICT (id) DO UPDATE SET 
  store_name = EXCLUDED.store_name,
  status = EXCLUDED.status,
  description = EXCLUDED.description,
  rating = EXCLUDED.rating;
