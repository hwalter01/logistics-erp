# Location

## Description

Represents a physical location belonging to a customer.

Examples:

- warehouse
    
- factory
    
- delivery location
    
- pickup site
    

---

## Attributes

|Field|Type|Not Null|Unique|Description|
|---|---|---|---|---|
|location_id|UUID|yes|yes (PK)|Internal identifier|
|customer_id|UUID|yes|no|FK to Customer|
|address_id|UUID|yes|no|FK to Address|
|name|VARCHAR(255)|yes|no|Location name|
|contact_person|VARCHAR(255)|no|no|On-site contact|
|contact_phone|VARCHAR(50)|no|no|Contact phone|
|contact_email|VARCHAR(255)|no|no|Contact email|
|site_instructions|TEXT|no|no|Instructions for drivers|
|created_at|TIMESTAMPTZ|yes|no|Creation timestamp|
|updated_at|TIMESTAMPTZ|yes|no|Last update timestamp|
|row_version|BIGINT|yes|no|Optimistic locking|

---

## Relationships

| Relationship        | Target   | Type        |
| ------------------- | -------- | ----------- |
| Location → Customer | Customer | Many-to-One |
| Location → Address  | Address  | Many-to-One |