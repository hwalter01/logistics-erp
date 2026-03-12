# Data Model Overview

## Overview

This document provides a high-level overview of the domain entities used
across the Logistics ERP platform.

It shows the main business objects and their relationships across
services.

------------------------------------------------------------------------

# Master Data Entities

## Address

Represents a reusable physical address.

Fields include:

-   street
-   houseNumber
-   postalCode
-   city
-   country
-   additionalLine

Addresses can be referenced by:

-   Customer
-   Location
-   Driver

------------------------------------------------------------------------

## Customer

Represents a business partner requesting logistics services.

Key attributes:

-   customerNumber
-   name
-   vatNumber
-   contactEmail
-   contactPhone
-   podRequired

Relationships:

Customer → Address\
Customer → Location (1:N)

------------------------------------------------------------------------

## Location

Represents a physical site belonging to a customer.

Examples:

-   warehouse
-   factory
-   distribution center

Relationships:

Location → Customer\
Location → Address

------------------------------------------------------------------------

## Driver

Represents a driver operating company vehicles.

Attributes:

-   driverNumber
-   firstName
-   lastName
-   licenseNumber
-   employmentType
-   status

Relationship:

Driver → Address

------------------------------------------------------------------------

## Vehicle

Represents a truck or powered vehicle.

Attributes:

-   vehicleNumber
-   licensePlate
-   vin
-   brand
-   model
-   status

------------------------------------------------------------------------

## Trailer

Represents a trailer unit attached to vehicles.

Attributes:

-   trailerNumber
-   licensePlate
-   trailerType
-   status

Note: trailer license plates are not unique.

------------------------------------------------------------------------

# Order Domain

## Order

Represents a transport request from a customer.

Attributes:

-   orderId
-   customerId
-   orderNumber
-   status

Relationships:

Order → Stop (1:N)

------------------------------------------------------------------------

## Stop

Represents a pickup or delivery location for an order.

Attributes:

-   stopId
-   locationId
-   stopType
-   plannedTime

Relationships:

Stop → Location

------------------------------------------------------------------------

# Trip Domain

## Trip

Represents the execution of one or more transport orders.

Attributes:

-   tripId
-   driverId
-   vehicleId
-   trailerId
-   status

Relationships:

Trip → Order (N:M)

------------------------------------------------------------------------

## TripEvent

Represents operational events occurring during a trip.

Examples:

-   trip started
-   arrived at pickup
-   delivery completed

Attributes:

-   tripEventId
-   tripId
-   eventType
-   timestamp

------------------------------------------------------------------------

# Document Domain

## Document

Represents files associated with logistics operations.

Attributes:

-   documentId
-   tripId
-   orderId
-   type
-   fileName
-   mimeType
-   storageKey
