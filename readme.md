# StreamFlix — Milestone 1

Minimal end-to-end foundation: Angular frontend → Spring Boot REST API → PostgreSQL.

## Prerequisites

- Java 21 and Maven 3.9+
- Node.js 20+ and npm
- Docker Desktop (used to run PostgreSQL locally)

Docker and PostgreSQL were not available in the development environment used to create this milestone, so the database runtime still needs to be started on a machine with Docker.

## Run locally

1. Start PostgreSQL:

   ```powershell
   docker compose up -d postgres
   ```

2. Start the backend in a second terminal:

   ```powershell
   cd backend
   mvn spring-boot:run
   ```

   Flyway creates the `movies` table and seeds four sample records. The API is available at `http://localhost:8080/api/movies`.

3. Install and start the Angular frontend in a third terminal:

   ```powershell
   cd frontend
   npm.cmd install
   npm.cmd start
   ```

   Open `http://localhost:4200`. The homepage loads movie records from PostgreSQL through the Spring Boot API.

## Verification

Backend compile/package:

```powershell
cd backend
mvn clean package
```

Frontend production build:

```powershell
cd frontend
npm.cmd run build
```

API smoke check after startup:

```powershell
Invoke-RestMethod http://localhost:8080/api/movies
```

## Environment overrides

The backend defaults to the credentials in `docker-compose.yml`. Override with `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, or `PORT` when connecting to another PostgreSQL instance.

Milestone 1 intentionally does not include authentication, subscriptions, streaming, admin tools, or paid cloud services.
