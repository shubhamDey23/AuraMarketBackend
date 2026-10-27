-- Consultation Service Seed Data (auramarket_consultation)

-- 1. Seed 5 Ayurveda Doctors
INSERT INTO doctors (id, tenant_id, user_id, name, profile_image_url, gender, languages, specializations, qualification, registration_authority, registration_number, years_of_experience, clinic_name, consultation_types, consultation_fee, bio, verification_status) VALUES
(
    '33333333-3333-3333-3333-333333333333',
    'AURA_AYURVEDA',
    '33333333-3333-3333-3333-333333333333',
    'Dr. Vaidya Ayush',
    '/assets/doctors/dr-ayush.jpg',
    'MALE',
    'Hindi, English, Sanskrit',
    'Kayachikitsa, Panchakarma, Nadi Pariksha',
    'BAMS, MD (Ayurveda)',
    'Central Council of Indian Medicine (CCIM)',
    'DEMO-AYU-00001',
    12,
    'Aura Ayurveda Wellness Clinic',
    'VIDEO,AUDIO,IN_PERSON',
    500.00,
    'Senior Ayurvedic physician specializing in chronic lifestyle disorders, Panchakarma detox, and holistic wellness.',
    'VERIFIED'
),
(
    '33333333-3333-3333-3333-333333333334',
    'AURA_AYURVEDA',
    '33333333-3333-3333-3333-333333333334',
    'Dr. Ananya Sharma',
    '/assets/doctors/dr-ananya.jpg',
    'FEMALE',
    'English, Hindi, Marathi',
    'Women''s Wellness, Prasuti Tantra, Hormonal Health',
    'BAMS, MS (Ayurveda)',
    'Maharashtra Council of Indian Medicine',
    'DEMO-AYU-00002',
    9,
    'Prakriti Women''s Ayurvedic Care',
    'VIDEO,AUDIO',
    600.00,
    'Ayurvedic specialist focused on natural women''s health, hormonal balance, postnatal care, and lifestyle diet.',
    'VERIFIED'
),
(
    '33333333-3333-3333-3333-333333333335',
    'AURA_AYURVEDA',
    '33333333-3333-3333-3333-333333333335',
    'Dr. Rajesh Verma',
    '/assets/doctors/dr-rajesh.jpg',
    'MALE',
    'Hindi, English, Gujarati',
    'Panchakarma Detox, Joint & Spine Health, Shalya Tantra',
    'BAMS, Ph.D. (Ayurveda)',
    'Gujarat Ayurveda University Council',
    'DEMO-AYU-00003',
    15,
    'Sattva Panchakarma & Rejuvenation Center',
    'VIDEO,IN_PERSON',
    750.00,
    'Panchakarma expert with 15 years experience in classical detox, joint care remedies, and Rasayana therapy.',
    'VERIFIED'
),
(
    '33333333-3333-3333-3333-333333333336',
    'AURA_AYURVEDA',
    '33333333-3333-3333-3333-333333333336',
    'Dr. Sunita Patel',
    '/assets/doctors/dr-sunita.jpg',
    'FEMALE',
    'English, Gujarati, Hindi',
    'Skin & Hair Ayurveda, Tvacha Roga, Herbal Cosmetology',
    'BAMS, Diploma in Ayurvedic Cosmetology',
    'Central Council of Indian Medicine (CCIM)',
    'DEMO-AYU-00004',
    8,
    'Nirmal Botanical Skin Clinic',
    'VIDEO,AUDIO',
    550.00,
    'Specialist in herbal dermatology, natural hair restoration, and Ayurvedic complexion treatments.',
    'VERIFIED'
),
(
    '33333333-3333-3333-3333-333333333337',
    'AURA_AYURVEDA',
    '33333333-3333-3333-3333-333333333337',
    'Dr. Vikram Joshi',
    '/assets/doctors/dr-vikram.jpg',
    'MALE',
    'English, Hindi, Kannada',
    'Lifestyle & Mind Wellness, Manasa Roga, Stress Management',
    'BAMS, MD (Manasa Roga)',
    'Karnataka Ayurveda Medical Council',
    'DEMO-AYU-00005',
    11,
    'Shanti Ayurveda Mind-Body Institute',
    'VIDEO,AUDIO',
    650.00,
    'Ayurvedic practitioner specializing in sleep hygiene, stress relief, Meditation integration, and Sattvic nutrition.',
    'VERIFIED'
)
ON CONFLICT (id) DO UPDATE SET 
  name = EXCLUDED.name, 
  specializations = EXCLUDED.specializations, 
  verification_status = EXCLUDED.verification_status;

