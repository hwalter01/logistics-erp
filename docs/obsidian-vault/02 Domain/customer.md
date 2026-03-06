# Customer

## Description

Represents a logistics customer that can place transport orders.

Each customer has exactly one primary address.

---

## Attributes

|Field|Type|Not Null|Unique|Description|
|---|---|---|---|---|
|customer_id|UUID|yes|yes (PK)|Internal identifier|
|customer_number|VARCHAR(50)|yes|yes|Business identifier|
|name|VARCHAR(255)|yes|no|Customer company name|
|vat_number|VARCHAR(100)|no|no|VAT number|
|contact_email|VARCHAR(255)|no|no|Contact email|
|contact_phone|VARCHAR(50)|no|no|Contact phone|
|notes|TEXT|no|no|Internal notes|
|pod_required|BOOLEAN|yes|no|Whether proof of delivery is mandatory|
|address_id|UUID|yes|no|FK to Address|
|created_at|TIMESTAMPTZ|yes|no|Creation timestamp|
|updated_at|TIMESTAMPTZ|yes|no|Last update timestamp|
|row_version|BIGINT|yes|no|Optimistic locking version|

---

## Relationships

| Relationship        | Target   | Type        |
| ------------------- | -------- | ----------- |
| Customer → Address  | Address  | Many-to-One |
| Location → Customer | Customer | Many-to-One |