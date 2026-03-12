# Service Boundaries

## Overview

This document describes the domain ownership and responsibility
boundaries between services in the Logistics ERP system.

Clearly defined service boundaries prevent tight coupling and ensure
that each microservice owns its data and business logic.

------------------------------------------------------------------------

# Core Principle

Each service:

-   owns its **database schema**
-   owns its **domain entities**
-   exposes functionality via **REST APIs**
-   is accessed by other services **only through APIs**

Direct database access across services is **not allowed**.

------------------------------------------------------------------------

# Service Ownership

## Master Data Service

Owns stable reference data used across the platform.

### Entities

-   Address
-   Customer
-   Location
-   Driver
-   Vehicle
-   Trailer

### Responsibilities

-   CRUD operations for reference data
-   enforcing unique business identifiers
-   search and filtering
-   validation of master data relationships

### Referenced by

-   Order Service
-   Trip Service
-   Fleet Service

------------------------------------------------------------------------

## Order Service

Manages customer transport requests.

### Entities

-   Order
-   Stop

### Responsibilities

-   creating transport orders
-   managing pickup and delivery stops
-   referencing customers and locations
-   preparing orders for trip planning

### External References

-   Customer (Master Data)
-   Location (Master Data)

------------------------------------------------------------------------

## Trip Service

Manages the operational execution of transports.

### Entities

-   Trip
-   TripEvent

### Responsibilities

-   assigning orders to trips
-   assigning drivers and vehicles
-   tracking trip progress
-   storing operational events

### External References

-   Driver (Master Data)
-   Vehicle (Master Data)
-   Trailer (Master Data)
-   Order (Order Service)

------------------------------------------------------------------------

## Document Service

Stores logistics-related documents.

### Entities

-   Document

### Responsibilities

-   storing files
-   managing document metadata
-   linking documents to orders or trips

### External References

-   Order
-   Trip

------------------------------------------------------------------------

## Fleet Service

Manages fleet lifecycle and maintenance.

### Entities

-   MaintenanceRecord
-   Inspection
-   ServiceSchedule

### Responsibilities

-   vehicle maintenance tracking
-   inspection management
-   fleet availability tracking

### External References

-   Vehicle
-   Trailer
