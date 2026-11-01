ALTER TABLE products
    ADD COLUMN is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN is_trending BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE categories
    ADD COLUMN is_featured BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE brands
    ADD COLUMN is_featured BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_products_tenant_featured ON products (tenant_id, is_featured, status);
CREATE INDEX idx_products_tenant_trending ON products (tenant_id, is_trending, status);
CREATE INDEX idx_categories_tenant_featured ON categories (tenant_id, is_featured, is_active);
CREATE INDEX idx_brands_tenant_featured ON brands (tenant_id, is_featured, is_active);
