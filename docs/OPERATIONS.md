# StreamFlix operations runbook

## Local services

Use `docker compose up --build` to start PostgreSQL, the Spring Boot API, and the Angular/Nginx frontend. The browser is available at `http://localhost:4200`; the API health endpoint is `http://localhost:8080/actuator/health`.

For local development without Docker, start PostgreSQL separately, then run `mvn spring-boot:run` from `backend` and `npm start` from `frontend`.

## Production configuration

Run the backend with the `prod` Spring profile. These values must be supplied by the deployment secret manager or environment, never committed:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET` (at least 32 random characters)
- `FRONTEND_URL`
- `MEDIA_ROOT` and `FFMPEG_PATH` when local media processing is enabled
- `STRIPE_SECRET_KEY` and `STRIPE_WEBHOOK_SECRET` when billing is enabled

Flyway runs on application startup and Hibernate is configured to validate the schema. Apply migrations before directing traffic to a new release. Do not use `spring.jpa.hibernate.ddl-auto=update` in production.

## Health and monitoring

- `GET /actuator/health` is the liveness/readiness baseline.
- `GET /actuator/metrics` is available for an authenticated operational network only; place it behind the platform gateway or an internal network.
- Every response includes `X-Request-Id`; requests are logged with method, path, status, duration, and request ID. Authorization headers and request bodies are never logged.
- Forward application logs to the cloud platform's log aggregation and alert on repeated 5xx responses, database health failures, high latency, disk capacity, and failed webhook requests.

## Backups and recovery

Run `scripts/backup-postgres.ps1` on a scheduled host with `pg_dump` installed. Store the resulting custom-format dump outside the application host, encrypt it at rest, and retain multiple daily and weekly copies. Test restoration regularly:

```powershell
pg_restore --clean --if-exists --dbname=streamflix_restore .\backups\streamflix-YYYYMMDD-HHMMSS.dump
```

The PostgreSQL volume and media volume are separate. Back up both database dumps and the media storage, or use object storage with versioning for production video/poster assets. Recovery order is: provision PostgreSQL, restore the dump, restore media, deploy the matching application version, then verify `/actuator/health` and a signed playback request.

## Release and rollback

GitHub Actions runs backend tests and frontend build/tests on pushes and pull requests. Build immutable container tags from the commit SHA. Deploy the backend and frontend together, wait for health checks, and inspect Flyway logs. Roll back to the previous image tag if health checks fail; never roll back by deleting migration history. A database migration must remain backward compatible with the application version during rollout.

## Cloud deployment boundary

This repository contains deployable containers but does not create cloud resources or incur provider charges. Before deployment, choose a managed PostgreSQL service, object storage/CDN strategy, secret manager, TLS/ingress, alerting, and backup retention policy. Obtain approval before provisioning paid resources.
