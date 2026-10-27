-- Create independent databases for each microservice
CREATE DATABASE auramarket_identity;
CREATE DATABASE auramarket_tenant;
CREATE DATABASE auramarket_catalog;
CREATE DATABASE auramarket_seller;
CREATE DATABASE auramarket_inventory;
CREATE DATABASE auramarket_cart;
CREATE DATABASE auramarket_order;
CREATE DATABASE auramarket_payment;
CREATE DATABASE auramarket_consultation;
CREATE DATABASE auramarket_notification;
CREATE DATABASE auramarket_admin;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE auramarket_identity TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_tenant TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_catalog TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_seller TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_inventory TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_cart TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_order TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_payment TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_consultation TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_notification TO auramarket_user;
GRANT ALL PRIVILEGES ON DATABASE auramarket_admin TO auramarket_user;