-- 2. Doctor Availabilities
INSERT INTO doctor_availabilities (id, doctor_id, day_of_week, start_time, end_time, slot_duration_minutes, is_available) VALUES
('da000001-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333', 'MONDAY', '10:00:00', '13:00:00', 30, true),
('da000001-0000-0000-0000-000000000002', '33333333-3333-3333-3333-333333333333', 'MONDAY', '17:00:00', '20:00:00', 30, true),
('da000001-0000-0000-0000-000000000003', '33333333-3333-3333-3333-333333333333', 'WEDNESDAY', '10:00:00', '13:00:00', 30, true),
('da000001-0000-0000-0000-000000000004', '33333333-3333-3333-3333-333333333334', 'TUESDAY', '11:00:00', '16:00:00', 30, true),
('da000001-0000-0000-0000-000000000005', '33333333-3333-3333-3333-333333333335', 'THURSDAY', '10:00:00', '14:00:00', 30, true)
ON CONFLICT (id) DO NOTHING;

-- 3. Appointments
INSERT INTO appointments (id, tenant_id, customer_id, doctor_id, appointment_date, start_time, end_time, consultation_type, fee, status, booking_reference, payment_status) VALUES
-- Completed Appointment
('ap000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', '33333333-3333-3333-3333-333333333333', '2026-09-20', '10:00:00', '10:30:00', 'VIDEO', 500.00, 'COMPLETED', 'APT-REF-001', 'PAID'),

-- Confirmed Upcoming Appointment
('ap000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', '33333333-3333-3333-3333-333333333334', '2026-10-05', '11:00:00', '11:30:00', 'VIDEO', 600.00, 'CONFIRMED', 'APT-REF-002', 'PAID'),

-- Pending Appointment
('ap000001-0000-0000-0000-000000000003', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', '33333333-3333-3333-3333-333333333335', '2026-10-10', '10:00:00', '10:30:00', 'IN_PERSON', 750.00, 'PENDING', 'APT-REF-003', 'PENDING'),

-- Cancelled Appointment
('ap000001-0000-0000-0000-000000000004', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', '33333333-3333-3333-3333-333333333336', '2026-09-15', '15:00:00', '15:30:00', 'AUDIO', 550.00, 'CANCELLED', 'APT-REF-004', 'REFUNDED')
ON CONFLICT (id) DO UPDATE SET status = EXCLUDED.status, payment_status = EXCLUDED.payment_status;

-- 4. Prescriptions for Completed Appointment
INSERT INTO prescriptions (id, tenant_id, appointment_id, doctor_id, customer_id, diagnosis, medicines_json, advice) VALUES
(
    'rx000001-0000-0000-0000-000000000001',
    'AURA_AYURVEDA',
    'ap000001-0000-0000-0000-000000000001',
    '33333333-3333-3333-3333-333333333333',
    '11111111-1111-1111-1111-111111111112',
    'Vata-Pitta imbalance with mild digestive sluggishness (Agnimandya) and stress-related fatigue.',
    '[{"medicineName":"Triphala Churna","dosage":"1 tsp","timing":"At bedtime with warm water","duration":"30 days"},{"medicineName":"Ashwagandha Capsules","dosage":"1 capsule","timing":"Twice daily after meals","duration":"45 days"}]',
    'Drink warm CCF tea daily. Avoid heavy cold meals at night. Practice 15 minutes of Pranayama in the morning.'
)
ON CONFLICT (id) DO UPDATE SET diagnosis = EXCLUDED.diagnosis, advice = EXCLUDED.advice;
