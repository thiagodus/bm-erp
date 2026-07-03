CREATE TABLE organization
(
    id UUID PRIMARY KEY,
    legal_name varchar(100) NOT NULL,
    trade_name varchar(100) NOT NULL,
    cnpj varchar(14) NOT NULL,
    email varchar(50) NOT NULL,
    phone varchar(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP.
    street varchar(100),
    city varchar(50).
    state varchar(2),
    zip_code varchar(10),
    country varchar(2),
    active BOOLEAN NOT NULL DEFAULT TRUE
    CONSTRAINT uk_organization_cnpj UNIQUE (cnpj)
)