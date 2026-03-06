# Stop

Represents a planned operation in an order: pickup or delivery.

## Attributes

- stopId
- orderId
- sequenceNumber (1..N)
- type (PICKUP | DELIVERY)
- locationId (recommended)
- timeWindowStart (optional)
- timeWindowEnd (optional)
- instructions (optional)
- reference (optional; e.g., dock reference)
- plannedDurationMinutes (optional, future)

## Relationships

- Stop -> [[order]] (required)
- Stop -> [[location]] (recommended)
- Stop influences [[trip]] execution (indirectly via order/trip)

## Business Notes

- Stops define where and when the driver must operate.
- Later: record actual arrival/departure times via trip events per stop.