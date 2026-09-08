ALTER TABLE md.customers
    ADD COLUMN short_code VARCHAR(8) NOT NULL;

ALTER TABLE md.customers
    ADD CONSTRAINT ux_customers_short_code
        UNIQUE (short_code);

ALTER TABLE md.customers
    ADD CONSTRAINT chk_customers_short_code
        CHECK (short_code ~ '^[A-Z0-9]{2,8}$');