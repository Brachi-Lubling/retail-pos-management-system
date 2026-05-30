# Inquiry Manager System

A Java-based client-server system for managing customer inquiries with automated assignment, multithreading support, and persistent storage.

---

## Overview

Inquiry Manager System is a distributed Java application that simulates a real-world customer support environment.
Clients can create inquiries, which are automatically assigned to available representatives based on workload and specialization.

The system demonstrates concepts of concurrency, socket programming, and layered software architecture.

---

## Features

- Create and cancel customer inquiries
- Automatic assignment to available representatives
- Real-time inquiry status tracking
- Representative login/logout system
- Role-based specialization for inquiry handling
- Archiving of completed inquiries
- Monthly statistics generation
- Persistent file-based storage using serialization
- Thread-safe operations for concurrent access

---

## Architecture

The system is built using a layered architecture:

- **Communication Layer** – Handles socket-based client-server communication
- **Service Layer** – Contains business logic and system orchestration
- **Repository Layer** – Manages data access and persistence
- **Data Layer** – Defines domain models and entities

A Singleton-based `InquiryManager` is used to maintain centralized system state.

---

## Tech Stack

- Java
- Socket Programming (Client-Server)
- Multithreading
- Object Serialization
- Concurrent Data Structures (ConcurrentLinkedQueue, AtomicInteger)
- File-based persistence

---

## Project Structure

```
InquiryManagerSystem/
├── Client/
├── Server/
│   ├── communication/
│   ├── data/
│   ├── repository/
│   └── service/
```

---

## How to Run

### Start Server
```bash
java InquiryManagerServer
```

### Start Client
```bash
java InquiryManagerClient
```

> The server must be running before starting a client instance.

---

## System Flow

1. Client submits a new inquiry
2. Server validates and receives request
3. Inquiry is queued for processing
4. Automatically assigned to a representative
5. Status is updated during processing
6. Completed inquiries are archived
7. Monthly reports are generated

---

## Design Highlights

- Thread-safe concurrent processing
- Automatic load balancing between representatives
- Clean separation between system layers
- Scalable architecture for future extensions

---

## Future Improvements

- PostgreSQL database integration
- REST API layer
- Authentication system
- Admin dashboard UI
- Improved logging and monitoring

---

## Author

Academic Software Engineering Project


