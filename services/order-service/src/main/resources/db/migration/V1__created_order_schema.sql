CREATE SCHEMA IF NOT EXISTS ord;


-- =========================================================
-- ORDERS
-- =========================================================

CREATE TABLE ord.orders (
                            order_id UUID PRIMARY KEY,

    -- Immutable internal business identifier.
    -- Example: ORD-NS-2026-0001
                            order_number VARCHAR(50) NOT NULL,

    -- References Master Data Service.
    -- Intentionally no cross-service foreign key.
                            customer_id UUID NOT NULL,

    -- Reference supplied by the customer.
                            customer_reference VARCHAR(100),

                            status VARCHAR(30) NOT NULL,

                            special_requirements TEXT,

                            created_at TIMESTAMPTZ NOT NULL,
                            updated_at TIMESTAMPTZ NOT NULL,
                            row_version BIGINT NOT NULL,

                            CONSTRAINT ux_orders_order_number
                                UNIQUE (order_number),

                            CONSTRAINT ux_orders_customer_reference
                                UNIQUE (customer_id, customer_reference),

                            CONSTRAINT chk_orders_status
                                CHECK (
                                    status IN (
                                               'DRAFT',
                                               'CONFIRMED',
                                               'ASSIGNED_TO_TRIP',
                                               'COMPLETED',
                                               'CANCELLED'
                                        )
                                    )
);


-- =========================================================
-- ORDER STOPS
-- =========================================================

CREATE TABLE ord.order_stops (
                                 stop_id UUID PRIMARY KEY,

                                 order_id UUID NOT NULL,

    -- Position of the stop inside the transport order.
                                 sequence_number INTEGER NOT NULL,

                                 stop_type VARCHAR(30) NOT NULL,

    -- References Master Data Service.
    -- Intentionally no cross-service foreign key.
                                 location_id UUID NOT NULL,

                                 time_window_start TIMESTAMPTZ,
                                 time_window_end TIMESTAMPTZ,

                                 instructions TEXT,
                                 reference VARCHAR(100),

                                 created_at TIMESTAMPTZ NOT NULL,
                                 updated_at TIMESTAMPTZ NOT NULL,
                                 row_version BIGINT NOT NULL,

                                 CONSTRAINT fk_order_stops_order
                                     FOREIGN KEY (order_id)
                                         REFERENCES ord.orders(order_id)
                                         ON DELETE CASCADE,

                                 CONSTRAINT ux_order_stops_sequence
                                     UNIQUE (order_id, sequence_number),

                                 CONSTRAINT chk_order_stops_sequence
                                     CHECK (sequence_number > 0),

                                 CONSTRAINT chk_order_stops_type
                                     CHECK (
                                         stop_type IN (
                                                       'PICKUP',
                                                       'DELIVERY'
                                             )
                                         ),

                                 CONSTRAINT chk_order_stops_time_window
                                     CHECK (
                                         time_window_start IS NULL
                                             OR time_window_end IS NULL
                                             OR time_window_start <= time_window_end
                                         )
);


-- =========================================================
-- CARGO ITEMS
-- =========================================================

CREATE TABLE ord.cargo_items (
                                 cargo_item_id UUID PRIMARY KEY,

                                 order_id UUID NOT NULL,

                                 description VARCHAR(255) NOT NULL,
                                 notes TEXT,

                                 created_at TIMESTAMPTZ NOT NULL,
                                 updated_at TIMESTAMPTZ NOT NULL,
                                 row_version BIGINT NOT NULL,

                                 CONSTRAINT fk_cargo_items_order
                                     FOREIGN KEY (order_id)
                                         REFERENCES ord.orders(order_id)
                                         ON DELETE CASCADE
);


-- =========================================================
-- CARGO MEASUREMENTS
-- =========================================================

CREATE TABLE ord.cargo_measurements (
                                        measurement_id UUID PRIMARY KEY,

                                        cargo_item_id UUID NOT NULL,

                                        measurement_value NUMERIC(15, 3) NOT NULL,
                                        unit VARCHAR(30) NOT NULL,

                                        created_at TIMESTAMPTZ NOT NULL,
                                        updated_at TIMESTAMPTZ NOT NULL,
                                        row_version BIGINT NOT NULL,

                                        CONSTRAINT fk_cargo_measurements_item
                                            FOREIGN KEY (cargo_item_id)
                                                REFERENCES ord.cargo_items(cargo_item_id)
                                                ON DELETE CASCADE,

    -- Example:
    -- A cargo item may have
    -- 12 PALLET + 4800 KILOGRAM,
    -- but not two separate KILOGRAM measurements.
                                        CONSTRAINT ux_cargo_measurements_unit
                                            UNIQUE (cargo_item_id, unit),

                                        CONSTRAINT chk_cargo_measurements_value
                                            CHECK (measurement_value > 0),

                                        CONSTRAINT chk_cargo_measurements_unit
                                            CHECK (
                                                unit IN (
                                                         'PIECE',
                                                         'PALLET',
                                                         'PACKAGE',
                                                         'BOX',
                                                         'KILOGRAM',
                                                         'TONNE',
                                                         'LITER',
                                                         'CUBIC_METER',
                                                         'METER',
                                                         'LOADING_METER'
                                                    )
                                                )
);


-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX ix_orders_customer_id
    ON ord.orders(customer_id);

CREATE INDEX ix_orders_status
    ON ord.orders(status);

CREATE INDEX ix_order_stops_order_id
    ON ord.order_stops(order_id);

CREATE INDEX ix_order_stops_location_id
    ON ord.order_stops(location_id);

CREATE INDEX ix_cargo_items_order_id
    ON ord.cargo_items(order_id);

CREATE INDEX ix_cargo_measurements_cargo_item_id
    ON ord.cargo_measurements(cargo_item_id);