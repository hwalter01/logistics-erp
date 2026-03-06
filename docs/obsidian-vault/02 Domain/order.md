# Order

Represents a transport order placed by a customer.

## Attributes

- orderId
- customerId
- customerReference (required; unique per customer is recommended)
- cargoDescription (free text)
- weightKg (optional)
- volumeM3 (optional)
- pallets (optional)
- specialRequirements (optional; equipment, handling)
- status
- createdAt

## Status (Suggested)

MVP:
- DRAFT
- CONFIRMED
- ASSIGNED_TO_TRIP
- COMPLETED
- CANCELLED

## Relationships

- Order -> [[customer]] (required)
- Order -> [[stop]] (one or more stops)
- Order -> [[trip]] (optional link; an order is executed by a trip)

## Business Notes

- MVP can start with 1 order per trip, but the model should allow multiple orders per trip later.
- Orders are the contractual/business object; trips are operational execution objects.