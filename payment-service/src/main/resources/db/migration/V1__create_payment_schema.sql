CREATE TABLE payments (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    order_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    provider VARCHAR(50) NOT NULL DEFAULT 'TEST_PROVIDER',
    transaction_reference VARCHAR(100),
    amount DECIMAL(12,2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    status VARCHAR(30) NOT NULL DEFAULT 'INITIATED',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payments_tenant_order ON payments(tenant_id, order_id);
CREATE INDEX idx_payments_tenant_customer ON payments(tenant_id, customer_id);

CREATE TABLE payment_transactions (
    id UUID PRIMARY KEY,
    payment_id UUID NOT NULL REFERENCES payments(id) ON DELETE CASCADE,
    gateway_transaction_id VARCHAR(100),
    payment_method VARCHAR(50),
    status VARCHAR(30) NOT NULL,
    raw_response_json TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pt_payment ON payment_transactions(payment_id);

CREATE TABLE refunds (
    id UUID PRIMARY KEY,
    payment_id UUID NOT NULL REFERENCES payments(id) ON DELETE CASCADE,
    refund_reference VARCHAR(100),
    amount DECIMAL(12,2) NOT NULL,
    reason TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_refunds_payment ON refunds(payment_id);
