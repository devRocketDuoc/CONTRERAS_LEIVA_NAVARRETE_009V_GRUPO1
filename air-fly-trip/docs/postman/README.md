# Air-Fly-Trip Postman

Este directorio contiene la coleccion principal para probar el ecosistema completo desde Postman.

## Archivo recomendado

Importa este archivo:

- [air-fly-trip-complete.postman_collection.json](</C:/Duoc/FullStack I/proyecto-semestral/air-fly-trip/docs/postman/air-fly-trip-complete.postman_collection.json>)

## Que hace la coleccion principal

- usa `api-gateway` para todos los endpoints `/api/v1/*`
- usa los puertos directos de cada microservicio solo para `GET /health`
- guarda `token` y `logged_user_id` automaticamente despues del login
- incluye variables listas para pruebas locales
- evita colisiones en registro y creacion de usuarios usando correos dinamicos

## Variables incluidas

### Infraestructura

- `base_url_gateway = http://localhost:8080`
- `base_url_eureka = http://localhost:8761`

### Bases URL directas por servicio

- `base_url_auth = http://localhost:8082`
- `base_url_terminal = http://localhost:8083`
- `base_url_vehicle = http://localhost:8084`
- `base_url_route = http://localhost:8085`
- `base_url_trip = http://localhost:8086`
- `base_url_reservation = http://localhost:8087`
- `base_url_tariff = http://localhost:8088`
- `base_url_payment = http://localhost:8089`
- `base_url_notification = http://localhost:8090`
- `base_url_charging_station = http://localhost:8091`

### Autenticacion

- `auth_admin_email = admin@airflytrip.cl`
- `auth_admin_password = admin123`
- `token`
- `logged_user_id`

### IDs y valores auxiliares

- `new_user_id`
- `seed_user_id = 3`
- `terminal_id = 1`
- `charging_station_id = 1`
- `vehicle_id = 1`
- `origin_terminal_id = 1`
- `destination_terminal_id = 2`
- `route_id = 1`
- `tariff_id = 1`
- `reservation_id = 1`
- `trip_id = 1`
- `payment_id = 1`
- `notification_id = 1`
- `vehicle_type = STANDARD`
- `notification_type = SYSTEM`
- `notification_status = PENDING`

### Estados de ejemplo

- `station_update_status = MAINTENANCE`
- `vehicle_update_status = CHARGING`
- `reservation_update_status = CONFIRMED`
- `trip_update_status = CONFIRMED`
- `payment_update_status = APPROVED`
- `notification_update_status = SENT`

## Flujo recomendado en Postman

1. Levanta el stack con Docker Compose.
2. Importa `air-fly-trip-complete.postman_collection.json`.
3. Ejecuta la carpeta `infraestructura`.
4. Ejecuta `POST /api/v1/auth/login`.
5. Usa el resto de carpetas para probar endpoints autenticados por gateway.

## Endpoints que conviene ejecutar primero

- `infraestructura -> GET / eureka-server`
- `infraestructura -> GET /eureka/apps`
- `auth-service -> POST /api/v1/auth/login`
- `auth-service -> GET /api/v1/auth/me`

## Nota importante

La coleccion ya trae variables de coleccion y no depende de un environment externo para cargarse correctamente en Postman.
