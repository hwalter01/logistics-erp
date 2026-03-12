# Trailer

## Description

Represents a trailer or semi-trailer.

Note:  
License plates are **not unique**, because logistics companies often reuse plates when trailers are replaced.

---

## Attributes

|Field|Type|Not Null|Unique|Description|
|---|---|---|---|---|
|trailer_id|UUID|yes|yes (PK)|Internal identifier|
|trailer_number|VARCHAR(50)|yes|yes|Business identifier|
|license_plate|VARCHAR(50)|yes|no|Trailer license plate|
|trailer_type|VARCHAR(50)|no|no|Trailer type|
|status|VARCHAR(50)|yes|no|Trailer status|
|notes|TEXT|no|no|Internal notes|
|created_at|TIMESTAMPTZ|yes|no|Creation timestamp|
|updated_at|TIMESTAMPTZ|yes|no|Last update timestamp|
|row_version|BIGINT|yes|no|Optimistic locking|

---

## Enums

### TrailerStatus

ACTIVE  
MAINTENANCE  
OUT_OF_SERVICE