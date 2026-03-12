SET search_path TO md;

-- =========================================
-- V3__test_master_data.sql
-- Test data for master data service
-- Clearly identifiable by TEST / z9 UUIDs
-- =========================================

-- -------------------------
-- ADDRESSES
-- -------------------------

INSERT INTO addresses (
    address_id,
    street,
    house_number,
    postal_code,
    city,
    country,
    additional_line,
    created_at,
    updated_at,
    row_version
)
VALUES
    ('f9f9f9f9-9999-9999-9999-999999999901', 'Test Street',      '11', '99101', 'Test City Alpha',   'Test Country', 'Test address alpha',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999902', 'Mock Avenue',      '22', '99102', 'Test City Beta',    'Test Country', 'Test address beta',    NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999903', 'Dummy Road',       '33', '99103', 'Test City Gamma',   'Test Country', 'Test address gamma',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999904', 'QA Boulevard',     '44', '99104', 'Test City Delta',   'Test Country', 'Test address delta',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999905', 'Integration Lane', '55', '99105', 'Test City Epsilon', 'Test Country', 'Test address epsilon', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999906', 'Fixture Way',      '66', '99106', 'Test City Zeta',    'Test Country', 'Test address zeta',    NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999907', 'Sample Park',      '77', '99107', 'Test City Eta',     'Test Country', 'Test address eta',     NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999908', 'Assert Street',    '88', '99108', 'Test City Theta',   'Test Country', 'Test address theta',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999909', 'Verify Alley',     '99', '99109', 'Test City Iota',    'Test Country', 'Test address iota',    NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999910', 'Spec Road',       '100', '99110', 'Test City Kappa',   'Test Country', 'Test address kappa',   NOW(), NOW(), 0);

-- -------------------------
-- CUSTOMERS
-- -------------------------

INSERT INTO customers (
    customer_id,
    customer_number,
    name,
    vat_number,
    contact_email,
    contact_phone,
    notes,
    pod_required,
    address_id,
    created_at,
    updated_at,
    row_version
)
VALUES
    ('f9f9f9f9-9999-9999-9999-999999999801', 'TEST-CUST-2001', 'Test Customer Alpha GmbH',   'TEST-VAT-2001', 'alpha@test.example',   '+49 1000 000001', 'Test customer alpha',   true,  'f9f9f9f9-9999-9999-9999-999999999901', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999802', 'TEST-CUST-2002', 'Test Customer Beta GmbH',    'TEST-VAT-2002', 'beta@test.example',    '+49 1000 000002', 'Test customer beta',    false, 'f9f9f9f9-9999-9999-9999-999999999902', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999803', 'TEST-CUST-2003', 'Test Customer Gamma GmbH',   'TEST-VAT-2003', 'gamma@test.example',   '+49 1000 000003', 'Test customer gamma',   true,  'f9f9f9f9-9999-9999-9999-999999999903', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999804', 'TEST-CUST-2004', 'Test Customer Delta GmbH',   'TEST-VAT-2004', 'delta@test.example',   '+49 1000 000004', 'Test customer delta',   false, 'f9f9f9f9-9999-9999-9999-999999999904', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999805', 'TEST-CUST-2005', 'Test Customer Epsilon GmbH', 'TEST-VAT-2005', 'epsilon@test.example', '+49 1000 000005', 'Test customer epsilon', true,  'f9f9f9f9-9999-9999-9999-999999999905', NOW(), NOW(), 0);

-- -------------------------
-- LOCATIONS
-- -------------------------

INSERT INTO locations (
    location_id,
    customer_id,
    address_id,
    name,
    contact_person,
    contact_phone,
    contact_email,
    site_instructions,
    created_at,
    updated_at,
    row_version
)
VALUES
    ('f9f9f9f9-9999-9999-9999-999999999701', 'f9f9f9f9-9999-9999-9999-999999999801', 'f9f9f9f9-9999-9999-9999-999999999906', 'Test Customer Alpha Warehouse',   'Test Dispatcher Alpha',   '+49 2000 000001', 'dispatch.alpha@test.example',   'TEST INSTRUCTION: Report to Gate A', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999702', 'f9f9f9f9-9999-9999-9999-999999999802', 'f9f9f9f9-9999-9999-9999-999999999907', 'Test Customer Beta Warehouse',    'Test Dispatcher Beta',    '+49 2000 000002', 'dispatch.beta@test.example',    'TEST INSTRUCTION: Report to Gate B', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999703', 'f9f9f9f9-9999-9999-9999-999999999803', 'f9f9f9f9-9999-9999-9999-999999999908', 'Test Customer Gamma Warehouse',   'Test Dispatcher Gamma',   '+49 2000 000003', 'dispatch.gamma@test.example',   'TEST INSTRUCTION: Report to Gate C', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999704', 'f9f9f9f9-9999-9999-9999-999999999804', 'f9f9f9f9-9999-9999-9999-999999999909', 'Test Customer Delta Warehouse',   'Test Dispatcher Delta',   '+49 2000 000004', 'dispatch.delta@test.example',   'TEST INSTRUCTION: Report to Gate D', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999705', 'f9f9f9f9-9999-9999-9999-999999999805', 'f9f9f9f9-9999-9999-9999-999999999910', 'Test Customer Epsilon Warehouse', 'Test Dispatcher Epsilon', '+49 2000 000005', 'dispatch.epsilon@test.example', 'TEST INSTRUCTION: Report to Gate E', NOW(), NOW(), 0);

-- -------------------------
-- DRIVERS
-- -------------------------

INSERT INTO drivers (
    driver_id,
    driver_number,
    first_name,
    last_name,
    phone,
    email,
    license_number,
    employment_type,
    status,
    address_id,
    created_at,
    updated_at,
    row_version
)
VALUES
    ('f9f9f9f9-9999-9999-9999-999999999601', 'TEST-DRV-2001', 'Test',   'DriverAlpha',   '+49 3000 000001', 'driver.alpha@test.example',   'TEST-LIC-2001', 'EMPLOYEE',   'ACTIVE',   'f9f9f9f9-9999-9999-9999-999999999901', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999602', 'TEST-DRV-2002', 'Mock',   'DriverBeta',    '+49 3000 000002', 'driver.beta@test.example',    'TEST-LIC-2002', 'EMPLOYEE',   'ACTIVE',   'f9f9f9f9-9999-9999-9999-999999999902', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999603', 'TEST-DRV-2003', 'Dummy',  'DriverGamma',   '+49 3000 000003', 'driver.gamma@test.example',   'TEST-LIC-2003', 'CONTRACTOR', 'ACTIVE',   'f9f9f9f9-9999-9999-9999-999999999903', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999604', 'TEST-DRV-2004', 'QA',     'DriverDelta',   '+49 3000 000004', 'driver.delta@test.example',   'TEST-LIC-2004', 'EMPLOYEE',   'INACTIVE', 'f9f9f9f9-9999-9999-9999-999999999904', NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999605', 'TEST-DRV-2005', 'Assert', 'DriverEpsilon', '+49 3000 000005', 'driver.epsilon@test.example', 'TEST-LIC-2005', 'EMPLOYEE',   'ACTIVE',   'f9f9f9f9-9999-9999-9999-999999999905', NOW(), NOW(), 0);

-- -------------------------
-- VEHICLES
-- -------------------------

INSERT INTO vehicles (
    vehicle_id,
    vehicle_number,
    license_plate,
    vin,
    brand,
    model,
    status,
    notes,
    created_at,
    updated_at,
    row_version
)
VALUES
    ('f9f9f9f9-9999-9999-9999-999999999501', 'TEST-TRUCK-2001', 'TEST-HH-1001', 'TEST-VIN-1001', 'TestBrand', 'TestModel A', 'ACTIVE',      'Test vehicle alpha',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999502', 'TEST-TRUCK-2002', 'TEST-HH-1002', 'TEST-VIN-1002', 'TestBrand', 'TestModel B', 'ACTIVE',      'Test vehicle beta',    NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999503', 'TEST-TRUCK-2003', 'TEST-HH-1003', 'TEST-VIN-1003', 'TestBrand', 'TestModel C', 'MAINTENANCE', 'Test vehicle gamma',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999504', 'TEST-TRUCK-2004', 'TEST-HH-1004', 'TEST-VIN-1004', 'TestBrand', 'TestModel D', 'ACTIVE',      'Test vehicle delta',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999505', 'TEST-TRUCK-2005', 'TEST-HH-1005', 'TEST-VIN-1005', 'TestBrand', 'TestModel E', 'INACTIVE',    'Test vehicle epsilon', NOW(), NOW(), 0);

-- -------------------------
-- TRAILERS
-- -------------------------

INSERT INTO trailers (
    trailer_id,
    trailer_number,
    license_plate,
    trailer_type,
    status,
    notes,
    created_at,
    updated_at,
    row_version
)
VALUES
    ('f9f9f9f9-9999-9999-9999-999999999401', 'TEST-TRL-2001', 'TEST-TR-1001', 'BOX',     'ACTIVE',      'Test trailer alpha',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999402', 'TEST-TRL-2002', 'TEST-TR-1002', 'REEFER',  'ACTIVE',      'Test trailer beta',    NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999403', 'TEST-TRL-2003', 'TEST-TR-1003', 'TANK',    'MAINTENANCE', 'Test trailer gamma',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999404', 'TEST-TRL-2004', 'TEST-TR-1004', 'FLATBED', 'ACTIVE',      'Test trailer delta',   NOW(), NOW(), 0),
    ('f9f9f9f9-9999-9999-9999-999999999405', 'TEST-TRL-2005', 'TEST-TR-1005', 'BOX',     'INACTIVE',    'Test trailer epsilon', NOW(), NOW(), 0);