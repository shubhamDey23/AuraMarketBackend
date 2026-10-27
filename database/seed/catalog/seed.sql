-- Catalog Service Seed Data (auramarket_catalog)

-- 1. Seed Main Categories for AURA_AYURVEDA
INSERT INTO categories (id, tenant_id, parent_id, name, slug, description, icon, display_order, is_active) VALUES
('c0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', NULL, 'Ayurvedic Medicines', 'ayurvedic-medicines', 'Classical herbal formulations & churnas', 'capsule', 1, true),
('c0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', NULL, 'Personal Care', 'personal-care', 'Herbal skincare, body care and hair products', 'sparkles', 2, true),
('c0000001-0000-0000-0000-000000000003', 'AURA_AYURVEDA', NULL, 'Herbal Oils', 'herbal-oils', 'Traditional Ayurvedic massage and hair oils', 'droplet', 3, true),
('c0000001-0000-0000-0000-000000000004', 'AURA_AYURVEDA', NULL, 'Wellness & Immunity', 'wellness-immunity', 'Rasayanas, teas and immunity boosters', 'heart', 4, true),
('c0000001-0000-0000-0000-000000000005', 'AURA_AYURVEDA', NULL, 'Nutrition & Supplements', 'nutrition-supplements', 'Herbal nutrition powders and wellness drinks', 'leaf', 5, true),
('c0000001-0000-0000-0000-000000000006', 'AURA_AYURVEDA', NULL, 'Books & Education', 'books-education', 'Ayurvedic literature and lifestyle guides', 'book-open', 6, true)
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, slug = EXCLUDED.slug, description = EXCLUDED.description;

