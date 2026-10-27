CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    recipient_id UUID NOT NULL,
    channel VARCHAR(30) NOT NULL DEFAULT 'IN_APP',
    subject VARCHAR(255),
    body TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    event_id VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_notifications_recipient ON notifications(recipient_id);
CREATE INDEX idx_notifications_tenant_recipient ON notifications(tenant_id, recipient_id);

CREATE TABLE notification_preferences (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL,
    email_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    sms_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    in_app_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_user_pref UNIQUE (tenant_id, user_id)
);

CREATE TABLE notification_templates (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    template_code VARCHAR(100) NOT NULL,
    channel VARCHAR(30) NOT NULL,
    subject_template VARCHAR(255),
    body_template TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_tenant_template UNIQUE (tenant_id, template_code, channel)
);

-- Seed default templates
INSERT INTO notification_templates (id, tenant_id, template_code, channel, subject_template, body_template, created_at)
VALUES 
('e0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', 'ORDER_CREATED', 'IN_APP', 'Order Placed', 'Your order {{orderId}} has been placed successfully.', CURRENT_TIMESTAMP),
('e0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', 'APPOINTMENT_BOOKED', 'IN_APP', 'Appointment Confirmed', 'Your appointment with doctor has been confirmed for {{date}}.', CURRENT_TIMESTAMP);
