# User Roles

The system must support different roles with clearly defined permissions and responsibilities.

Role-based access control ensures that users can only access the information and actions relevant to their responsibilities.

---

## 1. Dispatcher

The Dispatcher is the primary operational role in the system.

### Responsibilities

Dispatchers manage transport operations and ensure that orders are executed successfully.

### Permissions

Dispatchers must be able to:

- Create and manage customers
    
- Create and manage customer locations
    
- Create transport orders
    
- Define pickup and delivery stops
    
- Create trips
    
- Assign drivers and vehicles to trips
    
- Monitor trip progress
    
- View trip timelines and events
    
- Access trip documents (including POD)
    
- Search and filter trips by various criteria
    

Dispatchers require visibility across all operational data.

---

## 2. Driver

Drivers are responsible for executing assigned trips and reporting trip progress.

### Permissions

Drivers must be able to:

- View their assigned trips
    
- See trip details including stops and instructions
    
- Report status updates during trip execution
    
- Upload Proof of Delivery documents
    
- View recent trip history related to their assignments
    

Drivers must **not** have access to trips assigned to other drivers.

---

## 3. Accounting

Accounting staff access operational data only after trips are completed.

### Permissions

Accounting staff must be able to:

- View completed trips
    
- Access delivery timestamps and status history
    
- Download Proof of Delivery documents
    
- Export operational data for billing purposes
    

Accounting users should not modify operational data.

---

## 4. Administrator

Administrators manage system configuration and user access.

### Permissions

Administrators must be able to:

- Create and manage user accounts
    
- Assign user roles
    
- Deactivate users when necessary
    
- Configure basic system settings
    

Administrators typically have broad system access but do not participate in day-to-day operations.