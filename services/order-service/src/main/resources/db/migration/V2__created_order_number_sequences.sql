CREATE TABLE ord.order_number_sequences (
    customer_id UUID NOT NULL,
    year INTEGER NOT NULL,
    last_number INTEGER NOT NULL,

    PRIMARY KEY (customer_id, year),

    CONSTRAINT chk_order_number_sequences_year
        CHECK (year > 0),

    CONSTRAINT chk_order_number_sequences_last_number
        CHECK (last_number >= 0)
);