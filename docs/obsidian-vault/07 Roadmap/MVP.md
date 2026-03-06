# Product Roadmap — Trip Tracker → ERP Foundation

## Guiding Principles

- Deliver operational value early (dispatcher visibility + driver status updates)
    
- Keep data model and architecture extensible for ERP modules
    
- Prefer correctness and auditability over “quick hacks”
    

---

## Phase 0 — Project Setup (Foundation)

**Goal:** Make development and deployments reproducible.

**Deliverables**

- Repository structure established (services, docs, infra)
    
- Local dev environment via Docker Compose
    
- Database migrations enabled
    
- API documentation framework in place (OpenAPI)
    
- Authentication mechanism scaffolding
    

**Acceptance Criteria**

- A new developer can run the system locally in < 30 minutes
    
- Database schema can be rebuilt from scratch via migrations
    

---

## Phase 1 — MVP: Trip Tracker Core

**Goal:** End-to-end operational workflow from dispatcher planning to driver completion.

**Features**

1. Customer & Location Management (basic CRUD)
    
2. Order creation with stops and time windows
    
3. Trip creation
    
4. Assignment of driver + vehicle (+ optional trailer)
    
5. Driver “My Trips” list (minimal UI or API-only)
    
6. Trip event recording (append-only timeline)
    
7. Dispatcher trip dashboard with filtering
    
8. Basic RBAC (Driver/Dispatcher/Admin)
    

**Acceptance Criteria**

- Dispatcher can create an order, create a trip, assign driver+truck
    
- Driver can record trip statuses through the lifecycle
    
- Dispatcher can view trip timeline and current status
    
- Drivers cannot view trips they are not assigned to
    
- Event double-submission does not create duplicates (idempotency)
    

---

## Phase 2 — Documents & Operational Quality

**Goal:** Add proof and reliability for real operations.

**Features**

1. Document upload & attachment to trips (POD)
    
2. “Missing POD” flags for dispatchers/accounting
    
3. Improved validation for status transitions
    
4. Audit log views for admin (optional)
    
5. Basic export endpoint for accounting (CSV/JSON)
    

**Acceptance Criteria**

- Driver uploads POD for delivered trips
    
- Dispatcher can view/download POD
    
- System flags delivered trips missing POD after configurable time
    

---

## Phase 3 — ERP Expansion: Fleet & Maintenance (Light)

**Goal:** Start ERP value with fleet compliance and maintenance scheduling.

**Features**

1. Vehicle maintenance records (inspection dates, service intervals)
    
2. Reminders/flags for upcoming inspections (HU/SP etc.)
    
3. Vehicle status changes (active/in repair)
    
4. Reports: vehicle utilization, downtime (simple)
    

**Acceptance Criteria**

- Dispatcher sees when a vehicle is unavailable due to repair
    
- System flags upcoming inspections before expiry
    

---

## Phase 4 — ERP Expansion: Billing Preparation (Not full invoicing yet)

**Goal:** Provide accurate data for billing, reduce disputes.

**Features**

1. Structured capture of billable timestamps (waiting time, loading time)
    
2. Rate configuration (basic)
    
3. Cost calculation previews per trip/order
    
4. Export to accounting software format (later: integration)
    

**Acceptance Criteria**

- Accounting can export completed trips with evidence
    
- Disputes can reference recorded timestamps + POD
    

---

## Phase 5 — Integrations & Customer Portal (Optional)

**Goal:** External visibility and automation.

**Potential Features**

- Telematics/GPS event ingestion
    
- Customer track-and-trace portal
    
- EDI import for orders
    
- Notifications (email/SMS) based on events
    

---

## MVP Definition (Explicit)

For MVP, NordCargo expects:

- Orders, trips, assignments, event timeline, and POD
    
- Role-based access
    
- Minimal UI acceptable (API-first), but dispatcher needs workable listing/filtering
