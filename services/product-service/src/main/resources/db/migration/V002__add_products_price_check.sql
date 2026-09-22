-- Adds a price invariant on the existing products table.
-- Rollback: ALTER TABLE products DROP CONSTRAINT IF EXISTS ck_products_price;

ALTER TABLE products
    ADD CONSTRAINT ck_products_price CHECK (price > 0);
