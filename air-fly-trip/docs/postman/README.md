# Air-Fly-Trip Postman

Importa la coleccion:

- `docs/postman/air-fly-trip-complete.postman_collection.json`

## Flujo recomendado

1. Ejecuta `auth-service -> POST /api/v1/auth/login`.
2. La coleccion guarda automaticamente `token` y `logged_user_id`.
3. Usa ese token para todos los endpoints protegidos.
4. Si quieres crear un usuario con rol distinto, usa `POST /api/v1/auth/users` con token de `ADMIN`.

## Variables incluidas

La coleccion ya trae variables con los puertos locales del stack y con IDs semilla para probar rapido.

### Bases URL

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

### IDs y valores de apoyo

- `token`
- `logged_user_id`
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

### Estados de ejemplo para `PATCH`

- `station_update_status = MAINTENANCE`
- `vehicle_update_status = CHARGING`
- `reservation_update_status = CONFIRMED`
- `trip_update_status = CONFIRMED`
- `payment_update_status = APPROVED`
- `notification_update_status = SENT`

## Comentarios rapidos

- `POST /api/v1/auth/register` siempre crea `CLIENT`.
- `POST /api/v1/auth/users` respeta el rol y requiere token valido.
- Los endpoints publicos reales en esta coleccion son solo los `GET /health` y los dos endpoints de autenticacion inicial (`register` y `login`).
- Para el resto, la coleccion ya manda `Authorization: Bearer {{token}}`.
