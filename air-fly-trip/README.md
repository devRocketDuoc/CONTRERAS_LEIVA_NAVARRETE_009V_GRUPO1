# Air-Fly-Trip

Proyecto academico basado en microservicios Spring Boot para la gestion de autenticacion, terminales, vehiculos, rutas, viajes, reservas, tarifas, pagos, notificaciones y estaciones de carga.

## Arquitectura

La solucion queda compuesta por:

- `eureka-server` para service discovery
- `api-gateway` para enrutamiento centralizado
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

## Stack tecnico

- Java 21
- Spring Boot 4.0.6
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Springdoc OpenAPI
- Spring Security con JWT
- OpenFeign
- MySQL 8.4
- Flyway
- Maven Wrapper
- Docker y Docker Compose

## Puertos del proyecto

| Componente | Puerto |
| --- | --- |
| `api-gateway` | `8080` |
| `eureka-server` | `8761` |
| `auth-service` | `8082` |
| `terminal-service` | `8083` |
| `vehicle-service` | `8084` |
| `route-service` | `8085` |
| `trip-service` | `8086` |
| `reservation-service` | `8087` |
| `tariff-service` | `8088` |
| `payment-service` | `8089` |
| `notification-service` | `8090` |
| `charging-station-service` | `8091` |

## Puertos de bases de datos

| Base | Puerto |
| --- | --- |
| `auth-db` | `3308` |
| `terminal-db` | `3309` |
| `vehicle-db` | `3310` |
| `route-db` | `3311` |
| `trip-db` | `3312` |
| `reservation-db` | `3313` |
| `tariff-db` | `3314` |
| `payment-db` | `3315` |
| `notification-db` | `3316` |
| `charging-station-db` | `3317` |

## Levantar el proyecto

Windows:

```bat
scripts\\reset-and-up.cmd
```

Linux, macOS o WSL:

```bash
./scripts/reset-and-up.sh
```

Equivalente manual:

```bash
docker compose down --volumes --remove-orphans
docker compose up -d --build --force-recreate
docker compose ps
```

## Validacion rapida del ecosistema

Windows:

```bat
scripts\\validate-gateway.cmd
```

PowerShell:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\\scripts\\validate-gateway.ps1
```

Linux, macOS o WSL:

```bash
./scripts/validate-gateway.sh
```

Estos scripts validan:

- disponibilidad de Eureka
- disponibilidad de API Gateway
- login y almacenamiento de token
- acceso autenticado a los endpoints principales por gateway

## Rutas del gateway

El gateway expone los endpoints funcionales por `http://localhost:8080`:

- `/api/v1/auth/**`
- `/api/v1/routes/**`
- `/api/v1/vehicles/**`
- `/api/v1/trips/**`
- `/api/v1/reservations/**`
- `/api/v1/tariffs/**`
- `/api/v1/payments/**`
- `/api/v1/notifications/**`
- `/api/v1/terminals/**`
- `/api/v1/charging-stations/**`

## Swagger y OpenAPI

Cada microservicio expone su interfaz Swagger UI en:

| Servicio | Swagger UI |
| --- | --- |
| `auth-service` | `http://localhost:8082/doc/swagger-ui.html` |
| `terminal-service` | `http://localhost:8083/doc/swagger-ui.html` |
| `vehicle-service` | `http://localhost:8084/doc/swagger-ui.html` |
| `route-service` | `http://localhost:8085/doc/swagger-ui.html` |
| `trip-service` | `http://localhost:8086/doc/swagger-ui.html` |
| `reservation-service` | `http://localhost:8087/doc/swagger-ui.html` |
| `tariff-service` | `http://localhost:8088/doc/swagger-ui.html` |
| `payment-service` | `http://localhost:8089/doc/swagger-ui.html` |
| `notification-service` | `http://localhost:8090/doc/swagger-ui.html` |
| `charging-station-service` | `http://localhost:8091/doc/swagger-ui.html` |

## Postman

La coleccion principal para probar el proyecto completo esta en:

- [air-fly-trip-complete.postman_collection.json](</C:/Duoc/FullStack I/proyecto-semestral/air-fly-trip/docs/postman/air-fly-trip-complete.postman_collection.json>)
- [README de Postman](</C:/Duoc/FullStack I/proyecto-semestral/air-fly-trip/docs/postman/README.md>)

La coleccion ya incluye:

- variables de coleccion
- autenticacion por JWT
- flujo por `api-gateway` para todos los endpoints `/api/v1/*`
- endpoints directos de `health` por microservicio

No requiere un environment separado para funcionar.

## Pruebas unitarias

Cada microservicio incluye pruebas unitarias enfocadas en controladores y servicios.

Para ejecutar pruebas de un microservicio:

```bash
cd <microservice>
./mvnw test
```

En Windows:

```bat
cd <microservice>
mvnw.cmd test
```

## Estado actual

El proyecto queda alineado en los 10 microservicios con:

- documentacion Swagger y OpenAPI
- pruebas unitarias
- registro en Eureka
- exposicion por API Gateway
- coleccion Postman principal para el ecosistema completo
