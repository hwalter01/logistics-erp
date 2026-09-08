CREATE SCHEMA IF NOT EXISTS md;

CREATE TABLE md.addresses (
                              address_id UUID PRIMARY KEY,
                              street VARCHAR(255) NOT NULL,
                              house_number VARCHAR(50) NOT NULL,
                              postal_code VARCHAR(20) NOT NULL,
                              city VARCHAR(100) NOT NULL,
                              country VARCHAR(100) NOT NULL,
                              additional_line VARCHAR(255),
                              created_at TIMESTAMPTZ NOT NULL,
                              updated_at TIMESTAMPTZ NOT NULL,
                              row_version BIGINT NOT NULL
);

CREATE TABLE md.customers (
                              customer_id UUID PRIMARY KEY,
                              customer_number VARCHAR(50) NOT NULL,
                              name VARCHAR(255) NOT NULL,
                              vat_number VARCHAR(100),
                              contact_email VARCHAR(255),
                              contact_phone VARCHAR(50),
                              notes TEXT,
                              pod_required BOOLEAN NOT NULL,
                              address_id UUID NOT NULL,
                              created_at TIMESTAMPTZ NOT NULL,
                              updated_at TIMESTAMPTZ NOT NULL,
                              row_version BIGINT NOT NULL,

                              CONSTRAINT fk_customers_address
                                  FOREIGN KEY (address_id)
                                      REFERENCES md.addresses(address_id),

                              CONSTRAINT ux_customers_customer_number
                                  UNIQUE (customer_number)
);

CREATE TABLE md.locations (
                              location_id UUID PRIMARY KEY,
                              customer_id UUID NOT NULL,
                              address_id UUID NOT NULL,
                              name VARCHAR(255) NOT NULL,
                              contact_person VARCHAR(255),
                              contact_phone VARCHAR(50),
                              contact_email VARCHAR(255),
                              site_instructions TEXT,
                              created_at TIMESTAMPTZ NOT NULL,
                              updated_at TIMESTAMPTZ NOT NULL,
                              row_version BIGINT NOT NULL,

                              CONSTRAINT fk_locations_customer
                                  FOREIGN KEY (customer_id)
                                      REFERENCES md.customers(customer_id),

                              CONSTRAINT fk_locations_address
                                  FOREIGN KEY (address_id)
                                      REFERENCES md.addresses(address_id)
);

CREATE TABLE md.drivers (
                            driver_id UUID PRIMARY KEY,
                            driver_number VARCHAR(50) NOT NULL,
                            first_name VARCHAR(100) NOT NULL,
                            last_name VARCHAR(100) NOT NULL,
                            phone VARCHAR(50),
                            email VARCHAR(255),
                            license_number VARCHAR(100) NOT NULL,
                            employment_type VARCHAR(50) NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            address_id UUID NOT NULL,
                            created_at TIMESTAMPTZ NOT NULL,
                            updated_at TIMESTAMPTZ NOT NULL,
                            row_version BIGINT NOT NULL,

                            CONSTRAINT fk_drivers_address
                                FOREIGN KEY (address_id)
                                    REFERENCES md.addresses(address_id),

                            CONSTRAINT ux_drivers_driver_number
                                UNIQUE (driver_number)
);

CREATE TABLE md.vehicles (
                             vehicle_id UUID PRIMARY KEY,
                             vehicle_number VARCHAR(50) NOT NULL,
                             license_plate VARCHAR(50) NOT NULL,
                             vin VARCHAR(100),
                             brand VARCHAR(100),
                             model VARCHAR(100),
                             status VARCHAR(50) NOT NULL,
                             notes TEXT,
                             created_at TIMESTAMPTZ NOT NULL,
                             updated_at TIMESTAMPTZ NOT NULL,
                             row_version BIGINT NOT NULL,

                             CONSTRAINT ux_vehicles_vehicle_number
                                 UNIQUE (vehicle_number)
);

CREATE TABLE md.trailers (
                             trailer_id UUID PRIMARY KEY,
                             trailer_number VARCHAR(50) NOT NULL,
                             license_plate VARCHAR(50) NOT NULL,
                             trailer_type VARCHAR(50),
                             status VARCHAR(50) NOT NULL,
                             notes TEXT,
                             created_at TIMESTAMPTZ NOT NULL,
                             updated_at TIMESTAMPTZ NOT NULL,
                             row_version BIGINT NOT NULL,

                             CONSTRAINT ux_trailers_trailer_number
                                 UNIQUE (trailer_number)
);