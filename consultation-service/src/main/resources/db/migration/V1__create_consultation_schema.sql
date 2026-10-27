CREATE TABLE doctors (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    profile_image_url VARCHAR(255),
    gender VARCHAR(20),
    languages VARCHAR(255),
    specializations VARCHAR(255),
    qualification VARCHAR(150),
    registration_authority VARCHAR(150),
    registration_number VARCHAR(100),
    years_of_experience INT DEFAULT 0,
    clinic_name VARCHAR(150),
    consultation_types VARCHAR(100) DEFAULT 'VIDEO,AUDIO,IN_PERSON',
    consultation_fee DECIMAL(12,2) NOT NULL DEFAULT 500.00,
    bio TEXT,
    verification_status VARCHAR(30) NOT NULL DEFAULT 'PENDING_VERIFICATION',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_doctors_tenant_user UNIQUE (tenant_id, user_id)
);

CREATE INDEX idx_doctors_tenant_status ON doctors(tenant_id, verification_status);

CREATE TABLE doctor_availabilities (
    id UUID PRIMARY KEY,
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_duration_minutes INT DEFAULT 30,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_doc_avail_doctor ON doctor_availabilities(doctor_id);

CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    customer_id UUID NOT NULL,
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    consultation_type VARCHAR(50) DEFAULT 'VIDEO',
    fee DECIMAL(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    booking_reference VARCHAR(100) NOT NULL UNIQUE,
    payment_status VARCHAR(30) DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_app_tenant_customer ON appointments(tenant_id, customer_id);
CREATE INDEX idx_app_doctor_date ON appointments(doctor_id, appointment_date);

CREATE TABLE prescriptions (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    appointment_id UUID NOT NULL REFERENCES appointments(id) ON DELETE CASCADE,
    doctor_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    diagnosis TEXT,
    medicines_json TEXT,
    advice TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_rx_appointment ON prescriptions(appointment_id);

-- Seed Development Doctor for AURA_AYURVEDA
INSERT INTO doctors (id, tenant_id, user_id, name, gender, languages, specializations, qualification, registration_authority, registration_number, years_of_experience, clinic_name, consultation_types, consultation_fee, bio, verification_status)
VALUES (
    '33333333-3333-3333-3333-333333333333',
    'AURA_AYURVEDA',
    '33333333-3333-3333-3333-333333333333',
    'Dr. Vaidya Ayush',
    'MALE',
    'Hindi, English, Sanskrit',
    'Kayachikitsa, Panchakarma, Nadi Pariksha',
    'BAMS, MD (Ayurveda)',
    'Central Council of Indian Medicine (CCIM)',
    'AYU-REG-2026-88',
    12,
    'Aura Ayurveda Wellness Clinic',
    'VIDEO,AUDIO,IN_PERSON',
    500.00,
    'Senior Ayurvedic physician specializing in chronic lifestyle disorders, Panchakarma detox, and holistic wellness.',
    'VERIFIED'
);
