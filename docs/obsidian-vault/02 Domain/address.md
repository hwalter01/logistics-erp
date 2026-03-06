# Address

## Description

Stores physical addresses used by customers, locations, and drivers.

Addresses are stored centrally to avoid duplication and allow reuse.

---

## Attributes

|Field|Type|Not Null|Unique|Description|
|---|---|---|---|---|
|address_id|UUID|yes|yes (PK)|Unique identifier|
|street|VARCHAR(255)|yes|no|Street name|
|house_number|VARCHAR(50)|yes|no|House number|
|postal_code|VARCHAR(20)|yes|no|Postal code|
|city|VARCHAR(100)|yes|no|City|
|country|VARCHAR(100)|yes|no|Country code or name|
|additional_line|VARCHAR(255)|no|no|Optional additional address line|
|created_at|TIMESTAMPTZ|yes|no|Creation timestamp|
|updated_at|TIMESTAMPTZ|yes|no|Last update timestamp|
|row_version|BIGINT|yes|no|Optimistic locking version|

---

## Relationships

| Relationship       | Target  | Type        |
| ------------------ | ------- | ----------- |
| Customer → Address | Address | Many-to-One |
| Driver → Address   | Address | Many-to-One |
| Location → Address | Address | Many-to-One |