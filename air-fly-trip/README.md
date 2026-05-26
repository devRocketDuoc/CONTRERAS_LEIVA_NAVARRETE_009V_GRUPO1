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

Siempre iniciar con reset completo para arrancar desde cero en todos los microservicios y sus bases.

### Opción recomendada (script único)

Linux / macOS / WSL:

```bash
./scripts/reset-and-up.sh
```

Windows (CMD o PowerShell):

```bat
scripts\reset-and-up.cmd
```

### Opción manual equivalente

```bash
docker compose down --volumes --remove-orphans
docker compose up -d --build --force-recreate
docker compose ps
```

## Reset manual de auth-service (Flyway desde cero)

Usar este flujo cuando quieras rehacer solo `auth-service` y su base `db_auth`.

1. Validar nombres de migraciones Flyway (ejemplo: `V2__...sql` con doble guion bajo).

```bash
ls auth-service/src/main/resources/db/migration
```

2. Eliminar contenedor de app y base de `auth` junto con su volumen.

```bash
docker rm -fv auth-service auth-db
```

3. Levantar nuevamente `auth-service`.

```bash
docker compose up -d --build auth-service
```

4. Verificar que Flyway aplicó migraciones.

```bash
docker exec auth-db mysql -uroot -proot -D db_auth -e "SELECT installed_rank,version,description,script,success FROM flyway_schema_history ORDER BY installed_rank;"
```

5. Verificar estado del servicio.

```bash
curl -i http://localhost:8082/health
```

## Puertos activos

| Servicio                   | Puerto app | Base de datos          | Puerto BD |
| -------------------------- | ---------- | ---------------------- | --------- |
| `auth-service`             | `8082`     | `db_auth`              | `3308`    |
| `terminal-service`         | `8083`     | `db_terminals`         | `3309`    |
| `vehicle-service`          | `8084`     | `db_vehicles`          | `3310`    |
| `route-service`            | `8085`     | `db_routes`            | `3311`    |
| `trip-service`             | `8086`     | `db_trips`             | `3312`    |
| `reservation-service`      | `8087`     | `db_reservations`      | `3313`    |
| `tariff-service`           | `8088`     | `db_tariffs`           | `3314`    |
| `payment-service`          | `8089`     | `db_payments`          | `3315`    |
| `notification-service`     | `8090`     | `db_notifications`     | `3316`    |
| `charging-station-service` | `8091`     | `db_charging_stations` | `3317`    |

## Notas de arquitectura

- Cada microservicio usa su propia base de datos MySQL.
- Flyway administra el esquema inicial.
