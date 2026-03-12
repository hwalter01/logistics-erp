# Master Data API

## Overview

The Master Data Service manages the core reference data used by the logistics platform.

It is responsible for the lifecycle of the following entities:

- Address
- Customer
- Location
- Driver
- Vehicle
- Trailer

This service provides CRUD operations, pagination, sorting, and search/filter capabilities for these master data entities.

---

## Base URL
/api/v1
## Common Concepts

### Pagination

List endpoints support pagination via standard query parameters.

|Parameter|Type|Required|Description|
|---|---|---|---|
|`page`|integer|no|Zero-based page index|
|`size`|integer|no|Page size|
|`sort`|string|no|Sort field and direction, e.g. `name,asc`|

### Pagination Example

`GET /api/v1/customers?page=0&size=20&sort=customerNumber,asc`

### Paginated Response Format

```
{  
  "content": [],  
  "page": 0,  
  "size": 20,  
  "totalElements": 0,  
  "totalPages": 0,  
  "first": true,  
  "last": true  
}
```

---

### Filtering

Search endpoints support optional query parameters.

All provided filters are combined using logical `AND`.

Example:

`GET /api/v1/customers?name=Nord&podRequired=true` 

This returns only customers whose name matches `Nord` and whose `podRequired` flag is `true`.

---

### Error Handling

The service returns structured error responses.

### Example Error Response

```{  
  "timestamp": "2026-03-07T10:22:31.123Z",  
  "status": 404,  
  "error": "Not Found",  
  "message": "Customer not found: 11111111-1111-1111-1111-111111111111",  
  "path": "/api/v1/customers/11111111-1111-1111-1111-111111111111"  
}
```

### Common Status Codes

|Status|Meaning|
|---|---|
|`200 OK`|Successful read/update/delete|
|`201 Created`|Successful creation|
|`400 Bad Request`|Validation error or malformed request|
|`404 Not Found`|Resource does not exist|
|`409 Conflict`|Duplicate business key|
|`500 Internal Server Error`|Unexpected server error|

---

# Address API

## Address Entity Fields

|Field|Type|Required|
|---|---|---|
|`street`|string|yes|
|`houseNumber`|string|yes|
|`postalCode`|string|yes|
|`city`|string|yes|
|`country`|string|yes|
|`additionalLine`|string|no|

---

## Create Address

**POST** `/api/v1/addresses`

### Request Body

```
{  
  "street": "Hafenstraße",  
  "houseNumber": "10",  
  "postalCode": "20095",  
  "city": "Hamburg",  
  "country": "Germany",  
  "additionalLine": "Warehouse Entrance B"  
}
```
### Response

```
{  
  "addressId": "f0616a1a-95eb-4b6b-9151-92427595a4c6",  
  "street": "Hafenstraße",  
  "houseNumber": "10",  
  "postalCode": "20095",  
  "city": "Hamburg",  
  "country": "Germany",  
  "additionalLine": "Warehouse Entrance B"  
}
```
---

## Get Address by ID

**GET** `/api/v1/addresses/{addressId}`

### Response

```
{  
  "addressId": "f0616a1a-95eb-4b6b-9151-92427595a4c6",  
  "street": "Hafenstraße",  
  "houseNumber": "10",  
  "postalCode": "20095",  
  "city": "Hamburg",  
  "country": "Germany",  
  "additionalLine": "Warehouse Entrance B"  
}

```

---

## Search Addresses

**GET** `/api/v1/addresses`

### Supported Query Parameters

|Parameter|Type|Required|Description|
|---|---|---|---|
|`street`|string|no|Partial match|
|`houseNumber`|string|no|Exact match|
|`postalCode`|string|no|Exact match|
|`city`|string|no|Partial match|
|`country`|string|no|Partial match|
|`page`|integer|no|Page index|
|`size`|integer|no|Page size|
|`sort`|string|no|Sort criteria|

### Example

`GET /api/v1/addresses?city=Hamburg&page=0&size=10&sort=street,asc`

---

## Update Address

**PUT** `/api/v1/addresses/{addressId}`

### Request Body

```
{  
  "street": "Hafenstraße",  
  "houseNumber": "12A",  
  "postalCode": "20095",  
  "city": "Hamburg",  
  "country": "Germany",  
  "additionalLine": "Updated entrance"  
}
```

---

## Delete Address

**DELETE** `/api/v1/addresses/{addressId}`

---

# Customer API

## Customer Entity Fields

|Field|Type|Required|
|---|---|---|
|`customerNumber`|string|yes|
|`name`|string|yes|
|`vatNumber`|string|no|
|`contactEmail`|string|no|
|`contactPhone`|string|no|
|`notes`|string|no|
|`podRequired`|boolean|yes|
|`addressId`|UUID|yes|

---

## Create Customer

**POST** `/api/v1/customers`

### Request Body

