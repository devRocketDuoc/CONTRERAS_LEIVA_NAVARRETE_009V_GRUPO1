# Air-Fly-Trip: Plataforma de Movilidad Aerea Autonoma

Air-Fly-Trip es un sistema distribuido diseñado para la gestion operativa y comercial de una red de vehiculos aereos autonomos que operan viajes bajo demanda entre terminales aereas propias denominadas vertipuertos. La plataforma permite coordinar de forma automatizada clientes, terminales, vehiculos, rutas, suscripciones, viajes, pagos y mantenimiento, garantizando la trazabilidad de cada operacion.

## Arquitectura y Tecnologias

El sistema implementa una arquitectura de microservicios completamente desacoplada, donde cada componente es independiente y gestiona su propia persistencia de datos.

* **Backend**: Java 21, Spring Boot 4.0.6, Spring Data JPA y Spring Security.
* **Infraestructura**: Spring Cloud Gateway para el enrutamiento y Eureka Server para el Service Discovery.
* **Mensajeria**: Apache Kafka para la comunicacion asincrona basada en eventos.
* **Bases de Datos**: MySQL 8.x, con una instancia independiente por cada microservicio para asegurar el aislamiento de datos.
* **Migraciones**: Flyway para la gestion de versiones del esquema de base de datos.
* **Seguridad**: Autenticacion y autorizacion basada en JSON Web Tokens (JJWT) y cifrado de contraseñas con BCrypt.
* **Contenedores**: Docker y Docker Compose para la orquestacion de servicios y dependencias.

## Catalogo de Microservicios

El ecosistema se compone de los siguientes microservicios integrados:

| Servicio | Puerto | Base de Datos | Responsabilidad Principal |
| :--- | :--- | :--- | :--- |
| api-gateway | 8080 | N/A | Punto de entrada unico, seguridad perimetral y enrutamiento. |
| eureka-server | 8761 | N/A | Registro y localizacion dinamica de instancias de servicios. |
| ms-auth | 8081 | db_auth | Gestion de credenciales, roles y emision de tokens JWT. |
| ms-users | 8082 | db_users | Administracion de perfiles de clientes, operadores y administradores. |
| ms-terminals | 8083 | db_terminals | Gestion de vertipuertos y su estado operativo. |
| ms-vehicles | 8084 | db_vehicles | Control de la flota, niveles de bateria y estados de disponibilidad. |
| ms-routes | 8085 | db_routes | Definicion de trayectos habilitados y calculo de tarifas base. |
| ms-subscriptions | 8086 | db_subscriptions | Administracion de planes comerciales y beneficios por cliente. |
| ms-trips | 8087 | db_trips | Gestion del ciclo de vida del viaje: solicitud, asignacion y finalizacion. |
| ms-payments | 8088 | db_payments | Registro de transacciones financieras y validacion de cobros. |
| ms-operations | 8089 | db_operations | Gestion de procesos tecnicos como carga de bateria y mantenimiento. |
| ms-audit | 8090 | db_audit | Registro inmutable de eventos del sistema para auditoria y trazabilidad. |

## Instrucciones de Despliegue

### Requisitos Previos
* Docker y Docker Compose instalados.
* Java JDK 21.
* Maven 3.9 o superior.

### Ejecucion del Sistema
El proyecto esta configurado para iniciarse integralmente mediante Docker Compose, incluyendo la infraestructura de mensajeria y bases de datos.

1. Clonar el repositorio:
   ```bash
   git clone
   cd air-fly-trip
### Compilar los microservicios:

```bash
mvn clean package -DskipTests
```

### Iniciar los contenedores:

```bash
docker-compose up -d --build
```

### Detener los servicios:

```bash
docker-compose down
```

## Documentacion de la API

La documentacion tecnica de los endpoints se genera automaticamente mediante SpringDoc OpenAPI. Una vez que el sistema este operativo, puede acceder a la interfaz interactiva de Swagger a traves del API Gateway:

* **Swagger UI**: http://localhost:8080/swagger-ui.html

Para una revision detallada del diseño funcional de los endpoints y su relacion con las reglas de negocio, consulte el documento: `docs/api-endpoints.md`.
