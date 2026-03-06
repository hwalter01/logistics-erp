# Vehicle

## Description

Represents a truck or tractor unit.

---

## Attributes

|Field|Type|Not Null|Unique|Description|
|---|---|---|---|---|
|vehicle_id|UUID|yes|yes (PK)|Internal identifier|
|vehicle_number|VARCHAR(50)|yes|yes|Business identifier|
|license_plate|VARCHAR(50)|yes|yes|Vehicle license plate|
|vin|VARCHAR(100)|no|no|Vehicle identification number|
|brand|VARCHAR(100)|no|no|Manufacturer|
|model|VARCHAR(100)|no|no|Model|
|status|VARCHAR(50)|yes|no|Vehicle status|
|notes|TEXT|no|no|Internal notes|
|created_at|TIMESTAMPTZ|yes|no|Creation timestamp|
|updated_at|TIMESTAMPTZ|yes|no|Last update timestamp|
|row_version|BIGINT|yes|no|Optimistic locking|

---

## Enums

### VehicleStatus

ACTIVE  
MAINTENANCE  
OUT_OF_SERVICE