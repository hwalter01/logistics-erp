# Functional Requirements

## 1. User Roles

### Dispatcher

- Creates and manages customers, locations, orders, and trips
    
- Assigns driver + vehicle
    
- Monitors trip execution status in a dashboard
    

### Driver

- Views assigned trips
    
- Updates trip status via minimal UI (later: mobile app)
    
- Uploads POD or delivery documents at completion
    

### Accounting (Read-only for MVP)

- Views completed trips, timestamps, and POD availability
    
- Exports trip data for billing preparation
    

### Admin

- Manages user accounts and role assignments
    
- Controls basic configuration (later: business rules, status definitions, etc.)
    

---

## 2. Customer & Location Management

### FR-CUST-001

Dispatcher/Admin can create, update, and view customers.

### FR-CUST-002

A customer can have multiple locations (pickup/delivery sites), each with:

- Address
    
- Contact person + phone/email
    
- Site notes (e.g., “Gate 3”, “call before arrival”)
    

### FR-CUST-003

Customers have references and attributes:

- Customer name, legal entity info
    
- Default invoice address (future)
    
- Customer-specific notes and requirements (e.g., “POD mandatory”)
    

---

## 3. Order Management

### FR-ORD-001

Dispatcher can create an Order with:

- Customer
    
- Order reference number (customer reference)
    
- Cargo description (free text)
    
- Weight/volume fields (optional initially)
    
- Required equipment notes (e.g., trailer type, liftgate)
    

### FR-ORD-002

An Order consists of one or more Stops:

- Stop type: pickup or delivery
    
- Location (either customer location or ad-hoc address)
    
- Time window (earliest/latest arrival)
    
- Instructions (free text)
    

### FR-ORD-003

Order states (MVP):

- DRAFT
    
- CONFIRMED
    
- ASSIGNED_TO_TRIP
    
- COMPLETED
    
- CANCELLED
    

---

## 4. Trip Management

### FR-TRIP-001

Dispatcher can create a Trip. A Trip can include:

- One or multiple Orders (future optional; MVP may start with 1 order per trip but design should allow multiple)
    
- Planned start date/time (optional)
    
- Planned end date/time (optional)
    
- Notes (free text)
    

### FR-TRIP-002

Dispatcher can assign:

- One driver
    
- One truck (vehicle)
    
- Optionally one trailer
    

Assignment must consider availability rules (see business rules).

### FR-TRIP-003

Dispatcher can view a Trip with:

- Assigned driver/truck/trailer
    
- All stops (pickup and delivery)
    
- Current status
    
- Timeline of all events (append-only)
    
- Attached documents (POD etc.)
    

### FR-TRIP-004

Trips have lifecycle states (MVP baseline; can evolve):

- PLANNED
    
- ASSIGNED
    
- STARTED
    
- ARRIVED_PICKUP
    
- LOADED
    
- IN_TRANSIT
    
- ARRIVED_DELIVERY
    
- DELIVERED
    
- CLOSED
    
- CANCELLED (exception)
    

---

## 5. Status / Event Tracking

### FR-EVT-001

Driver can record status events for assigned trips, including timestamp.

### FR-EVT-002

Each event entry contains:

- Event type (status)
    
- Timestamp (server time as source of truth; client time can be stored as metadata)
    
- User who created it
    
- Optional note/comment
    

### FR-EVT-003

Events must be append-only (auditable). Corrections must create a new event rather than overwriting the previous one.

### FR-EVT-004

System should prevent invalid status transitions (configurable later). MVP should enforce a simple transition policy.

### FR-EVT-005

Events should support idempotency (duplicate submissions must not create duplicates).

---

## 6. Document Management (POD)

### FR-DOC-001

Driver can upload documents for a trip:

- Proof of Delivery (photo, PDF)
    
- Delivery note, damage report (optional categories)
    

### FR-DOC-002

Dispatcher and Accounting can view/download documents attached to a trip.

### FR-DOC-003

Certain customers require POD to mark a trip as “CLOSED”.

---

## 7. Search & Dashboard

### FR-UI-001

Dispatcher can list/filter trips by:

- Status
    
- Date range
    
- Driver
    
- Vehicle
    
- Customer
    

### FR-UI-002

Dispatcher sees “attention required” flags:

- Delayed relative to time window (later)
    
- Missing POD for delivered trips after X hours
    
- Unassigned trips scheduled within next 24 hours
    

(MVP can implement basic filtering; flags can be incremental.)