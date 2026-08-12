ALTER TABLE orders
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE order_item
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE order_item
ALTER COLUMN quantity TYPE INTEGER
    USING quantity::INTEGER;

ALTER TABLE order_item
    ADD COLUMN notes TEXT;

ALTER TABLE orders
    ALTER COLUMN customer_id DROP NOT NULL;