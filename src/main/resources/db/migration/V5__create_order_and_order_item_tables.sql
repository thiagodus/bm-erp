CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        customer_id UUID NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        notes TEXT,
                        total NUMERIC(19,2) NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                        updated_at TIMESTAMP WITH TIME ZONE,

                        CONSTRAINT fk_order_customer
                            FOREIGN KEY (customer_id)
                                REFERENCES customer(id)
);

CREATE TABLE order_item (
                            id UUID PRIMARY KEY,
                            order_id UUID NOT NULL,
                            product_id UUID NOT NULL,
                            quantity NUMERIC(19,2) NOT NULL,
                            unit_price NUMERIC(19,2) NOT NULL,

                            CONSTRAINT fk_order_item_order
                                FOREIGN KEY (order_id)
                                    REFERENCES orders(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_order_item_product
                                FOREIGN KEY (product_id)
                                    REFERENCES product(id)
);