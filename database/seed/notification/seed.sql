-- Notification Service Seed Data (auramarket_notification)

-- 1. Notification Templates
INSERT INTO notification_templates (id, tenant_id, template_code, channel, subject_template, body_template, created_at) VALUES 
('e0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', 'ORDER_CREATED', 'IN_APP', 'Order Placed', 'Your order {{orderId}} has been placed successfully.', CURRENT_TIMESTAMP),
('e0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', 'APPOINTMENT_BOOKED', 'IN_APP', 'Appointment Confirmed', 'Your appointment with doctor has been confirmed for {{date}}.', CURRENT_TIMESTAMP),
('e0000001-0000-0000-0000-000000000003', 'AURA_AYURVEDA', 'PRESCRIPTION_CREATED', 'IN_APP', 'Prescription Available', 'Your doctor has issued a new prescription for appointment {{appointmentId}}.', CURRENT_TIMESTAMP)
ON CONFLICT (tenant_id, template_code, channel) DO NOTHING;

-- 2. Notification Preferences
INSERT INTO notification_preferences (id, tenant_id, user_id, email_enabled, sms_enabled, in_app_enabled) VALUES
('np000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', true, true, true),
('np000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', '22222222-2222-2222-2222-222222222222', true, true, true)
ON CONFLICT (tenant_id, user_id) DO NOTHING;

-- 3. Demo Notifications for Customer
INSERT INTO notifications (id, tenant_id, recipient_id, channel, subject, body, status, event_id) VALUES
('n0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', 'IN_APP', 'Order Placed', 'Your order ORD-2026-0001 has been placed successfully.', 'DELIVERED', 'EVT-1001'),
('n0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', 'IN_APP', 'Appointment Confirmed', 'Your consultation with Dr. Vaidya Ayush is confirmed for 2026-09-20.', 'DELIVERED', 'EVT-1002'),
('n0000001-0000-0000-0000-000000000003', 'AURA_AYURVEDA', '11111111-1111-1111-1111-111111111112', 'IN_APP', 'Prescription Ready', 'Dr. Vaidya Ayush has uploaded your Ayurvedic wellness prescription.', 'DELIVERED', 'EVT-1003')
ON CONFLICT (id) DO UPDATE SET status = EXCLUDED.status;
