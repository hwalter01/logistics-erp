# Trip Event

Represents an append-only, timestamped event in the trip lifecycle.
Trip events create the auditable timeline and are the source of truth for status.

## Attributes

- eventId
- tripId
- eventType
- occurredAt (server timestamp as source of truth)
- createdByUserId
- note (optional)
- clientTimestamp (optional; if driver device time differs)
- idempotencyKey (optional but recommended)

## Event Types (Suggested Baseline)

- PLANNED (optional if trip is created already planned)
- ASSIGNED
- STARTED
- ARRIVED_PICKUP
- LOADED
- IN_TRANSIT
- ARRIVED_DELIVERY
- DELIVERED
- CLOSED
- CANCELLED

## Relationships

- Trip Event -> [[trip]] (required)

## Business Notes

- Events are append-only: never modify/delete; corrections are new events.
- Invalid transitions should be prevented (business rules).
- Duplicate submissions must not create duplicates (idempotency key).