# Driver

## Description

Represents a driver working for the logistics company.

Drivers can either be:

- employees
    
- subcontractors
    

---

## Attributes

|Field|Type|Not Null|Unique|Description|
|---|---|---|---|---|
|driver_id|UUID|yes|yes (PK)|Internal identifier|
|driver_number|VARCHAR(50)|yes|yes|Business identifier|
|first_name|VARCHAR(100)|yes|no|First name|
|last_name|VARCHAR(100)|yes|no|Last name|
|phone|VARCHAR(50)|no|no|Phone number|
|email|VARCHAR(255)|no|no|Email|
|license_number|VARCHAR(100)|yes|no|Driver license number|
|employment_type|VARCHAR(50)|yes|no|Employment type|
|status|VARCHAR(50)|yes|no|Driver status|
|address_id|UUID|yes|no|FK to Address|
|created_at|TIMESTAMPTZ|yes|no|Creation timestamp|
|updated_at|TIMESTAMPTZ|yes|no|Last update timestamp|
|row_version|BIGINT|yes|no|Optimistic locking|

---

## Enums

### DriverStatus

ACTIVE  
INACTIVE  
ON_LEAVE  
SICK

---

### EmploymentType

EMPLOYEE  
SUBCONTRACTOR

---

## Relationships

| Relationship     | Target  | Type        |
| ---------------- | ------- | ----------- |
| Driver → Address | Address | Many-to-One |