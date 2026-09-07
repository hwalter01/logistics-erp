SET search_path TO md;

-- Locations must be removed first because they reference
-- customers and addresses.
DELETE FROM locations
WHERE location_id::text LIKE
      'f9f9f9f9-9999-9999-9999-9999999997%';

-- Drivers reference addresses.
DELETE FROM drivers
WHERE driver_number LIKE 'TEST-DRV-%';

-- Customers reference addresses.
DELETE FROM customers
WHERE customer_number LIKE 'TEST-CUST-%';

-- Vehicles and trailers are independent.
DELETE FROM vehicles
WHERE vehicle_number LIKE 'TEST-TRUCK-%';

DELETE FROM trailers
WHERE trailer_number LIKE 'TEST-TRL-%';

-- Remove the fixed test addresses last.
DELETE FROM addresses
WHERE address_id IN (
                     'f9f9f9f9-9999-9999-9999-999999999901',
                     'f9f9f9f9-9999-9999-9999-999999999902',
                     'f9f9f9f9-9999-9999-9999-999999999903',
                     'f9f9f9f9-9999-9999-9999-999999999904',
                     'f9f9f9f9-9999-9999-9999-999999999905',
                     'f9f9f9f9-9999-9999-9999-999999999906',
                     'f9f9f9f9-9999-9999-9999-999999999907',
                     'f9f9f9f9-9999-9999-9999-999999999908',
                     'f9f9f9f9-9999-9999-9999-999999999909',
                     'f9f9f9f9-9999-9999-9999-999999999910'
    );