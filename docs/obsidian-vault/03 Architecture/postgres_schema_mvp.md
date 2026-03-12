-- =========================================
-- MVP PostgreSQL schema for Trip Tracker
-- with service-like schemas
-- =========================================

-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- -----------------------------------------
-- Schemas (simulate microservice DB separation)
-- -----------------------------------------
CREATE SCHEMA IF NOT EXISTS md;     -- master data: drivers, vehicles, customers, locations, addresses
CREATE SCHEMA IF NOT EXISTS ord;    -- orders: orders, stops
CREATE SCHEMA IF NOT EXISTS trip;   -- trips: trips, assignments, events, trip_order
CREATE SCHEMA IF NOT EXISTS doc;    -- documents: pod and other files


-- =========================================
-- ENUM types (keep simple; can be tables if preferred)
-- =========================================

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'driver_status') THEN
        CREATE TYPE driver_status AS ENUM ('ACTIVE', 'INACTIVE');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'vehicle_status') THEN
        CREATE TYPE vehicle_status AS ENUM ('ACTIVE', 'INACTIVE', 'IN_REPAIR');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'trailer_status') THEN
        CREATE TYPE trailer_status AS ENUM ('ACTIVE', 'INACTIVE', 'IN_REPAIR');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_status') THEN
        CREATE TYPE order_status AS ENUM ('DRAFT', 'CONFIRMED', 'ASSIGNED_TO_TRIP', 'COMPLETED', 'CANCELLED');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'stop_type') THEN
        CREATE TYPE stop_type AS ENUM ('PICKUP', 'DELIVERY');
    END IF;

    -- trip.status_current is a cache; source of truth is trip_event.event_type
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'trip_status') THEN
        CREATE TYPE trip_status AS ENUM ('PLANNED', 'ASSIGNED', 'STARTED', 'ARRIVED_PICKUP', 'LOADED', 'IN_TRANSIT',
                                         'ARRIVED_DELIVERY', 'DELIVERED', 'CLOSED', 'CANCELLED');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'trip_event_type') THEN
        CREATE TYPE trip_event_type AS ENUM ('PLANNED', 'ASSIGNED', 'STARTED', 'ARRIVED_PICKUP', 'LOADED', 'IN_TRANSIT',
                                             'ARRIVED_DELIVERY', 'DELIVERED', 'CLOSED', 'CANCELLED');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'document_type') THEN
        CREATE TYPE document_type AS ENUM ('POD', 'DELIVERY_NOTE', 'DAMAGE_REPORT', 'OTHER');
    END IF;
END $$;


-- =========================================
-- MASTER DATA (md)
-- =========================================

