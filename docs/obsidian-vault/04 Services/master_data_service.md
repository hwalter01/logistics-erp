# Master Data Service

## Overview

The Master Data Service manages the core reference data used across the logistics platform.

It provides centralized management of fundamental business entities such as customers, drivers, vehicles, trailers, and locations.

These entities represent relatively stable information that is shared across multiple operational services such as Order Management, Trip Planning, and Fleet Operations.

The service exposes REST APIs for creating, updating, searching, and deleting master data records.

---

# Responsibilities

The Master Data Service is responsible for:

• Managing the lifecycle of core business entities  
• Ensuring business identifiers remain unique  
• Providing searchable and paginated access to master data  
• Validating relationships between master data entities  
• Maintaining consistent address information  

---

# Managed Entities

The service owns the following domain entities.

## Address

Represents a physical address.

Addresses are reusable and may be referenced by multiple entities such as:

- Customers
- Drivers
- Locations

Fields include:

- street
- houseNumber
- postalCode
- city
- country
- additionalLine

---

## Customer

Represents a business partner that requests transport services.

A customer may have multiple locations (e.g. warehouses or factories).

Key properties:

- customerNumber (unique business identifier)
- name
- vatNumber
- contact information
- podRequired (proof of delivery requirement)
- address reference

Relationships:

Customer → Address  
Customer → Location (1:N)

---

## Location

Represents a specific operational site belonging to a customer.

Examples include:

- warehouses
- distribution centers
- factories
- delivery points

Locations contain:

- name
- contact details
- operational instructions
- address reference
- associated customer

Relationships:

Location → Customer  
Location → Address

---

## Driver

Represents a driver who operates vehicles within the logistics fleet.

Drivers may be employees or subcontractors.

Key attributes:

- driverNumber (unique)
- name
- contact information
- licenseNumber
- employmentType
- status
- address reference

Employment types include:

- EMPLOYEE
- SUBCONTRACTOR

Driver status may represent availability.

---

## Vehicle

Represents a powered vehicle (typically a truck tractor).

Vehicles are part of the company's fleet and may be assigned to trips.

Key attributes:

- vehicleNumber (unique internal identifier)
- licensePlate
- VIN
- brand
- model
- operational status

Vehicle status may include:

- ACTIVE
- MAINTENANCE
- OUT_OF_SERVICE

---

## Trailer

Represents a trailer unit that can be attached to vehicles.

Unlike vehicles, trailer license plates are **not required to be unique**, because plates may be reused after a trailer is retired.

Key attributes:

- trailerNumber (unique)
- licensePlate
- trailerType
- status
- notes

Trailer types include:

- BOX
- REEFER
- TANK
- FLATBED

---

# Domain Relationships

The following simplified relationship model applies.

Customer  
└── Location (1:N)

Customer  
└── Address (1:1)

Location  
└── Address (1:1)

Driver  
└── Address (1:1)


Vehicles and trailers currently do not reference addresses.

---

# Architectural Role

The Master Data Service acts as a **reference data provider** for other services.

Other services should **not duplicate master data** but instead reference entities using their IDs.

Examples:

Order Service
→ references `customerId` and `locationId`

Trip Service
→ references `driverId`, `vehicleId`, and `trailerId`

---

# API Design Principles

The service follows these design principles.

## RESTful Endpoints

Each entity exposes standard CRUD endpoints:

`POST /api/v1/{entity}`  
`GET /api/v1/{entity}/{id}`  
`GET /api/v1/{entity}`  
`PUT /api/v1/{entity}/{id}`  
`DELETE /api/v1/{entity}/{id}`

---

## Pagination

All list endpoints support pagination.

Query parameters:
`page`  
`size`  
`sort`

Example:
`GET /api/v1/customers?page=0&size=20&sort=customerNumber,asc`

---

## Filtering

Search endpoints support dynamic filtering using optional query parameters.

Example:
`GET /api/v1/customers?name=Nord&podRequired=true`

Filters are combined using logical **AND**.

---

## Error Handling

The service returns structured error responses.

Common cases include:

- ResourceNotFoundException
- DuplicateResourceException
- Validation errors

---

# Business Rules

The following business constraints apply.

### Unique Identifiers

These fields must be unique:

Customer
- customerNumber

Driver
- driverNumber

Vehicle
- vehicleNumber
- licensePlate

Trailer
- trailerNumber

Note:
Trailer license plates are intentionally **not unique**.

---

### Address Reuse

Addresses may be shared across entities.

For example:

- a customer headquarters
- a warehouse location
- a driver's home address

All references point to the same Address entity.

---

### Status Fields

Operational status fields exist to represent entity availability.

Examples:

Driver
- ACTIVE
- INACTIVE
- ON_LEAVE

Vehicle
- ACTIVE
- MAINTENANCE
- OUT_OF_SERVICE

Trailer
- ACTIVE
- MAINTENANCE
- OUT_OF_SERVICE

---

# Data Consistency

The service enforces referential integrity through:

- foreign key constraints
- application-level validation
- repository checks for duplicates

---

# Integration with Other Services

The Master Data Service provides foundational data for other services in the platform.

Future services that depend on this service include:

Order Service
- references customers and locations

Trip Service
- references drivers, vehicles, trailers

Fleet Service
- may manage maintenance and availability

---

# Technology Stack

The service is implemented using the following technologies.

Backend Framework
- Spring Boot

Persistence
- Spring Data JPA
- PostgreSQL

Database Migration
- Flyway

Build System
- Gradle

API Format
- REST / JSON

---

# Future Improvements

Potential enhancements include:

- OpenAPI documentation
- soft delete support
- audit logging
- entity history tracking
- address normalization
- caching for frequently accessed entities