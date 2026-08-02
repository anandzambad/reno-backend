# Reno V2 Backend

Modern backend for Reno V2.

## Target stack

- Java 21
- Spring Boot
- Spring Web / REST
- Spring Data JPA
- MySQL
- Flyway database migrations
- OpenAPI / Swagger
- Docker
- GitHub Actions

## Architecture

The backend will use modular business features with clear separation between API, application/service, domain, persistence and infrastructure concerns.

## Migration

The backend will be built from the legacy Reno application's actual workflows and database schema. Existing behavior will be preserved while APIs and internals are modernized incrementally.
