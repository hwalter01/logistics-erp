# Business Rules (What the system must enforce)

## 1. Assignment Rules

1. A driver cannot be assigned to overlapping active trips.
    
2. A vehicle cannot be assigned to overlapping active trips.
    
3. A trailer cannot be assigned to overlapping active trips (if used).
    

(MVP can enforce a simplified rule: “only one active trip per driver/vehicle at a time”.)

## 2. Status Transition Rules

We want controlled transitions to prevent inconsistent data.

Example allowed path:  
PLANNED → ASSIGNED → STARTED → ARRIVED_PICKUP → LOADED → IN_TRANSIT → ARRIVED_DELIVERY → DELIVERED → CLOSED

Exceptions:

- CANCELLED allowed from PLANNED/ASSIGNED
    
- If something goes wrong, a dispatcher may add an “EXCEPTION” event type (MVP optional)
    

## 3. POD Rules

- For selected customers, POD is mandatory to close a trip.
    
- POD can be uploaded at DELIVERED or later, but the system should flag missing POD.
    

## 4. Ownership / Visibility

- Drivers see only their assigned trips (current + recent history)
    
- Dispatchers see all trips
    
- Accounting sees closed/delivered trips + documents
    

## 5. Audit Rules

- Events must not be edited or deleted by normal users.
    
- Corrections occur via additional events or admin-only override processes (future).