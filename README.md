
# OEBS: Over Engineered Booking System Demo

[![Build Status](https://github.com/niomwungeri-fabrice/oebs/actions/workflows/ci.yml/badge.svg)](https://github.com/niomwungeri-fabrice/oebs/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/java-21-blue)
![License](https://img.shields.io/github/license/niomwungeri-fabrice/oebs)
![GitHub pull requests](https://img.shields.io/github/issues-pr/niomwungeri-fabrice/oebs)
![GitHub Issues](https://img.shields.io/github/issues/niomwungeri-fabrice/oebs)

[//]: # (![GitHub release &#40;latest by date&#41;]&#40;https://img.shields.io/github/v/release/niomwungeri-fabrice/oebs&#41;)

OEBS Demo Application intended to showcase the production ready set up for spring boot application with other related technology

---
## Running Locally

### Prerequisites

- **Docker**: Ensure Docker is installed and running on your system.
- **Java**: Java 17 (recommended) or higher.

### Running Locally with Java 21 or higher

If you have Java 21 installed, you can run the application directly using:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/niomwungeri-fabrice/oebs.git
    cd oebs
    ```
2. Make sure you have sample.env copied to .env
3. **Start the PostgreSQL Database in Docker**:
    ```bash
    docker-compose up db
    ```
4. **Run the Spring Boot Application**:
    ```bash
     mvn spring-boot:run
    ```

4. **Access Swagger UI**:
    - Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) to view and test API endpoints.

### Running Locally with Lower Java Versions

If you don’t have Java 21 installed and prefer not to upgrade, use Docker to run the application with Java 21:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/niomwungeri-fabrice/oebs.git
    cd oebs
    ```

2. **Start Both the Database and Application Services**:
    ```bash
    make dev
    ```

   This will build and run the application in a Docker container with Java 21, exposing the app on port `8080`.

3. **Access Swagger UI**:
    - Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) to view and test API endpoints.

## API Documentation

OEBS uses Swagger UI for interactive API documentation. To explore and test the endpoints, go to [Swagger UI](http://localhost:8080/swagger-ui/index.html).

# Development Setup

### Environment Configuration

- **Database**: OEBS uses PostgreSQL with separate configuration profiles for development and production.
- **Java**: Spring Boot is configured to run with Java 21. If using a lower Java version, run the app with Docker as described above.
- **Docker Compose**: Use Docker Compose to manage database and application services.
- Add more...
