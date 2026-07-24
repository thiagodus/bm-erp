CREATE TABLE customer
(
    id UUID PRIMARY KEY,

    type VARCHAR(20) NOT NULL,
    name VARCHAR(150) NOT NULL,
    document VARCHAR(14),
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255),

    street VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    country VARCHAR(2),

    notes VARCHAR(1000),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_customer_phone UNIQUE (phone),
    CONSTRAINT uk_customer_document UNIQUE (document)
);