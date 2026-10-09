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

## Authentication and profiles

Milestone 3 adds stateless JWT authentication with BCrypt password hashing. The local development JWT secret is configured in `backend/src/main/resources/application.yml`; set `JWT_SECRET` to a random secret of at least 32 characters outside local development.

Available auth endpoints:

- `POST /api/auth/register` with `{ "email", "password", "profileName" }`
- `POST /api/auth/login` with `{ "email", "password" }`
- `GET /api/profiles` with a Bearer token
- `POST /api/profiles` with a Bearer token and `{ "name", "avatarKey" }`

The Angular app provides `/login`, `/register`, and protected `/profiles` routes. Tokens are stored in browser local storage for this development milestone and attached automatically to API requests. Use HTTPS and a hardened token/cookie strategy before production deployment.

## User features

Milestone 4 adds authenticated user content and episodic catalogue APIs:

- `GET/POST/DELETE /api/me/watchlist` for My List
- `GET /api/me/continue-watching` and `GET/POST /api/me/history` for viewing activity
- `GET /api/series` and `GET /api/series/{id}` for series, seasons, and episodes
- `GET /api/movies/search?q=&genre=` for title and genre filters

The frontend exposes protected `/my-list` and `/history` screens, a genre filter on `/search`, and series episode details at `/series/:id`.

## Video streaming

Milestone 5 uses local storage and FFmpeg, with no paid cloud dependency:

- Upload: `POST /api/media/movies/{movieId}/upload` as an authenticated multipart request with field `file`
- Subtitles: `POST /api/media/movies/{movieId}/subtitles?language=en&label=English` as an authenticated `.vtt` multipart upload
- Playback token: `GET /api/media/movies/{movieId}/playback-token` with a Bearer token
- HLS: the returned short-lived manifest URL contains a signed five-minute playback token

Install FFmpeg and make `ffmpeg` available on `PATH`, or set `FFMPEG_PATH` to its executable. Set `MEDIA_ROOT` to change the local media directory; the default is `./media`. Uploaded originals, HLS segments, and subtitles are ignored by Git. The frontend player is available at `/watch/{movieId}` after a processed asset is ready.

## Subscriptions and billing

Milestone 6 is wired for Stripe test mode only. Configure these environment variables before using Checkout:

```powershell
$env:STRIPE_SECRET_KEY = "sk_test_..."
$env:STRIPE_WEBHOOK_SECRET = "whsec_..."
$env:FRONTEND_URL = "http://localhost:4200"
```

Replace the placeholder `stripe_price_id` values from the V6 migration/database with Stripe test-mode recurring Price IDs. Start the backend, expose `/api/webhooks/stripe` through the Stripe CLI or a test webhook endpoint, then open `/subscriptions` while signed in. Signed webhook events update local subscription status; playback is denied unless the local subscription is active or trialing.

Billing endpoints:

- `GET /api/subscriptions/plans`
- `GET /api/subscriptions/me` with a Bearer token
- `POST /api/subscriptions/checkout` with `{ "planCode": "basic" }`
- `POST /api/subscriptions/cancel` with a Bearer token
- `POST /api/webhooks/stripe` with Stripe’s `Stripe-Signature` header

## Admin dashboard

Milestone 7 adds an admin-only dashboard at `/admin` for catalogue publishing, movie creation, poster uploads, user/subscription summaries, and basic analytics. The backend protects `/api/admin/**` with the `ADMIN` role. There is no default admin account: register a normal account, then set its email before starting the backend:

```powershell
$env:ADMIN_EMAIL = "admin@example.com"
```

On startup, the matching account is promoted to `ADMIN`. Admin video uploads use the existing local media workflow; poster files are stored under `MEDIA_ROOT/posters`. No paid service is required for the dashboard itself.

## Production readiness

Milestone 8 adds production-oriented configuration, Actuator health/metrics endpoints, request correlation logging, security headers, production environment validation, automated backend/frontend checks, Dockerfiles, GitHub Actions CI, and a PostgreSQL backup script. See [`docs/OPERATIONS.md`](docs/OPERATIONS.md) for deployment, monitoring, backup, recovery, and rollback procedures. Cloud resources are intentionally not provisioned by this repository.

### Database credentials

Spring Boot and Docker Compose both default to PostgreSQL user `streamflix` with password `streamflix`. If an existing local database was initialized with different credentials, set them in the current terminal before starting the backend:

```powershell
$env:DB_USERNAME = "your-existing-db-user"
$env:DB_PASSWORD = "your-existing-db-password"
mvn.cmd -s ..\maven-settings.xml spring-boot:run
```

Do not commit real database passwords. A persistent PostgreSQL volume keeps the credentials from its first initialization; changing Compose variables alone does not change that existing database user.