```
{  
  "customerNumber": "CUST-1001",  
  "name": "NordSteel GmbH",  
  "vatNumber": "DE123456789",  
  "contactEmail": "logistics@nordsteel.de",  
  "contactPhone": "+49 40 1234567",  
  "notes": "POD required for all deliveries",  
  "podRequired": true,  
  "addressId": "f0616a1a-95eb-4b6b-9151-92427595a4c6"  
}
```

### Response

```
{  
  "customerId": "11111111-1111-1111-1111-111111111111",  
  "customerNumber": "CUST-1001",  
  "name": "NordSteel GmbH",  
  "vatNumber": "DE123456789",  
  "contactEmail": "logistics@nordsteel.de",  
  "contactPhone": "+49 40 1234567",  
  "notes": "POD required for all deliveries",  
  "podRequired": true,  
  "addressId": "f0616a1a-95eb-4b6b-9151-92427595a4c6"  
}
```

---

## Get Customer by ID

**GET** `/api/v1/customers/{customerId}`

---

## Search Customers

**GET** `/api/v1/customers`

### Supported Query Parameters

|Parameter|Type|Required|Description|
|---|---|---|---|
|`customerNumber`|string|no|Exact match|
|`vatNumber`|string|no|Exact match|
|`name`|string|no|Case-insensitive partial match|
|`podRequired`|boolean|no|Exact match|
|`page`|integer|no|Page index|
|`size`|integer|no|Page size|
|`sort`|string|no|Sort criteria|

### Example

`GET /api/v1/customers?name=Nord&podRequired=true&page=0&size=10&sort=customerNumber,asc`

---

## Update Customer

**PUT** `/api/v1/customers/{customerId}`

### Request Body

```
{  
  "customerNumber": "CUST-1001",  
  "name": "NordSteel GmbH Updated",  
  "vatNumber": "DE123456789",  
  "contactEmail": "updated@nordsteel.de",  
  "contactPhone": "+49 40 999999",  
  "notes": "Updated customer notes",  
  "podRequired": true,  
  "addressId": "f0616a1a-95eb-4b6b-9151-92427595a4c6"  
}
```

---

## Delete Customer

**DELETE** `/api/v1/customers/{customerId}`

---

# Driver API

## Driver Entity Fields

|Field|Type|Required|
|---|---|---|
|`driverNumber`|string|yes|
|`firstName`|string|yes|
|`lastName`|string|yes|
|`phone`|string|no|
|`email`|string|no|
|`licenseNumber`|string|yes|
|`employmentType`|enum|yes|
|`status`|enum|yes|
|`addressId`|UUID|yes|

---

## Create Driver

**POST** `/api/v1/drivers`

### Request Body

```
{  
  "driverNumber": "DRV-1001",  
  "firstName": "Max",  
  "lastName": "Müller",  
  "phone": "+49 171 1234567",  
  "email": "max.mueller@company.de",  
  "licenseNumber": "LIC1001",  
  "employmentType": "EMPLOYEE",  
  "status": "ACTIVE",  
  "addressId": "f0616a1a-95eb-4b6b-9151-92427595a4c6"  
}
```

---

## Get Driver by ID

**GET** `/api/v1/drivers/{driverId}`

---

## Search Drivers

**GET** `/api/v1/drivers`

### Supported Query Parameters

|Parameter|Type|Required|Description|
|---|---|---|---|
|`driverNumber`|string|no|Exact match|
|`firstName`|string|no|Partial match|
|`lastName`|string|no|Partial match|
|`status`|enum|no|Exact match|
|`employmentType`|enum|no|Exact match|
|`page`|integer|no|Page index|
|`size`|integer|no|Page size|
|`sort`|string|no|Sort criteria|

### Example

`GET /api/v1/drivers?status=ACTIVE&employmentType=EMPLOYEE&page=0&size=10&sort=driverNumber,asc`

---

## Update Driver

**PUT** `/api/v1/drivers/{driverId}`

---

## Delete Driver

**DELETE** `/api/v1/drivers/{driverId}`

---

# Vehicle API

## Vehicle Entity Fields

|Field|Type|Required|
|---|---|---|
|`vehicleNumber`|string|yes|
|`licensePlate`|string|yes|
|`vin`|string|no|
|`brand`|string|no|
|`model`|string|no|
|`status`|enum|yes|
|`notes`|string|no|

---

## Create Vehicle

**POST** `/api/v1/vehicles`

### Request Body

```
{  
  "vehicleNumber": "TRUCK-1001",  
  "licensePlate": "HH-LG-1001",  
  "vin": "VIN1001",  
  "brand": "Mercedes",  
  "model": "Actros",  
  "status": "ACTIVE",  
  "notes": "Long-haul truck"  
}
```

---

## Get Vehicle by ID

**GET** `/api/v1/vehicles/{vehicleId}`

---

## Search Vehicles

**GET** `/api/v1/vehicles`

### Supported Query Parameters

