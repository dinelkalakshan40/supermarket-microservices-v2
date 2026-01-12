# Microservices  System (Backend Only)

A backend-only microservices system demonstrating real-world enterprise patterns such as service discovery, API Gateway, synchronous and asynchronous communication, circuit breaker, logging, and monitoring.

This project is implemented using Spring Boot and Node.js microservices, RabbitMQ for event-driven communication, Eureka Server for service discovery, and Prometheus for monitoring.

---

## Key Features

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

## Technology Stack

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

## System Architecture

![Architecture Diagram](docs/architecture.png)

### Architecture Overview
- All microservices are registered with Eureka Server
- API Gateway (port 8080) is the only public entry point
- Services communicate using:
  - Synchronous REST APIs
  - Asynchronous events via RabbitMQ
- Prometheus scrapes metrics from all services

---

## Microservices Overview

| Service Name       | Technology            | Database | Description |
|--------------------|----------------------|----------|-------------|
| Product Service    | Spring Boot           | MySQL    | Product management |
| Inventory Service  | Spring Boot           | MongoDB  | Inventory management |
| Order Service      | Spring Boot           | MongoDB  | Order processing |
| Customer Service   | Node.js + Express     | MongoDB  | Customer management |
| Eureka Server      | Spring Cloud          | —        | Service discovery |
| API Gateway        | Spring Cloud Gateway  | —        | Central request routing |
| Prometheus         | Monitoring Tool       | —        | Metrics collection |

---

## Communication Patterns

### Synchronous Communication
- REST-based communication via API Gateway
- Used for product, customer, and order-related operations

### Asynchronous Communication
- Event-driven communication using RabbitMQ
- When an order is placed:
  - Order Service publishes an event
  - Inventory Service consumes the event
  - Inventory quantity is reduced immediately

---

## Circuit Breaker Pattern

- Implemented between **Order Service and Customer Service** to prevent cascading failures
- When Order Service communicates with Customer Service:
  - If Customer Service becomes unavailable or unresponsive
  - The circuit breaker opens after configured failure thresholds
  - A fallback response is returned to Order Service
- This ensures that Order Service remains stable even when Customer Service is down

---

## Logging and Monitoring

### Logging
- Each microservice generates structured logs
- Logs are used to trace requests and debug failures

### Monitoring (Prometheus)
- Prometheus collects metrics from all services
- Monitored metrics include:
  - Request count
  - Response time

