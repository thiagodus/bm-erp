CREATE TABLE boleto (
                        id UUID PRIMARY KEY,
                        order_id UUID NOT NULL UNIQUE,
                        external_id VARCHAR(100) NOT NULL UNIQUE,
                        amount NUMERIC(19, 2) NOT NULL,
                        due_date DATE NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE NOT NULL,

                        CONSTRAINT fk_boleto_order
                            FOREIGN KEY (order_id)
                                REFERENCES orders(id)
);