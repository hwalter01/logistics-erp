SET search_path TO md;

-- -------------------------
-- ADDRESSES
-- -------------------------

INSERT INTO addresses (address_id, street, house_number, postal_code, city, country, additional_line, created_at, updated_at, row_version)
VALUES
    ('a1a1a1a1-0000-0000-0000-000000000001','Industriestrasse','10','20095','Hamburg','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000002','Hafenstrasse','25','20457','Hamburg','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000003','Logistikweg','3','80331','Munich','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000004','Industriepark','12','40210','Düsseldorf','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000005','Nordallee','5','28195','Bremen','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000006','Transportweg','99','10115','Berlin','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000007','Chemiepark','7','67059','Ludwigshafen','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000008','Warehouse Road','15','50667','Cologne','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000009','Cargo Center','44','44135','Dortmund','Germany',NULL,NOW(),NOW(),0),
    ('a1a1a1a1-0000-0000-0000-000000000010','Dockyard Lane','8','27568','Bremerhaven','Germany',NULL,NOW(),NOW(),0);

-- -------------------------
-- CUSTOMERS
-- -------------------------

INSERT INTO customers (customer_id, customer_number, name, vat_number, contact_email, contact_phone, notes, pod_required, address_id, created_at, updated_at, row_version)
VALUES
    (gen_random_uuid(),'CUST-2001','NordSteel GmbH','DE123456001','logistics@nordsteel.de','040123456',NULL,true,'a1a1a1a1-0000-0000-0000-000000000001',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2002','NordSteel Logistics','DE123456002','transport@nordsteel.de','040234567',NULL,false,'a1a1a1a1-0000-0000-0000-000000000002',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2003','Bavaria Chemicals','DE123456003','shipping@bavchem.de','089123456',NULL,true,'a1a1a1a1-0000-0000-0000-000000000003',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2004','Rhein Logistics','DE123456004','rhein@logistics.de','021155555',NULL,false,'a1a1a1a1-0000-0000-0000-000000000004',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2005','NordWind Energy','DE123456005','supply@nordwind.de','042188888',NULL,true,'a1a1a1a1-0000-0000-0000-000000000005',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2006','Berlin Distribution','DE123456006','dispatch@berlin.de','030111111',NULL,false,'a1a1a1a1-0000-0000-0000-000000000006',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2007','NordTransport Services','DE123456007','ops@nordtransport.de','040777777',NULL,true,'a1a1a1a1-0000-0000-0000-000000000007',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2008','Hamburg Warehousing','DE123456008','warehouse@hamburg.de','040333333',NULL,false,'a1a1a1a1-0000-0000-0000-000000000008',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2009','NordCargo International','DE123456009','cargo@nordcargo.de','040999999',NULL,true,'a1a1a1a1-0000-0000-0000-000000000009',NOW(),NOW(),0),
    (gen_random_uuid(),'CUST-2010','Munich Automotive','DE123456010','logistics@munich-auto.de','089444444',NULL,false,'a1a1a1a1-0000-0000-0000-000000000010',NOW(),NOW(),0);

-- -------------------------
-- LOCATIONS
-- -------------------------

INSERT INTO locations (location_id, customer_id, address_id, name, contact_person, contact_phone, contact_email, site_instructions, created_at, updated_at, row_version)
SELECT gen_random_uuid(), c.customer_id, c.address_id,
       c.name || ' Warehouse',
       'Dispatch Office',
       '000000',
       'dispatch@company.com',
       'Report to security gate',
       NOW(), NOW(), 0
FROM customers c
    LIMIT 10;

-- -------------------------
-- DRIVERS
-- -------------------------

INSERT INTO drivers (driver_id, driver_number, first_name, last_name, phone, email, license_number, employment_type, status, address_id, created_at, updated_at, row_version)
VALUES
    (gen_random_uuid(),'DRV-2001','Max','Müller','0170111111','max.mueller@company.de','LIC1001','EMPLOYEE','ACTIVE','a1a1a1a1-0000-0000-0000-000000000001',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2002','Jonas','Schmidt','0170222222','jonas.schmidt@company.de','LIC1002','EMPLOYEE','ACTIVE','a1a1a1a1-0000-0000-0000-000000000002',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2003','Peter','Weber','0170333333','p.weber@company.de','LIC1003','CONTRACTOR','ACTIVE','a1a1a1a1-0000-0000-0000-000000000003',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2004','Lukas','Fischer','0170444444','l.fischer@company.de','LIC1004','EMPLOYEE','ACTIVE','a1a1a1a1-0000-0000-0000-000000000004',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2005','Tim','Wagner','0170555555','t.wagner@company.de','LIC1005','EMPLOYEE','INACTIVE','a1a1a1a1-0000-0000-0000-000000000005',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2006','David','Becker','0170666666','d.becker@company.de','LIC1006','CONTRACTOR','ACTIVE','a1a1a1a1-0000-0000-0000-000000000006',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2007','Jan','Hoffmann','0170777777','j.hoffmann@company.de','LIC1007','EMPLOYEE','ACTIVE','a1a1a1a1-0000-0000-0000-000000000007',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2008','Leon','Schäfer','0170888888','l.schaefer@company.de','LIC1008','EMPLOYEE','ACTIVE','a1a1a1a1-0000-0000-0000-000000000008',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2009','Felix','Koch','0170999999','f.koch@company.de','LIC1009','EMPLOYEE','ACTIVE','a1a1a1a1-0000-0000-0000-000000000009',NOW(),NOW(),0),
    (gen_random_uuid(),'DRV-2010','Tom','Richter','0170123456','t.richter@company.de','LIC1010','EMPLOYEE','ACTIVE','a1a1a1a1-0000-0000-0000-000000000010',NOW(),NOW(),0);

-- -------------------------
-- VEHICLES
-- -------------------------

INSERT INTO vehicles (vehicle_id, vehicle_number, license_plate, vin, brand, model, status, notes, created_at, updated_at, row_version)
VALUES
    (gen_random_uuid(),'TRUCK-2001','HH-LG-1001','VIN1001','Mercedes','Actros','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2002','HH-LG-1002','VIN1002','MAN','TGX','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2003','HH-LG-1003','VIN1003','Volvo','FH16','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2004','HH-LG-1004','VIN1004','Scania','R500','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2005','HH-LG-1005','VIN1005','DAF','XF','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2006','HH-LG-1006','VIN1006','Mercedes','Actros','MAINTENANCE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2007','HH-LG-1007','VIN1007','MAN','TGX','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2008','HH-LG-1008','VIN1008','Volvo','FH','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2009','HH-LG-1009','VIN1009','Scania','S500','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRUCK-2010','HH-LG-1010','VIN1010','DAF','XF','ACTIVE',NULL,NOW(),NOW(),0);

-- -------------------------
-- TRAILERS
-- -------------------------

INSERT INTO trailers (trailer_id, trailer_number, license_plate, trailer_type, status, notes, created_at, updated_at, row_version)
VALUES
    (gen_random_uuid(),'TRL-2001','HH-TR-1001','BOX','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2002','HH-TR-1002','REEFER','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2003','HH-TR-1003','TANK','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2004','HH-TR-1004','FLATBED','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2005','HH-TR-1005','BOX','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2006','HH-TR-1006','BOX','MAINTENANCE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2007','HH-TR-1007','REEFER','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2008','HH-TR-1008','TANK','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2009','HH-TR-1009','FLATBED','ACTIVE',NULL,NOW(),NOW(),0),
    (gen_random_uuid(),'TRL-2010','HH-TR-1010','BOX','ACTIVE',NULL,NOW(),NOW(),0);