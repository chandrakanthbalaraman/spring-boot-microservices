ALTER TABLE orders
    ADD COLUMN idempotency_key VARCHAR(64),
    ADD COLUMN request_fingerprint VARCHAR(64);

CREATE UNIQUE INDEX uk_orders_idempotency_key
    ON orders (idempotency_key);