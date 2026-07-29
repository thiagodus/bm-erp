CREATE TABLE product
(
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    type VARCHAR(20) NOT NULL,
    category VARCHAR(30) NOT NULL,
    sale_price NUMERIC(12, 2) NOT NULL,
    cost_price NUMERIC(12, 2),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);