# Air-Fly-Trip

Base técnica académica para 10 microservicios Spring Boot independientes.

## Microservicios activos

- `auth-service`
- `terminal-service`
- `vehicle-service`
- `route-service`
- `trip-service`
- `reservation-service`
- `tariff-service`
- `payment-service`
- `notification-service`
- `charging-station-service`

## Stack base

- Java 21
- Spring Boot 4.0.6
- Maven
- MySQL
- Flyway
- OpenFeign
- Docker
- Docker Compose

## Ejecución local con Docker Compose

```bash
docker compose down -v
docker compose up -d --build
docker compose ps
```

## Puertos activos

| Servicio                   | Puerto app | Base de datos          | Puerto BD |
| -------------------------- | ---------- | ---------------------- | --------- |
| `auth-service`             | `8081`     | `db_auth`              | `3307`    |
| `terminal-service`         | `8082`     | `db_terminals`         | `3308`    |
| `vehicle-service`          | `8083`     | `db_vehicles`          | `3309`    |
| `route-service`            | `8084`     | `db_routes`            | `3310`    |
| `trip-service`             | `8085`     | `db_trips`             | `3311`    |
| `reservation-service`      | `8086`     | `db_reservations`      | `3312`    |
| `tariff-service`           | `8087`     | `db_tariffs`           | `3313`    |
| `payment-service`          | `8088`     | `db_payments`          | `3314`    |
| `notification-service`     | `8089`     | `db_notifications`     | `3315`    |
| `charging-station-service` | `8090`     | `db_charging_stations` | `3316`    |

## Notas de arquitectura

- Cada microservicio usa su propia base de datos MySQL.
- Flyway administra el esquema inicial.
- `auth-service` queda preparado para Spring Security, JWT y BCrypt.