|Parameter|Type|Required|Description|
|---|---|---|---|
|`vehicleNumber`|string|no|Exact match|
|`licensePlate`|string|no|Exact match|
|`brand`|string|no|Partial match|
|`model`|string|no|Partial match|
|`status`|enum|no|Exact match|
|`page`|integer|no|Page index|
|`size`|integer|no|Page size|
|`sort`|string|no|Sort criteria|

### Example

`GET /api/v1/vehicles?status=ACTIVE&brand=Mercedes&page=0&size=10&sort=vehicleNumber,asc`

---

## Update Vehicle

**PUT** `/api/v1/vehicles/{vehicleId}`

---

## Delete Vehicle

**DELETE** `/api/v1/vehicles/{vehicleId}`

---

# Trailer API

## Trailer Entity Fields

|Field|Type|Required|
|---|---|---|
|`trailerNumber`|string|yes|
|`licensePlate`|string|yes|
|`trailerType`|enum|no|
|`status`|enum|yes|
|`notes`|string|no|

---

## Create Trailer

**POST** `/api/v1/trailers`

### Request Body

```
{  
  "trailerNumber": "TRL-1001",  
  "licensePlate": "HH-TR-1001",  
  "trailerType": "BOX",  
  "status": "ACTIVE",  
  "notes": "Standard trailer"  
}
```

---

## Get Trailer by ID

**GET** `/api/v1/trailers/{trailerId}`

---

## Search Trailers

**GET** `/api/v1/trailers`

### Supported Query Parameters

|Parameter|Type|Required|Description|
|---|---|---|---|
|`trailerNumber`|string|no|Exact match|
|`licensePlate`|string|no|Exact match|
|`trailerType`|enum|no|Exact match|
|`status`|enum|no|Exact match|
|`page`|integer|no|Page index|
|`size`|integer|no|Page size|
|`sort`|string|no|Sort criteria|

### Example

`GET /api/v1/trailers?status=ACTIVE&trailerType=BOX&page=0&size=10&sort=trailerNumber,asc`

---

## Update Trailer

**PUT** `/api/v1/trailers/{trailerId}`

---

## Delete Trailer

**DELETE** `/api/v1/trailers/{trailerId}`

---

# Location API

## Location Entity Fields

|Field|Type|Required|
|---|---|---|
|`customerId`|UUID|yes|
|`addressId`|UUID|yes|
|`name`|string|yes|
|`contactPerson`|string|no|
|`contactPhone`|string|no|
|`contactEmail`|string|no|
|`siteInstructions`|string|no|

---

## Create Location

**POST** `/api/v1/locations`

### Request Body

```
{  
  "customerId": "11111111-1111-1111-1111-111111111111",  
  "addressId": "f0616a1a-95eb-4b6b-9151-92427595a4c6",  
  "name": "NordSteel Warehouse Hamburg",  
  "contactPerson": "Hans Meier",  
  "contactPhone": "+49 40 555000",  
  "contactEmail": "warehouse@nordsteel.de",  
  "siteInstructions": "Gate 3, report to security"  
}
```

---

## Get Location by ID

**GET** `/api/v1/locations/{locationId}`

---

## Search Locations

**GET** `/api/v1/locations`

### Supported Query Parameters

|Parameter|Type|Required|Description|
|---|---|---|---|
|`customerId`|UUID|no|Exact match|
|`name`|string|no|Partial match|
|`city`|string|no|Partial match via related address|
|`page`|integer|no|Page index|
|`size`|integer|no|Page size|
|`sort`|string|no|Sort criteria|

### Example

`GET /api/v1/locations?customerId=11111111-1111-1111-1111-111111111111&page=0&size=10&sort=name,asc`

---

## Update Location

**PUT** `/api/v1/locations/{locationId}`

---

## Delete Location

**DELETE** `/api/v1/locations/{locationId}`

---

# Enums

## EmploymentType

EMPLOYEE  
SUBCONTRACTOR

## DriverStatus

ACTIVE  
INACTIVE  
ON_LEAVE  
SICK

## VehicleStatus

ACTIVE  
MAINTENANCE  
OUT_OF_SERVICE

## TrailerStatus

ACTIVE  
MAINTENANCE  
OUT_OF_SERVICE

## TrailerType

BOX  
REEFER  
TANK  
FLATBED

---

# Notes

## Unique Business Keys

The following fields are treated as business identifiers and must be unique:

- `customer.customerNumber`
    
- `driver.driverNumber`
    
- `vehicle.vehicleNumber`
    
- `trailer.trailerNumber`
    

Note: `trailer.licensePlate` and ``vehicle.licensPlate`` is intentionally not unique.

---

## Search Semantics

All list filters are optional.

If multiple filters are provided, the service combines them using logical `AND`.

If no filters are provided, the endpoint returns all records using pagination and default sorting.

---

## Default Sorting

Current default sorting is configured per controller via `@PageableDefault`.

Typical defaults include:

- Customers → `customerNumber`
    
- Drivers → `driverNumber`
    
- Vehicles → `vehicleNumber`
    
- Trailers → `trailerNumber`
    
- Addresses → `city`
    
- Locations → `name`