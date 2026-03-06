# Document

Represents a file attached to a trip (or later also to an order).
Used for Proof of Delivery (POD) and other evidence.

## Attributes

- documentId
- tripId (required in MVP)
- orderId (optional, future)
- type (POD | DELIVERY_NOTE | DAMAGE_REPORT | OTHER)
- fileName
- mimeType
- fileSizeBytes
- storageKey (path / object storage key)
- checksum (optional but recommended)
- uploadedAt
- uploadedByUserId

## Relationships

- Document -> [[trip]] (required in MVP)

## Business Notes

- Some customers require POD before a trip can be closed.
- Documents must be easily retrievable by dispatcher and accounting.