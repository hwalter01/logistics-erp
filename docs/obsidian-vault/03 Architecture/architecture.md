# Logistics ERP -- System Architecture

## Overview

The Logistics ERP system is designed as a modular microservice-based
platform that supports core operations of a freight forwarding or
logistics company.

The architecture separates stable reference data from operational
workflows such as order management and trip execution. Each service owns
its own data and communicates with other services through well-defined
APIs.

This architecture enables:

-   clear domain boundaries
-   independent service evolution
-   scalable operational workflows
-   maintainable long-term system growth

The system is organized around domain-driven service boundaries.

------------------------------------------------------------------------

# High-Level Architecture

The platform consists of several independent backend services.

                    ┌─────────────────────┐
                    │     Web Frontend     │
                    │  (Future React App) │
                    └──────────┬──────────┘
                               │
                               ▼
                     ┌─────────────────┐
                     │    API Gateway   │
                     │ (optional later) │
                     └───────┬─────────┘
                             │
     ┌───────────────────────────────────────────────────────┐
     │                   Backend Services                     │
     │                                                       │
     │  ┌───────────────────┐   ┌─────────────────────────┐  │
     │  │ Master Data       │   │ Order Service           │  │
     │  │ Service           │   │                         │  │
     │  │ customers         │   │ manages transport       │  │
     │  │ drivers           │   │ orders and stops        │  │
     │  │ vehicles          │   └───────────┬─────────────┘  │
     │  │ trailers          │               │                │
     │  │ locations         │               ▼                │
     │  │ addresses         │     ┌─────────────────────┐    │
     │  └──────────┬────────┘     │ Trip Service        │    │
     │             │              │ trip planning       │    │
     │             │              │ dispatching         │    │
     │             │              │ trip events         │    │
     │             │              └──────────┬──────────┘    │
     │             │                         │               │
     │             ▼                         ▼               │
     │     ┌───────────────┐        ┌──────────────────┐     │
     │     │ Document      │        │ Fleet Service    │     │
     │     │ Service       │        │ maintenance      │     │
     │     │ PODs          │        │ inspections      │     │
     │     │ delivery docs │        │ availability     │     │
     │     └───────────────┘        └──────────────────┘     │
     │                                                       │
     └───────────────────────────────────────────────────────┘

------------------------------------------------------------------------

# Architectural Principles

## Domain-Oriented Service Boundaries

Each service is responsible for a specific domain area. Services own
their data and business logic and expose APIs for external access.

## Independent Databases

Each service maintains its own database schema.

  Service               Database Schema
  --------------------- -----------------
  Master Data Service   md
  Order Service         orders
  Trip Service          trip
  Fleet Service         fleet
  Document Service      documents

## API-Based Communication

Services interact through REST APIs. Only entity IDs are exchanged
between services rather than full data objects.

------------------------------------------------------------------------

# Core Services

## Master Data Service

Manages stable reference data used throughout the system.

Entities:

-   Address
-   Customer
-   Location
-   Driver
-   Vehicle
-   Trailer

Responsibilities:

-   CRUD operations
-   search and filtering
-   enforcing unique business identifiers
-   providing reference data for other services

------------------------------------------------------------------------

## Order Service

Manages transport orders from customers.

Entities:

-   Order
-   Stop

Responsibilities:

-   creating transport orders
-   defining pickup and delivery stops
-   referencing customers and locations
-   preparing orders for trip planning

Dependencies:

-   Customer
-   Location

------------------------------------------------------------------------

## Trip Service

Manages operational execution of transports.

Entities:

-   Trip
-   TripEvent

Responsibilities:

-   assigning orders to trips
-   assigning drivers and vehicles
-   tracking trip progress
-   storing operational events

------------------------------------------------------------------------

## Document Service

Stores documents related to logistics operations.

Examples:

-   proof of delivery (POD)
-   delivery notes
-   damage reports
-   invoices

Responsibilities:

-   file storage
-   metadata management
-   linking documents to trips or orders

------------------------------------------------------------------------

## Fleet Service

Manages vehicle and trailer lifecycle and maintenance.

Responsibilities:

-   maintenance schedules
-   inspections
-   availability tracking
-   fleet lifecycle management

------------------------------------------------------------------------

# Future Components

## API Gateway

Potential responsibilities:

-   centralized authentication
-   request routing
-   rate limiting
-   request logging

## Authentication Service

Possible implementation:

-   OAuth2
-   OpenID Connect
-   JWT

Potential provider: Keycloak

## Messaging / Event Bus

Future asynchronous communication between services.

Examples:

-   trip started events
-   order assigned events
-   document uploaded events

Technologies:

-   Kafka
-   RabbitMQ

------------------------------------------------------------------------

# Technology Stack

Backend Framework\
Spring Boot

Persistence\
Spring Data JPA

Database\
PostgreSQL

Database Migration\
Flyway

Build System\
Gradle

API Format\
REST / JSON

Future Frontend\
React

------------------------------------------------------------------------

# Deployment Model

Example development environment:

Docker Compose

-   postgres
-   master-data-service
-   order-service
-   trip-service

Each service runs independently and exposes its own HTTP port.

------------------------------------------------------------------------

# Summary

The Logistics ERP architecture separates stable reference data from
operational workflows.

This approach enables:

-   scalable service design
-   clear domain boundaries
-   maintainable long-term system architecture
