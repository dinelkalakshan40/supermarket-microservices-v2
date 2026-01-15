# Microservices  System (Backend Only)

A backend-only microservices system demonstrating real-world enterprise patterns such as service discovery, API Gateway, synchronous and asynchronous communication, circuit breaker, logging, and monitoring.

This project is implemented using Spring Boot and Node.js microservices, RabbitMQ for event-driven communication, Eureka Server for service discovery, and Prometheus for monitoring.

---

## 1. Key Features

- Backend-only microservices architecture
- Polyglot microservices (Java + Node.js)
- API Gateway as a single entry point (Port 8080)
- Service discovery using Eureka Server
- Synchronous REST communication
- Asynchronous communication using RabbitMQ
- Inventory quantity update via events
- Circuit Breaker pattern for fault tolerance
- Application-level logging
- Metrics monitoring using Prometheus

---

## 2. Technology Stack

### Backend
- Java 21
- Spring Boot
- Spring Cloud (Eureka Server, API Gateway)
- RESTful Web Services
- Spring Web / WebClient (for inter-service communication)
- Node.js
- Express.js

### Databases
- MySQL
- MongoDB

### Messaging
- RabbitMQ (Asynchronous event-driven communication)

### Monitoring
- Prometheus (REST API metrics collection)


---

## 3. System Architecture

![Architecture Diagram](docs/architecture.png)

### Architecture Overview
- All microservices are registered with Eureka Server
- API Gateway (port 8080) is the only public entry point
- Services communicate using:
  - Synchronous REST APIs
  - Asynchronous events via RabbitMQ
- Prometheus scrapes metrics from all services

---

## 4. Microservices Breakdown

| Service | Technology | Database | Responsibility |
|-------|-----------|----------|---------------|
| Product Service | Spring Boot | MySQL | Manage product data |
| Inventory Service | Spring Boot | MongoDB | Track and update stock levels |
| Order Service | Spring Boot | MongoDB | Handle order lifecycle |
| Customer Service | Node.js + Express | MongoDB | Manage customer data |
| Eureka Server | Spring Cloud | — | Service discovery |
| API Gateway | Spring Cloud Gateway | — | Central routing & security |
| Prometheus | Monitoring Tool | — | Metrics collection |

---

## 5. Communication Patterns

### 5.1 Synchronous Communication (REST)

Synchronous communication is used where **immediate responses** are required.

Examples:
- Validating customer information
- Retrieving product details
- Creating orders

### 5.2 Asynchronous Communication (Event-Driven)

Asynchronous communication is implemented using RabbitMQ to **decouple services**.

**Order Event Flow**
1. Order Service successfully places an order
2. Order Service publishes an `OrderPlaced` event to RabbitMQ
3. Inventory Service consumes the event
4. Inventory quantity is reduced accordingly

This approach ensures:
- Better scalability
- Non-blocking operations
- Reduced service coupling


---

## 6. Circuit Breaker Pattern

The Circuit Breaker pattern is implemented between:

**Order Service → Customer Service**

### Behavior
- If Customer Service becomes slow or unavailable:
  - Failures are tracked
  - Circuit breaker opens after threshold is reached
  - Order Service returns a fallback response
- When Customer Service recovers:
  - Circuit breaker automatically resets

---

## 7. Logging Strategy

- Each microservice produces **structured logs**
- Logs capture:
  - Incoming requests
  - Service-to-service calls
  - Errors and exceptions
- Logs are useful for:
  - Debugging
  - Root cause analysis
  - Tracing request flows across services

---

## 8. Monitoring with Prometheus

### 8.1 Metrics Collection

- Each service exposes metrics via:/actuator/prometheus
- Prometheus scrapes metrics at regular intervals

### 8.2 Metrics Examples
- HTTP request count
- Response latency

---

## 9. Running the System (High-Level)

> This section describes the typical startup order.

1. Start **Eureka Server**
2. Start **RabbitMQ**
3. Start **API Gateway**
4. Start backend services:
   - Product Service
   - Inventory Service
   - Customer Service
   - Order Service
5. Start **Prometheus**

All services will automatically register with Eureka and become accessible through the API Gateway.

---

## 10. Testing the System

- REST APIs tested using **Postman**
- Asynchronous flows verified via:
  - RabbitMQ management UI
  - Inventory updates after order placement
- Metrics verified via **Prometheus UI**