-- Subcategories
INSERT INTO categories (id, tenant_id, parent_id, name, slug, description, icon, display_order, is_active) VALUES
('c0000002-0000-0000-0000-000000000001', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000001', 'Classical Preparations', 'classical-preparations', 'Traditional churnas and kwaths', 'circle', 1, true),
('c0000002-0000-0000-0000-000000000002', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000001', 'Proprietary Ayurvedic Products', 'proprietary-ayurvedic', 'Modern Ayurvedic tablet blends', 'disc', 2, true),
('c0000002-0000-0000-0000-000000000003', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000001', 'Herbal Formulations', 'herbal-formulations', 'Single herb extracts and capsules', 'feather', 3, true),

('c0000002-0000-0000-0000-000000000004', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000002', 'Face Care', 'face-care', 'Ayurvedic face packs and washes', 'smile', 1, true),
('c0000002-0000-0000-0000-000000000005', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000002', 'Body Care', 'body-care', 'Herbal bath powders and lotions', 'user', 2, true),
('c0000002-0000-0000-0000-000000000006', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000002', 'Hair Care', 'hair-care', 'Herbal shampoos and cleansers', 'scissors', 3, true),
('c0000002-0000-0000-0000-000000000007', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000002', 'Oral Care', 'oral-care', 'Herbal tooth powders and paste', 'check-circle', 4, true),

('c0000002-0000-0000-0000-000000000008', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000003', 'Hair Oils', 'hair-oils', 'Traditional Bhringraj and Brahmi hair oils', 'droplet', 1, true),
('c0000002-0000-0000-0000-000000000009', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000003', 'Massage Oils', 'massage-oils', 'Abhyanga and pain-relief body oils', 'activity', 2, true),
('c0000002-0000-0000-0000-000000000010', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000003', 'Skin Oils', 'skin-oils', 'Kumkumadi and facial oils', 'sun', 3, true),

('c0000002-0000-0000-0000-000000000011', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000004', 'Herbal Teas', 'herbal-teas', 'Tulsi, green and digestive teas', 'coffee', 1, true),
('c0000002-0000-0000-0000-000000000012', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000004', 'Wellness Essentials', 'wellness-essentials', 'Daily Rasayana health supplements', 'star', 2, true),

('c0000002-0000-0000-0000-000000000013', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000005', 'Nutritional Products', 'nutritional-products', 'Superfood powders and tonics', 'package', 1, true),
('c0000002-0000-0000-0000-000000000014', 'AURA_AYURVEDA', 'c0000001-0000-0000-0000-000000000006', 'Ayurveda Books', 'ayurveda-books', 'Literature on Dinacharya and herbal remedy', 'book', 1, true)
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, slug = EXCLUDED.slug;

-- 2. Seed Brands for AURA_AYURVEDA
INSERT INTO brands (id, tenant_id, name, slug, description, is_active) VALUES
('b0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', 'ArogyaVeda', 'arogyaveda', 'Traditional formulations inspired by classical texts', true),
('b0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', 'HerbalRoot', 'herbalroot', 'Certified organic herbals harvested from ethical gardens', true),
('b0000001-0000-0000-0000-000000000003', 'AURA_AYURVEDA', 'PrakritiCare', 'prakriticare', 'Holistic self-care products rooted in Panchakarma traditions', true),
('b0000001-0000-0000-0000-000000000004', 'AURA_AYURVEDA', 'AyurWell', 'ayurwell', 'Modern wellness solutions driven by botanical research', true),
('b0000001-0000-0000-0000-000000000005', 'AURA_AYURVEDA', 'NatureVeda', 'natureveda', 'Pure, unrefined herbal tonics and natural elixirs', true),
('b0000001-0000-0000-0000-000000000006', 'AURA_AYURVEDA', 'SattvaLife', 'sattvalife', 'Premium Ayurvedic lifestyle items and educational resources', true)
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, slug = EXCLUDED.slug;

-- 3. Seed 30 Ayurveda Products
INSERT INTO products (id, tenant_id, seller_id, category_id, subcategory_id, brand_id, name, slug, description, short_description, product_type, sku, status, price, discount_price, currency, images_json, attributes_json, tags_json) VALUES

-- 1. Ashwagandha Capsules
('e0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222', 
 'c0000001-0000-0000-0000-000000000001', 'c0000002-0000-0000-0000-000000000003', 'b0000001-0000-0000-0000-000000000001',
 'Organic Ashwagandha Extract Capsules 60s', 'organic-ashwagandha-extract-capsules-60s',
 'Traditional herbal formulation commonly used in Ayurveda-inspired wellness routines for vitality and stress balance.',
 'Stress relief and vitality capsule supplement', 'PHYSICAL', 'AYU-ASHW-60', 'ACTIVE', 499.00, 449.00, 'INR',
 '["/assets/products/ashwagandha.jpg"]', '{"weight":"60 capsules","dosage":"1-2 capsules daily","ingredients":"Withania somnifera root extract"}', '["vitality","stress","herbal"]'),

-- 2. Triphala Powder
('e0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222',
 'c0000001-0000-0000-0000-000000000001', 'c0000002-0000-0000-0000-000000000001', 'b0000001-0000-0000-0000-000000000001',
 'Organic Triphala Churna 250g', 'organic-triphala-churna-250g',
 'A classic combination of Amla, Haritaki, and Bibhitaki recommended for gentle digestion support in daily wellness routines.',
 'Gentle digestive and detox powder', 'PHYSICAL', 'AYU-TRIP-250', 'ACTIVE', 350.00, 299.00, 'INR',
 '["/assets/products/triphala.jpg"]', '{"weight":"250g","usage":"1 tsp with lukewarm water","ingredients":"Amla, Haritaki, Bibhitaki"}', '["digestion","detox","classical"]'),

-- 3. Turmeric Herbal Capsules
('e0000001-0000-0000-0000-000000000003', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222223',
 'c0000001-0000-0000-0000-000000000001', 'c0000002-0000-0000-0000-000000000003', 'b0000001-0000-0000-0000-000000000002',
 'Turmeric Curcumin Herbal Capsules', 'turmeric-curcumin-herbal-capsules',
 'Potent organic turmeric extract enriched with black pepper extract for enhanced absorption and daily antioxidant balance.',
 'Antioxidant and immunity capsule', 'PHYSICAL', 'AYU-TURM-60', 'ACTIVE', 599.00, 499.00, 'INR',
 '["/assets/products/turmeric.jpg"]', '{"weight":"60 capsules","ingredients":"Curcuma longa, Piper nigrum"}', '["antioxidant","immunity"]'),

-- 4. Neem Face Wash
('e0000001-0000-0000-0000-000000000004', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222224',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000004', 'b0000001-0000-0000-0000-000000000003',
 'Purifying Neem & Tea Tree Face Wash 150ml', 'purifying-neem-tea-tree-face-wash-150ml',
 'Soap-free herbal face wash containing neem and tea tree extract for clear, refreshed facial skin.',
 'Gentle purifying facial cleanser', 'PHYSICAL', 'AYU-NEEM-FW', 'ACTIVE', 280.00, 240.00, 'INR',
 '["/assets/products/neem-facewash.jpg"]', '{"volume":"150ml","skinType":"All skin types"}', '["skincare","face","cleanser"]'),

-- 5. Aloe Vera Gel
('e0000001-0000-0000-0000-000000000005', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222224',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000004', 'b0000001-0000-0000-0000-000000000003',
 'Pure Organic Aloe Vera Gel 200g', 'pure-organic-aloe-vera-gel-200g',
 'Multi-purpose soothing skin gel crafted with cold-pressed organic aloe vera leaf extract.',
 'Soothing facial and body gel', 'PHYSICAL', 'AYU-ALOE-200', 'ACTIVE', 320.00, 275.00, 'INR',
 '["/assets/products/aloe-gel.jpg"]', '{"weight":"200g","usage":"Apply gently on face & skin"}', '["skincare","aloe","moisturizer"]'),

-- 6. Herbal Shampoo
('e0000001-0000-0000-0000-000000000006', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222225',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000006', 'b0000001-0000-0000-0000-000000000004',
 'Shikakai & Reetha Gentle Herbal Shampoo 300ml', 'shikakai-reetha-herbal-shampoo-300ml',
 'Traditional sulphate-free cleanser formulated with Shikakai, Reetha, and Amla for healthy scalp care.',
 'Sulphate-free scalp cleanser', 'PHYSICAL', 'AYU-SHAM-300', 'ACTIVE', 450.00, 399.00, 'INR',
 '["/assets/products/herbal-shampoo.jpg"]', '{"volume":"300ml","hairType":"Normal to Oily"}', '["haircare","shampoo","natural"]'),

-- 7. Bhringraj Hair Oil
('e0000001-0000-0000-0000-000000000007', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222',
 'c0000001-0000-0000-0000-000000000003', 'c0000002-0000-0000-0000-000000000008', 'b0000001-0000-0000-0000-000000000001',
 'Intensive Bhringraj Hair Oil 200ml', 'intensive-bhringraj-hair-oil-200ml',
 'Nourishing botanical hair oil boiled according to Taila Paka Vidhi using Bhringraj, Amla and Sesame Oil.',
 'Revitalizing scalp and hair oil', 'PHYSICAL', 'AYU-BHRING-200', 'ACTIVE', 499.00, 425.00, 'INR',
 '["/assets/products/bhringraj-oil.jpg"]', '{"volume":"200ml","ingredients":"Bhringraj, Sesame Oil, Amla"}', '["hairoil","bhringraj","scalp"]'),

-- 8. Brahmi Hair Oil
('e0000001-0000-0000-0000-000000000008', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222223',
 'c0000001-0000-0000-0000-000000000003', 'c0000002-0000-0000-0000-000000000008', 'b0000001-0000-0000-0000-000000000002',
 'Cooling Brahmi & Coconut Hair Oil 150ml', 'cooling-brahmi-coconut-hair-oil-150ml',
 'Cooling head massage oil blended with Brahmi leaves and pure virgin coconut oil for soothing stress relief.',
 'Cooling scalp massage oil', 'PHYSICAL', 'AYU-BRAH-150', 'ACTIVE', 380.00, 330.00, 'INR',
 '["/assets/products/brahmi-oil.jpg"]', '{"volume":"150ml","ingredients":"Brahmi, Virgin Coconut Oil"}', '["brahmi","cooling","hairoil"]'),

-- 9. Sesame Massage Oil (Abhyanga)
('e0000001-0000-0000-0000-000000000009', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222223',
 'c0000001-0000-0000-0000-000000000003', 'c0000002-0000-0000-0000-000000000009', 'b0000001-0000-0000-0000-000000000002',
 'Warm Sesame Abhyanga Body Massage Oil 500ml', 'warm-sesame-abhyanga-body-oil-500ml',
 'Cold-pressed black sesame oil formulated for daily Abhyanga self-massage routines.',
 'Traditional daily body massage oil', 'PHYSICAL', 'AYU-ABHY-500', 'ACTIVE', 650.00, 560.00, 'INR',
 '["/assets/products/sesame-oil.jpg"]', '{"volume":"500ml","usage":"Warm slightly before body application"}', '["abhyanga","massage","bodyoil"]'),

-- 10. Kumkumadi Facial Oil
('e0000001-0000-0000-0000-000000000010', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222224',
 'c0000001-0000-0000-0000-000000000003', 'c0000002-0000-0000-0000-000000000010', 'b0000001-0000-0000-0000-000000000005',
 'Radiant Kumkumadi Tailam Facial Oil 30ml', 'radiant-kumkumadi-tailam-facial-oil-30ml',
 'Precious Ayurvedic night serum made with saffron, sandalwood, and lotus stamens for natural skin glow.',
 'Luxury Ayurvedic saffron face oil', 'PHYSICAL', 'AYU-KUMK-30', 'ACTIVE', 999.00, 850.00, 'INR',
 '["/assets/products/kumkumadi.jpg"]', '{"volume":"30ml","usage":"2-3 drops at night"}', '["skinoil","saffron","nightserum"]'),

-- 11. Herbal Green Tea
('e0000001-0000-0000-0000-000000000011', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222225',
 'c0000001-0000-0000-0000-000000000004', 'c0000002-0000-0000-0000-000000000011', 'b0000001-0000-0000-0000-000000000006',
 'Organic Loose Leaf Herbal Green Tea 100g', 'organic-loose-leaf-herbal-green-tea-100g',
 'Whole leaf green tea blended with lemon peel and ginger for a invigorating daily beverage.',
 'Refreshing herbal green tea blend', 'PHYSICAL', 'AYU-TEA-100', 'ACTIVE', 299.00, 249.00, 'INR',
 '["/assets/products/green-tea.jpg"]', '{"weight":"100g","servings":40}', '["tea","antioxidant","wellness"]'),

-- 12. Tulsi Herbal Tea
('e0000001-0000-0000-0000-000000000012', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222225',
 'c0000001-0000-0000-0000-000000000004', 'c0000002-0000-0000-0000-000000000011', 'b0000001-0000-0000-0000-000000000006',
 'Triple Tulsi Wellness Tea Bags 25s', 'triple-tulsi-wellness-tea-bags-25s',
 'Blend of Rama, Krishna and Vana Tulsi leaves crafted for calming evening tea rituals.',
 'Calming holy basil herbal tea', 'PHYSICAL', 'AYU-TULSI-25', 'ACTIVE', 220.00, 190.00, 'INR',
 '["/assets/products/tulsi-tea.jpg"]', '{"count":"25 tea bags","ingredients":"Rama, Krishna, Vana Tulsi"}', '["tulsi","calming","tea"]'),

-- 13. Digestive Herbal Tea
('e0000001-0000-0000-0000-000000000013', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222',
 'c0000001-0000-0000-0000-000000000004', 'c0000002-0000-0000-0000-000000000011', 'b0000001-0000-0000-0000-000000000001',
 'Cumin Coriander Fennel Digestive Tea 150g', 'cumin-coriander-fennel-digestive-tea-150g',
 'Classic CCF tea formula combining equal parts roasted Cumin, Coriander, and Fennel seeds.',
 'Traditional CCF digestion tea blend', 'PHYSICAL', 'AYU-CCF-150', 'ACTIVE', 260.00, 220.00, 'INR',
 '["/assets/products/digestive-tea.jpg"]', '{"weight":"150g","ingredients":"Cumin, Coriander, Fennel"}', '["ccf","digestive","tea"]'),

-- 14. Amla Powder
('e0000001-0000-0000-0000-000000000014', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222',
 'c0000001-0000-0000-0000-000000000005', 'c0000002-0000-0000-0000-000000000013', 'b0000001-0000-0000-0000-000000000001',
 'Organic Pure Amla Fruit Powder 200g', 'organic-pure-amla-fruit-powder-200g',
 'Sun-dried organic Indian Gooseberry powder rich in natural Vitamin C for hair and immune nourishment.',
 'Natural Vitamin C fruit powder', 'PHYSICAL', 'AYU-AMLA-200', 'ACTIVE', 240.00, 199.00, 'INR',
 '["/assets/products/amla-powder.jpg"]', '{"weight":"200g","ingredients":"Emblica officinalis fruit"}', '["amla","vitaminc","superfood"]'),

-- 15. Shatavari Powder
('e0000001-0000-0000-0000-000000000015', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222223',
 'c0000001-0000-0000-0000-000000000005', 'c0000002-0000-0000-0000-000000000013', 'b0000001-0000-0000-0000-000000000002',
 'Organic Shatavari Root Powder 150g', 'organic-shatavari-root-powder-150g',
 'Traditional herbal root powder recognized in Ayurvedic rasayana traditions for female health and balance.',
 'Nourishing herbal root powder', 'PHYSICAL', 'AYU-SHAT-150', 'ACTIVE', 380.00, 325.00, 'INR',
 '["/assets/products/shatavari.jpg"]', '{"weight":"150g","ingredients":"Asparagus racemosus root"}', '["shatavari","wellness","rasayana"]'),

-- 16. Giloy Herbal Tablets
('e0000001-0000-0000-0000-000000000016', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222',
 'c0000001-0000-0000-0000-000000000001', 'c0000002-0000-0000-0000-000000000002', 'b0000001-0000-0000-0000-000000000001',
 'Giloy (Guduchi) Immunity Tablets 60s', 'giloy-guduchi-immunity-tablets-60s',
 'Concentrated stem extract tablets of Guduchi (Tinospora cordifolia) for daily seasonal health support.',
 'Guduchi stem extract tablets', 'PHYSICAL', 'AYU-GILO-60', 'ACTIVE', 299.00, 249.00, 'INR',
 '["/assets/products/giloy.jpg"]', '{"count":"60 tablets","dosage":"1 tablet twice daily"}', '["giloy","guduchi","immunity"]'),

-- 17. Neem Capsules
('e0000001-0000-0000-0000-000000000017', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222',
 'c0000001-0000-0000-0000-000000000001', 'c0000002-0000-0000-0000-000000000003', 'b0000001-0000-0000-0000-000000000001',
 'Purifying Neem Leaf Extract Capsules 60s', 'purifying-neem-leaf-extract-capsules-60s',
 'Organic Neem leaf extract capsules valued for blood cleansing and skin clarifying properties.',
 'Skin purifying neem capsules', 'PHYSICAL', 'AYU-NEEM-60', 'ACTIVE', 349.00, 299.00, 'INR',
 '["/assets/products/neem-capsules.jpg"]', '{"count":"60 capsules","ingredients":"Azadirachta indica leaf"}', '["neem","bloodpurifier","skin"]'),

-- 18. Sandalwood Face Pack
('e0000001-0000-0000-0000-000000000018', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222224',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000004', 'b0000001-0000-0000-0000-000000000003',
 'Chandan & Multani Mitti Face Pack 100g', 'chandan-multani-mitti-face-pack-100g',
 'Cooling sandalwood and Fuller earth facial clay pack for detoxifying oil control.',
 'Cooling facial clay mask', 'PHYSICAL', 'AYU-CHAN-100', 'ACTIVE', 350.00, 299.00, 'INR',
 '["/assets/products/sandalwood-pack.jpg"]', '{"weight":"100g","usage":"Mix with rose water"}', '["sandalwood","facepack","cooling"]'),

-- 19. Herbal Body Scrub
('e0000001-0000-0000-0000-000000000019', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222224',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000005', 'b0000001-0000-0000-0000-000000000003',
 'Ubtan Herbal Body Scrub 150g', 'ubtan-herbal-body-scrub-150g',
 'Traditional exfoliating body scrub formulated with turmeric, chickpea flour, and saffron.',
 'Exfoliating Ayurvedic ubtan scrub', 'PHYSICAL', 'AYU-UBTN-150', 'ACTIVE', 420.00, 360.00, 'INR',
 '["/assets/products/ubtan-scrub.jpg"]', '{"weight":"150g","ingredients":"Turmeric, Chickpea flour, Rose"}', '["ubtan","bodyscrub","exfoliant"]'),

-- 20. Pure Rose Water
('e0000001-0000-0000-0000-000000000020', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222224',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000004', 'b0000001-0000-0000-0000-000000000003',
 'Steam Distilled Kannauj Rose Water 200ml', 'steam-distilled-kannauj-rose-water-200ml',
 '100% pure steam distilled Damask rose water spray for refreshing facial hydration.',
 'Pure facial toner spray', 'PHYSICAL', 'AYU-ROSE-200', 'ACTIVE', 299.00, 250.00, 'INR',
 '["/assets/products/rose-water.jpg"]', '{"volume":"200ml","origin":"Kannauj, UP"}', '["rosewater","toner","facialmist"]'),

-- 21. Herbal Tooth Powder (Dashan Sanskar)
('e0000001-0000-0000-0000-000000000021', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222223',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000007', 'b0000001-0000-0000-0000-000000000002',
 'Dashan Sanskar Herbal Tooth Powder 100g', 'dashan-sanskar-herbal-tooth-powder-100g',
 'Classical Ayurvedic tooth powder prepared with Clove, Neem, Babool and Bakul for strong gums.',
 'Traditional herbal gum care powder', 'PHYSICAL', 'AYU-DASH-100', 'ACTIVE', 180.00, 150.00, 'INR',
 '["/assets/products/tooth-powder.jpg"]', '{"weight":"100g","ingredients":"Clove, Neem, Babool"}', '["oralcare","toothpowder","gumcare"]'),

-- 22. Clove Herbal Toothpaste
('e0000001-0000-0000-0000-000000000022', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222223',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000007', 'b0000001-0000-0000-0000-000000000002',
 'Complete Care Clove & Neem Toothpaste 150g', 'complete-care-clove-neem-toothpaste-150g',
 'Fluoride-free botanical toothpaste infused with clove oil and active neem extracts.',
 'Fluoride-free clove toothpaste', 'PHYSICAL', 'AYU-CLOV-150', 'ACTIVE', 199.00, 175.00, 'INR',
 '["/assets/products/toothpaste.jpg"]', '{"weight":"150g","fluorideFree":true}', '["toothpaste","clove","oralcare"]'),

-- 23. Aloe Moisturizing Cream
('e0000001-0000-0000-0000-000000000023', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222224',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000005', 'b0000001-0000-0000-0000-000000000003',
 'Saffron & Aloe Nourishing Cream 100g', 'saffron-aloe-nourishing-cream-100g',
 'Hydrating daily moisturiser blended with Aloe Vera butter, Wheatgerm oil and Saffron extract.',
 'Nourishing daily face cream', 'PHYSICAL', 'AYU-SAFF-100', 'ACTIVE', 399.00, 349.00, 'INR',
 '["/assets/products/aloe-cream.jpg"]', '{"weight":"100g","skinType":"Dry to Normal"}', '["facecream","moisturizer","saffron"]'),

-- 24. Herbal Bath Powder (Snana Churna)
('e0000001-0000-0000-0000-000000000024', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222224',
 'c0000001-0000-0000-0000-000000000002', 'c0000002-0000-0000-0000-000000000005', 'b0000001-0000-0000-0000-000000000003',
 'Snana Churna Herbal Bath Powder 250g', 'snana-churna-herbal-bath-powder-250g',
 'Soap substitute bath powder containing Green Gram, Vetiver, Rose petals and Wild Turmeric.',
 'Natural soap-free herbal bath wash', 'PHYSICAL', 'AYU-SNAN-250', 'ACTIVE', 320.00, 280.00, 'INR',
 '["/assets/products/bath-powder.jpg"]', '{"weight":"250g","ingredients":"Vetiver, Green Gram, Rose"}', '["snana","bathpowder","soapfree"]'),

-- 25. Immunity Wellness Blend
('e0000001-0000-0000-0000-000000000025', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222',
 'c0000001-0000-0000-0000-000000000004', 'c0000002-0000-0000-0000-000000000012', 'b0000001-0000-0000-0000-000000000001',
 'Ayush Kwath Immunity Powder 100g', 'ayush-kwath-immunity-powder-100g',
 'Herbal formulation blend containing Tulsi, Cinnamon, Sunthi and Krishna Marich.',
 'Traditional herbal decoction powder', 'PHYSICAL', 'AYU-KWAT-100', 'ACTIVE', 199.00, 160.00, 'INR',
 '["/assets/products/ayush-kwath.jpg"]', '{"weight":"100g","dosage":"Boil 3g in 150ml water"}', '["kwath","immunity","herbaldecoction"]'),

-- 26. Herbal Stress Support Tea
('e0000001-0000-0000-0000-000000000026', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222225',
 'c0000001-0000-0000-0000-000000000004', 'c0000002-0000-0000-0000-000000000011', 'b0000001-0000-0000-0000-000000000006',
 'Shanti Chamomile & Ashwagandha Tea 20s', 'shanti-chamomile-ashwagandha-tea-20s',
 'Bedtime herbal infusion of Chamomile flowers, Shankhpushpi, and Ashwagandha root.',
 'Restful sleep herbal tea', 'PHYSICAL', 'AYU-SLEEP-20', 'ACTIVE', 280.00, 240.00, 'INR',
 '["/assets/products/sleep-tea.jpg"]', '{"count":"20 teabags","ingredients":"Chamomile, Shankhpushpi"}', '["sleep","chamomile","relaxation"]'),

-- 27. Ayurveda Lifestyle Guide Book
('e0000001-0000-0000-0000-000000000027', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222225',
 'c0000001-0000-0000-0000-000000000006', 'c0000002-0000-0000-0000-000000000014', 'b0000001-0000-0000-0000-000000000006',
 'The Complete Ayurveda Lifestyle Handbook', 'complete-ayurveda-lifestyle-handbook',
 'Comprehensive educational guidebook detailing Dosha assessment, seasonal routines and Ayurvedic cooking.',
 'Educational guide on Doshas & daily routines', 'PHYSICAL', 'AYU-BOOK-01', 'ACTIVE', 599.00, 499.00, 'INR',
 '["/assets/products/book-lifestyle.jpg"]', '{"pages":320,"language":"English","format":"Paperback"}', '["book","lifestyle","dosha"]'),

-- 28. Dinacharya Daily Routine Guide
('e0000001-0000-0000-0000-000000000028', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222225',
 'c0000001-0000-0000-0000-000000000006', 'c0000002-0000-0000-0000-000000000014', 'b0000001-0000-0000-0000-000000000006',
 'Dinacharya & Ritucharya Wall Chart & Guide', 'dinacharya-ritucharya-wall-chart-guide',
 'Illustrated wall guide covering daily circadian habits and seasonal dietary adaptations in Ayurveda.',
 'Illustrated daily & seasonal routine chart', 'PHYSICAL', 'AYU-BOOK-02', 'ACTIVE', 350.00, 299.00, 'INR',
 '["/assets/products/chart-dinacharya.jpg"]', '{"format":"Laminated Wall Chart + Booklet"}', '["chart","dinacharya","ritucharya"]'),

-- 29. Self-Care Ayurveda Book
('e0000001-0000-0000-0000-000000000029', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222225',
 'c0000001-0000-0000-0000-000000000006', 'c0000002-0000-0000-0000-000000000014', 'b0000001-0000-0000-0000-000000000006',
 'Ayurvedic Home Remedies & Self-Care Manual', 'ayurvedic-home-remedies-self-care-manual',
 'Practical household guide to common kitchen spices, herbal gargles, and natural compresses.',
 'Practical home remedy handbook', 'PHYSICAL', 'AYU-BOOK-03', 'ACTIVE', 450.00, 399.00, 'INR',
 '["/assets/products/book-remedies.jpg"]', '{"pages":240,"language":"English"}', '["remedies","homecare","spices"]'),

-- 30. Herbal Wellness Starter Kit
('e0000001-0000-0000-0000-000000000030', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222',
 'c0000001-0000-0000-0000-000000000004', 'c0000002-0000-0000-0000-000000000012', 'b0000001-0000-0000-0000-000000000001',
 'Complete Ayurveda Daily Routine Wellness Kit', 'complete-ayurveda-daily-routine-wellness-kit',
 'Curated starter box including Triphala powder, Sesame Abhyanga oil, Rose water mist, and Copper Tongue Cleaner.',
 'Comprehensive daily self-care gift box', 'PHYSICAL', 'AYU-KIT-01', 'ACTIVE', 1499.00, 1250.00, 'INR',
 '["/assets/products/wellness-kit.jpg"]', '{"contents":"Triphala 100g, Oil 100ml, Rose water 100ml, Copper Scraper"}', '["kit","starterbox","wellness"]');

ON CONFLICT (id) DO UPDATE SET 
  name = EXCLUDED.name, 
  price = EXCLUDED.price, 
  discount_price = EXCLUDED.discount_price, 
  status = EXCLUDED.status;
