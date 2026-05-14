# Air-Fly-Trip - Desarrollo con perfiles local y docker

Este proyecto usa perfiles de Spring para soportar dos modos de ejecución:

- `local`: el microservicio corre fuera de Docker (VS Code, IntelliJ o Maven) y se conecta a MySQL por `localhost:<puerto_db_externo>`.
- `docker`: el microservicio corre dentro de Docker Compose y se conecta a MySQL por nombre de contenedor (`<db-service>:3306`).

## 1) Ejecutar un microservicio en modo local (app fuera de Docker)

Ejemplo con `auth-service`:

```bash
cd ~/Desktop/proyecto-fullstack/air-fly-trip

# Importante: detener el contenedor del microservicio si estaba arriba
# para evitar conflictos de puerto con VS Code (8081 en auth-service).
docker compose stop auth-service

# Levantar solo la base de datos del microservicio a probar.
docker compose up -d auth-db

# Verificar estado de contenedores.
docker compose ps
```

Luego ejecutar el microservicio desde VS Code/IntelliJ:

- Clase principal: `AuthServiceApplication.java`
- Botón `Play` (Run Java)

O por terminal:

```bash
cd auth-service
mvn spring-boot:run
```

Notas importantes:

- En modo local, el microservicio corre fuera de Docker.
- La base de datos puede correr en Docker.
- La conexión se hace a `localhost:<puerto_externo_db>` (por ejemplo `localhost:3307` en `auth-service`).
- No es necesario levantar todos los microservicios para trabajar en uno solo.
- Si `auth-service` está arriba en Docker y también ejecutas con `Play` en VS Code, habrá choque de puerto.
- En Git Bash, usa rutas como `~/Desktop/...` o `/c/Users/...` (no `/mnt/c/...`).

## 2) Ejecutar un microservicio en modo Docker (app + db en Docker)

Ejemplo con `auth-service`:

```bash
docker compose up -d --build auth-service
```

Con `depends_on` configurado, se levantan `auth-service` y su base `auth-db`.

## 3) Ver logs

```bash
docker compose logs -f auth-service
```

## 4) Reiniciar base desde cero

```bash
docker compose down -v
docker compose up -d --build auth-service
```

Advertencia:

- `docker compose down -v` elimina volúmenes y borra los datos de las bases MySQL.

## 5) Validación rápida esperada

### Modo Docker

```bash
docker compose down -v
docker compose up -d --build auth-service
docker compose logs -f auth-service
```

Debe verse:

- Perfil activo: `docker`
- Host/puerto de DB: `auth-db:3306`

### Modo local

```bash
cd ~/Desktop/proyecto-fullstack/air-fly-trip
docker compose stop auth-service
docker compose up -d auth-db

# Opción VS Code:
# Play en AuthServiceApplication.java

# Opción terminal:
cd auth-service
mvn spring-boot:run
```

Debe verse:

- Perfil activo: `local`
- Host/puerto de DB: `localhost:3307`
