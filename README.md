# E-Commerce Microservices System

This project is a microservices-based e-commerce application built using Spring Boot. It consists of multiple services that handle user management, product catalog, order processing, service discovery, and API gateway for routing and security. The system uses Eureka for service discovery, Feign for inter-service communication, and basic authentication for security.

## Project Overview

The application demonstrates a basic e-commerce workflow:
- Users (customers or admins) can browse products.
- Customers can place orders.
- Admins can manage products and users.
- Orders update product quantities and user order history via inter-service calls.

Key technologies:
- **Spring Boot**: For building microservices.
- **Spring Cloud Netflix Eureka**: For service discovery.
- **Spring Cloud Gateway**: For API routing and security.
- **Feign Client**: For synchronous HTTP communication between services.
- **Spring Data JPA**: For database interactions.
- **H2/MySQL**: Databases (H2 for dev, MySQL for prod).
- **Springdoc OpenAPI (Swagger)**: For API documentation.
- **SLF4J**: For logging.
- **JUnit & Mockito**: For unit and integration testing.

## Architecture

The system follows a microservices architecture:
- **Discovery Service**: Central registry for all services (Eureka Server).
- **API Gateway**: Entry point for all external requests. Handles routing to services and basic authentication.
- **User Service**: Manages user CRUD operations and order history.
- **Product Service**: Manages product CRUD and filtering (by price, name, category).
- **Order Service**: Handles order placement, updates product stock and user orders via Feign calls to Product and User services.

Communication:
- All services register with the Discovery Service.
- API Gateway discovers services via Eureka and routes requests (e.g., `/users/**` to User Service).
- Order Service uses Feign clients (`ProductClient` and `UserClient`) to call Product and User services directly (load-balanced via Eureka).
- Requests are proxied through the API Gateway for external access.

Databases:
- Each service has its own database (H2 in dev mode, MySQL in prod).
- Spring Profiles (`dev` and `prod`) switch between databases and configurations.

## Services Description

### 1. Discovery Service (Port: 8761)
- Eureka Server for service registration and discovery.
- Endpoints: `/eureka` (web UI available at `http://localhost:8761`).
- Configuration: `application.properties` disables self-registration.
- Logs: `discovery-service.log`.

### 2. API Gateway (Port: 8080)
- Routes requests to services based on paths (e.g., `/users/**` to User Service).
- Security: Basic authentication with hardcoded users:
  - Admin: `username: admin`, `password: adminpass` (Role: ADMIN).
  - Customer: `username: customer`, `password: custpass` (Role: CUSTOMER).
- Fallback route: `/fallback` for service unavailability.
- Swagger: Aggregates Swagger docs from all services (accessible via `/swagger-ui.html`).
- Logs: `api-gateway.log`.

### 3. User Service (Port: 8081)
- Manages users and their order history.
- Controllers:
  - `UserController`: Handles CRUD for users and adding orders (internal Feign endpoint).
- Services:
  - `UserService`: Business logic for adding/updating/deleting users, fetching users, adding orders to user history.
- Models:
  - `UserModel`: id, name, role (CUSTOMER/ADMIN), ordersList (list of order IDs).
- Repository: `UserRepository` (JPA).
- Exceptions: `UserException` (e.g., user not found, empty name).
- Swagger: `/swagger-ui.html` and `/v3/api-docs`.
- Logs: `user-service.log`.

### 4. Product Service (Port: 8082)
- Manages product catalog.
- Controllers:
  - `ProductController`: CRUD for products, filtering by price/name/category.
- Services:
  - `ProductService`: Business logic for adding/updating/deleting products, fetching with filters.
- Models:
  - `ProductModel`: id, name, description, category, price, quantity.
- Repository: `ProductRepository` (JPA with custom queries for filters).
- Exceptions: `ProductException` (e.g., product not found, negative quantity).
- Swagger: `/swagger-ui.html` and `/v3/api-docs`.
- Logs: `product-service.log`.

### 5. Order Service (Port: 8083)
- Handles order placement and retrieval.
- Controllers:
  - `OrderController`: Place order, get all/orders by ID/user ID.
- Services:
  - `OrderService`: Places orders, validates users/products, updates stock and user orders via Feign.
- Models:
  - `OrderModel`: id, userId, orderItems (map of productId:quantity), totalPrice, status (PLACED/FAILED/CANCELLED).
  - `OrderStatus`: Enum for order states.
- Clients (Feign):
  - `UserClient`: Fetches user, adds order to user.
  - `ProductClient`: Fetches/updates products.
- Repository: `OrderRepository` (JPA).
- Exceptions: `OrderException` (e.g., user/product not found, insufficient quantity).
- Swagger: `/swagger-ui.html` and `/v3/api-docs`.
- Logs: `order-service.log`.

## Authentication and Authorization

- **Basic Auth**: Implemented in API Gateway.
- **Roles**:
  - **ADMIN**: Can manage products (CRUD), view all orders/users.
  - **CUSTOMER**: Can place orders, view their own orders.
- Open Endpoints: Product browsing (`GET /products`, filters), Swagger docs.
- Secured Endpoints:
  - `/products` (POST/PUT/DELETE): ADMIN only.
  - `/orders`: CUSTOMER/ADMIN (place/view).
  - `/users`: Authenticated (full access for admins, but not specified further).

## Spring Profiles

- **dev**: Uses H2 in-memory database (file-based), enabled by default.
  - Config: `application-dev.properties` (H2 console enabled).
- **prod**: Uses MySQL database.
  - Config: `application-prod.properties`.
- Activation: Set `spring.profiles.active=prod` in `application.properties` or via VM args (`-Dspring.profiles.active=prod`).
- Differences: Database URL, driver, DDL auto, logging file suffix (_dev.log vs _prod.log).

## How to Run

### Prerequisites
- Java 17+
- Gradle (wrapper included)
- MySQL (for prod profile)

### Steps
1. **Build All Services**:
   - Navigate to root directory: `./gradlew build` (or per service).

2. **Run Services** (in order):
   - Discovery Service: `cd discovery-service && ./gradlew bootRun`
   - API Gateway: `cd api-gateway && ./gradlew bootRun`
   - User Service: `cd user-service && ./gradlew bootRun`
   - Product Service: `cd product-service && ./gradlew bootRun`
   - Order Service: `cd order-service && ./gradlew bootRun`

3. **Access**:
   - Eureka Dashboard: `http://localhost:8761`
   - Swagger (via Gateway): `http://localhost:8080/swagger-ui.html` (aggregates all services).
   - Example Requests (use Basic Auth):
     - Get Products: `GET http://localhost:8080/products` (no auth needed).
     - Place Order (Customer): `POST http://localhost:8080/orders` with body `{ "userId": 1, "orderItems": { "1": 2 } }`.

4. **Prod Mode**:
   - Create MySQL databases: `user`, `product`, `order`.
   - Update profiles to `prod` and run.

### Testing
- Unit Tests: Per service (e.g., `UserServiceTest`, `ProductControllerTest`).
- Integration Tests: Full flow tests (e.g., add/update/delete in integration tests).
- Run: `./gradlew test`

## Logging
- Each service logs to its own file (e.g., `user-service.log`).
- Level: DEBUG for com.microservice package.
- Gzipped old logs (e.g., `user-service.log.2025-09-29.0.gz`).

## Important Notes
- **Security**: Basic auth is used.
- **Error Handling**: Global exception handlers in each service.
- **Fallback**: API Gateway has a fallback for unavailable services.
- **Inter-Service Calls**: Order Service calls others; ensure they are up.

