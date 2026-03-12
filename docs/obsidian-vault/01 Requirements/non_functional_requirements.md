# Non-Functional Requirements

## 1. Auditability & Data Integrity

- All changes affecting trip lifecycle must be traceable:
    
    - Who did it
        
    - When
        
    - What changed
        
- Trip events are append-only (no silent overwrite)
    

## 2. Security

- Role-based access control:
    
    - Drivers only see their assigned trips
        
    - Dispatchers see all operational data
        
    - Accounting is read-only (MVP)
        
- Authentication via secure method (JWT acceptable for MVP)
    

## 3. Availability & Performance

- System available for daily operations (target 99.5% availability)
    
- Dispatcher dashboard loads within 2 seconds for typical queries
    

## 4. Reliability

- Idempotent event ingestion for unstable mobile connections
    
- Basic input validation and error handling with consistent responses
    

## 5. Compliance & Privacy

- Personal driver data must be protected and access-controlled
    
- Data retention policy can be defined later; system must not block it
    

## 6. Maintainability

- Clear separation of responsibilities and clean service boundaries
    
- API documented (OpenAPI)
    
- Database migrations reproducible (Flyway/Liquibase)
