# PR: Add secured user lookup endpoint by ID in auth-service

## Summary
This PR adds a secured endpoint in `auth-service` to retrieve user information by ID so other microservices can validate/fetch user data through Feign without duplicating auth logic.

## Changes
- Added endpoint:
  - `GET /api/v1/auth/users/{id}`
  - Controller: `AuthController#getUserById(Long id)`
- Added service method:
  - `AuthService#findUserById(Long id)`
  - Uses `appUserRepository.findById(id)`
  - Throws `NotFoundException("User not found")` when user does not exist
  - Maps `AppUser` to `UserResponse`

## Security
- Endpoint is protected by JWT.
- No changes to public endpoints (`/register`, `/login`, `/me`) or existing `/users` behavior.
- No exposure of `passwordHash` in response (`UserResponse` does not include it).

## Compatibility for Feign consumers
Works for internal Docker DNS and standard URL:
- `http://auth-service:8081/api/v1/auth/users/{id}`

Example response:
```json
{
  "id": 1,
  "email": "admin@airflytrip.cl",
  "firstName": "Admin",
  "lastName": "AirFlyTrip",
  "documentNumber": "11111111-1",
  "phone": "+56911111111",
  "role": "ADMIN",
  "status": "ACTIVE",
  "enabled": true,
  "createdAt": "..."
}
```

## Validation done
- Docker up/build for `auth-db` and `auth-service`: successful.
- `GET /health`: `200`.
- `GET /api/v1/auth/users/{id}` with valid JWT: `200`.
- Same endpoint without JWT: `401`.
- Same endpoint with non-existing id and valid JWT: `404`.
- Internal Docker-network call to `http://auth-service:8081/...`: `200`.

## Non-goals / untouched areas
- No Flyway migration changes.
- No Docker/ports/JWT changes.
- No package rename.
- No `app_user` table change.
- No `SecurityConfig` change required.
