CREATE TABLE inventory (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    product_id UUID NOT NULL,
    seller_id UUID NOT NULL,
    available_quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT NOT NULL DEFAULT 0,
    damaged_quantity INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 10,
    status VARCHAR(30) NOT NULL DEFAULT 'IN_STOCK',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_inventory_tenant_product UNIQUE (tenant_id, product_id)
);

CREATE INDEX idx_inventory_tenant_product ON inventory(tenant_id, product_id);
CREATE INDEX idx_inventory_seller ON inventory(seller_id);

CREATE TABLE inventory_transactions (
    id UUID PRIMARY KEY,
    inventory_id UUID NOT NULL REFERENCES inventory(id) ON DELETE CASCADE,
    transaction_type VARCHAR(50) NOT NULL,
    quantity_change INT NOT NULL,
    reference_id VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inventory_tx_inventory ON inventory_transactions(inventory_id);

CREATE TABLE inventory_reservations (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    product_id UUID NOT NULL,
    inventory_id UUID NOT NULL REFERENCES inventory(id) ON DELETE CASCADE,
    quantity INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inventory_res_tenant_product ON inventory_reservations(tenant_id, product_id);

-- Seed Initial Inventory for AURA_AYURVEDA products
INSERT INTO inventory (id, tenant_id, product_id, seller_id, available_quantity, reserved_quantity, damaged_quantity, reorder_level, status) VALUES
('f0000001-0000-0000-0000-000000000001', 'AURA_AYURVEDA', 'e0000001-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222', 150, 0, 0, 15, 'IN_STOCK'),
('f0000001-0000-0000-0000-000000000002', 'AURA_AYURVEDA', 'e0000001-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', 200, 0, 0, 20, 'IN_STOCK');