-- Addresses
CREATE TABLE IF NOT EXISTS md.addresses (
    address_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    street           TEXT NOT NULL,
    house_number     TEXT NOT NULL,
    postal_code      TEXT NOT NULL,
    city             TEXT NOT NULL,
    country          TEXT NOT NULL,
    additional_line  TEXT NULL,
    geo_lat          NUMERIC(10,7) NULL,
    geo_lon          NUMERIC(10,7) NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Customers
CREATE TABLE IF NOT EXISTS md.customers (
    customer_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_number   TEXT NOT NULL,
    name              TEXT NOT NULL,
    vat_number        TEXT NULL,
    contact_email     TEXT NULL,
    contact_phone     TEXT NULL,
    notes             TEXT NULL,
    pod_required      BOOLEAN NOT NULL DEFAULT false,
    address_id        UUID NULL REFERENCES md.addresses(address_id),
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_customers_customer_number
    ON md.customers(customer_number);

-- Locations
CREATE TABLE IF NOT EXISTS md.locations (
    location_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id        UUID NULL REFERENCES md.customers(customer_id),
    name               TEXT NOT NULL,
    contact_person     TEXT NULL,
    contact_phone      TEXT NULL,
    contact_email      TEXT NULL,
    site_instructions  TEXT NULL,
    address_id         UUID NOT NULL REFERENCES md.addresses(address_id),
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS ix_locations_customer_id
    ON md.locations(customer_id);

-- Drivers
CREATE TABLE IF NOT EXISTS md.drivers (
    driver_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    driver_number    TEXT NOT NULL,
    first_name       TEXT NOT NULL,
    last_name        TEXT NOT NULL,
    phone            TEXT NULL,
    email            TEXT NULL,
    license_number   TEXT NULL,
    status           driver_status NOT NULL DEFAULT 'ACTIVE',
    address_id       UUID NULL REFERENCES md.addresses(address_id),
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_drivers_driver_number
    ON md.drivers(driver_number);

CREATE INDEX IF NOT EXISTS ix_drivers_status
    ON md.drivers(status);

-- Vehicles
CREATE TABLE IF NOT EXISTS md.vehicles (
    vehicle_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_number   TEXT NOT NULL,
    license_plate    TEXT NOT NULL,
    vin              TEXT NULL,
    brand            TEXT NULL,
    model            TEXT NULL,
    status           vehicle_status NOT NULL DEFAULT 'ACTIVE',
    notes            TEXT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_vehicles_vehicle_number
    ON md.vehicles(vehicle_number);

CREATE UNIQUE INDEX IF NOT EXISTS ux_vehicles_license_plate
    ON md.vehicles(license_plate);

CREATE INDEX IF NOT EXISTS ix_vehicles_status
    ON md.vehicles(status);

-- Trailers
CREATE TABLE IF NOT EXISTS md.trailers (
    trailer_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trailer_number   TEXT NOT NULL,
    license_plate    TEXT NULL,
    trailer_type     TEXT NULL,
    status           trailer_status NOT NULL DEFAULT 'ACTIVE',
    notes            TEXT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_trailers_trailer_number
    ON md.trailers(trailer_number);

CREATE UNIQUE INDEX IF NOT EXISTS ux_trailers_license_plate
    ON md.trailers(license_plate)
    WHERE license_plate IS NOT NULL;

CREATE INDEX IF NOT EXISTS ix_trailers_status
    ON md.trailers(status);


-- =========================================
-- ORDERS (ord)
-- =========================================

-- Orders
CREATE TABLE IF NOT EXISTS ord.orders (
    order_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id         UUID NOT NULL REFERENCES md.customers(customer_id),
    customer_reference  TEXT NOT NULL,
    cargo_description   TEXT NOT NULL,
    weight_kg           NUMERIC(12,3) NULL,
    volume_m3           NUMERIC(12,3) NULL,
    pallets             INTEGER NULL,
    special_requirements TEXT NULL,
    status              order_status NOT NULL DEFAULT 'DRAFT',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Unique "customer_reference" per customer (very common in logistics)
CREATE UNIQUE INDEX IF NOT EXISTS ux_orders_customer_reference_per_customer
    ON ord.orders(customer_id, customer_reference);

CREATE INDEX IF NOT EXISTS ix_orders_customer_id
    ON ord.orders(customer_id);

CREATE INDEX IF NOT EXISTS ix_orders_status
    ON ord.orders(status);

-- Stops
CREATE TABLE IF NOT EXISTS ord.stops (
    stop_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id           UUID NOT NULL REFERENCES ord.orders(order_id) ON DELETE CASCADE,
    sequence_number    INTEGER NOT NULL,
    type               stop_type NOT NULL,
    location_id        UUID NOT NULL REFERENCES md.locations(location_id),
    time_window_start  TIMESTAMPTZ NULL,
    time_window_end    TIMESTAMPTZ NULL,
    instructions       TEXT NULL,
    reference          TEXT NULL,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_stops_sequence_positive CHECK (sequence_number > 0)
);

-- Order stop sequence must be unique per order
CREATE UNIQUE INDEX IF NOT EXISTS ux_stops_order_sequence
    ON ord.stops(order_id, sequence_number);

CREATE INDEX IF NOT EXISTS ix_stops_location_id
    ON ord.stops(location_id);

CREATE INDEX IF NOT EXISTS ix_stops_order_id
    ON ord.stops(order_id);


-- =========================================
-- TRIPS (trip)
-- =========================================

-- Trips
CREATE TABLE IF NOT EXISTS trip.trips (
    trip_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trip_number     TEXT NOT NULL,
    driver_id       UUID NULL REFERENCES md.drivers(driver_id),
    vehicle_id      UUID NULL REFERENCES md.vehicles(vehicle_id),
    trailer_id      UUID NULL REFERENCES md.trailers(trailer_id),
    status_current  trip_status NOT NULL DEFAULT 'PLANNED',
    planned_start   TIMESTAMPTZ NULL,
    planned_end     TIMESTAMPTZ NULL,
    dispatcher_notes TEXT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_trips_trip_number
    ON trip.trips(trip_number);

CREATE INDEX IF NOT EXISTS ix_trips_status_current
    ON trip.trips(status_current);

CREATE INDEX IF NOT EXISTS ix_trips_driver_id
    ON trip.trips(driver_id);

CREATE INDEX IF NOT EXISTS ix_trips_vehicle_id
    ON trip.trips(vehicle_id);

CREATE INDEX IF NOT EXISTS ix_trips_planned_start
    ON trip.trips(planned_start);

-- Trip-Order join (supports multiple orders per trip)
CREATE TABLE IF NOT EXISTS trip.trip_orders (
    trip_id   UUID NOT NULL REFERENCES trip.trips(trip_id) ON DELETE CASCADE,
    order_id  UUID NOT NULL REFERENCES ord.orders(order_id),
    PRIMARY KEY (trip_id, order_id)
);

CREATE INDEX IF NOT EXISTS ix_trip_orders_order_id
    ON trip.trip_orders(order_id);

-- Trip Events (append-only timeline)
CREATE TABLE IF NOT EXISTS trip.trip_events (
    event_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trip_id            UUID NOT NULL REFERENCES trip.trips(trip_id) ON DELETE CASCADE,
    event_type         trip_event_type NOT NULL,
    occurred_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_user_id UUID NOT NULL, -- from Identity Service; no FK here
    note               TEXT NULL,
    client_timestamp   TIMESTAMPTZ NULL,
    idempotency_key    TEXT NULL
);

-- Idempotency: same trip + same idempotency_key should not insert twice
CREATE UNIQUE INDEX IF NOT EXISTS ux_trip_events_idempotency_per_trip
    ON trip.trip_events(trip_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE INDEX IF NOT EXISTS ix_trip_events_trip_id_occurred_at
    ON trip.trip_events(trip_id, occurred_at);

CREATE INDEX IF NOT EXISTS ix_trip_events_event_type
    ON trip.trip_events(event_type);

-- (Optional but common) One "CLOSED" per trip
-- This is not perfect for all cases, but helps enforce consistency.
CREATE UNIQUE INDEX IF NOT EXISTS ux_trip_events_closed_once
    ON trip.trip_events(trip_id)
    WHERE event_type = 'CLOSED';


-- =========================================
-- DOCUMENTS (doc)
-- =========================================

CREATE TABLE IF NOT EXISTS doc.documents (
    document_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trip_id            UUID NOT NULL REFERENCES trip.trips(trip_id) ON DELETE CASCADE,
    order_id           UUID NULL REFERENCES ord.orders(order_id),
    type               document_type NOT NULL,
    file_name          TEXT NOT NULL,
    mime_type          TEXT NOT NULL,
    file_size_bytes    BIGINT NOT NULL,
    storage_key        TEXT NOT NULL,  -- path or object-store key
    checksum           TEXT NULL,
    uploaded_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    uploaded_by_user_id UUID NOT NULL  -- from Identity Service; no FK here
);

CREATE INDEX IF NOT EXISTS ix_documents_trip_id
    ON doc.documents(trip_id);

CREATE INDEX IF NOT EXISTS ix_documents_order_id
    ON doc.documents(order_id);

CREATE INDEX IF NOT EXISTS ix_documents_type
    ON doc.documents(type);

-- Optional: prevent duplicate uploads of same exact file to same trip (checksum-based)
CREATE UNIQUE INDEX IF NOT EXISTS ux_documents_trip_checksum
    ON doc.documents(trip_id, checksum)
    WHERE checksum IS NOT NULL;


-- =========================================
-- Helpful Views (optional)
-- =========================================

-- A view that shows the latest event per trip (useful for dashboards)
CREATE OR REPLACE VIEW trip.v_trip_latest_event AS
SELECT DISTINCT ON (e.trip_id)
    e.trip_id,
    e.event_id,
    e.event_type,
    e.occurred_at
FROM trip.trip_events e
ORDER BY e.trip_id, e.occurred_at DESC, e.event_id DESC;