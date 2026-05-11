# BirdWatch Backend

A RESTful backend for birdwatching enthusiasts.  
Record sightings, manage a bird species wiki, build a personal pokedex, and visualise hotspots on a map — all powered by a geospatial Java stack.

## Features

- **User management & authentication** – JWT-based login, profile editing, home location.
- **Bird species wiki** – Curated scientific data, images, and audio for every bird (admin-managed).
- **Trips & observations** – Start a birding trip, log sightings with location and optional media.
- **Personal Pokedex** – Track every species you’ve seen, filter unseen birds, manually mark common species.
- **Interactive map** – Recent public sightings, heatmap hotspots, and habitat polygons (PostGIS-powered).
- **File storage** – Media uploads to MinIO (S3-compatible object storage).

## Tech Stack

| Layer                 | Technology                                      |
|-----------------------|-------------------------------------------------|
| Runtime               | Java 17+                                        |
| Framework             | Spring Boot 3.x                                 |
| Security              | Spring Security, OAuth2 Resource Server, JWT    |
| Data Access           | Spring Data JPA, Hibernate Spatial              |
| Database              | PostgreSQL 16 + PostGIS                         |
| Migrations            | Flyway                                          |
| Object Storage        | MinIO                                           |
| Mapping               | MapStruct                                       |
| API Documentation     | Springdoc OpenAPI (Swagger UI)                  |
| Testing               | JUnit 5, MockMvc, Testcontainers                |
| Build                 | Maven                                           |
| Containerisation      | Docker, Docker Compose                          |

## Architecture

The application follows a **modular monolith** with feature-based packaging. Each business capability (`user`, `bird`, `trip`, `pokedex`, `map`) is isolated in its own package and adheres to a layered structure:

- **`domain`** – entities, value objects, enums
- **`dto`** – request/response records with Jakarta Bean Validation
- **`mapper`** – MapStruct interfaces for type-safe object mapping
- **`repository`** – Spring Data JPA interfaces (including custom native geospatial queries)
- **`service`** – business logic interfaces and implementations
- **`controller`** – thin REST controllers

Cross-cutting concerns (security, auditing, exception handling, storage) live in `common` and `infrastructure` packages.  
All coordinates are stored as true PostGIS geometry columns (`geometry(Point,4326)`, `geometry(MultiPolygon,4326)`) and queried using Hibernate Spatial.

## Getting Started

### Prerequisites

- Java 17 or later
- Docker and Docker Compose
- Maven (wrapper included)

### 1. Start infrastructure

```bash
docker compose up -d
```

This starts a PostgreSQL/PostGIS container and a MinIO container with the credentials defined in the `.env` file.

### 2. Build and run the application

```bash
./mvnw spring-boot:run
```

The application starts on port `8080`. Flyway automatically applies all database migrations.

### 3. Access API documentation

Open http://localhost:8080/swagger-ui.html to browse and test the API.  
Use the `POST /api/auth/register` endpoint to create an account and obtain a JWT token.

## API Overview

| Endpoint group         | Purpose                         |
|------------------------|---------------------------------|
| `/api/auth/**`         | Registration, login, token refresh (public) |
| `/api/users/me`        | Profile retrieval and update (authenticated) |
| `/api/admin/birds/**`  | Bird species CRUD and media management (admin) |
| `/api/birds/**`        | Public bird wiki and media listing |
| `/api/trips/**`        | Trip lifecycle, observations (authenticated) |
| `/api/pokedex/**`      | User’s seen/unseen birds, manual marking (authenticated) |
| `/api/map/**`          | Recent sightings, hotspots, habitats (public) |

## Testing

Run all tests with:

```bash
./mvnw test
```

- **Unit tests** – service layer with mocked dependencies (JUnit 5 + Mockito).
- **Integration tests** – full Spring context with a real database using Testcontainers (automatically spins up a PostGIS container).
- Test files mirror the main source structure under `src/test/java`.

## Project Structure

```
src/main/java/com/birdwatchbackend
├── BirdingApplication.java
├── common                // shared base entity, security, exceptions
├── config                // Spring configuration (Security, JPA, MinIO, OpenAPI)
├── domain
│   ├── user              // user, auth, profile
│   ├── bird              // bird species, media assets
│   ├── trip              // birding trips, observations
│   ├── pokedex           // user bird status, pokedex logic
│   └── map               // habitat areas, hotspots, recent sightings
└── infrastructure        // external adapters (MinIO file storage client)
```

## Environment Variables

Configuration is managed via `application.properties` and a `.env` file for Docker services:

- `DB_USER`, `DB_PASSWORD`, `DB_NAME`, `DB_URL`
- `MINIO_ROOT_USER`, `MINIO_ROOT_PASSWORD`
- `JWT_SECRET` (used for HS256 token signing)

Adjust these in your `.env` before starting the containers.

## License - MIT

This project is for educational and portfolio purposes.  
Feel free to adapt and extend it.
