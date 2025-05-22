# 🏡  Home360: Tu casa a un clic

## Resumen 📜

Home360 es una plataforma inmobiliaria digital construida sobre una **arquitectura de microservicios**. El sistema prioriza la modularidad, escalabilidad y mantenibilidad. 
La orquestación en desarrollo se gestiona con **Docker y Docker Compose**.

## Componentes Centrales de la Arquitectura 🏗️

* **🚪 API Gateway (`service-gateway`)**:
    * **Tecnología**: Spring Cloud Gateway MVC.
    * **Responsabilidad**: Único punto de entrada (SPE). Enrutamiento, validación de JWT (OAuth2 Resource Server), agregación de Swagger UI.
    * **Puerto** (8080).

* **🧭 Service Discovery (`service-discovery`)**:
    * **Tecnología**: Netflix Eureka Server.
    * **Responsabilidad**: Registro y descubrimiento dinámico de instancias de microservicios.
    * **Puerto(8761).

* **🧩 Microservicios de Negocio**:
    * **👤 `service-user`**: Gestión de usuarios, autenticación (emisión de JWT), roles. BD: `services_user`. Puerto (8081).
    * **🏠 `service-home`**: CRUD de propiedades, categorías, ubicaciones (departamentos, ciudades, barrios), gestión de imágenes (almacenamiento local), tareas programadas para actualización de estado de propiedades. BD: `services_home`. Puerto  (8082).
    * **🚶 `service-visits`**: Gestión de visitas a propiedades (desarrollo inicial). BD: `services_visits`. Puerto (def.): `${SERVER_PORT_SERVICE_VISITS}` (e.g., 8083).
    * **💸 `service-transactions`**: Gestión de transacciones inmobiliarias (desarrollo inicial). BD: `services_transactions`. Puerto (def.): `${SERVER_PORT_SERVICE_TRANSACTIONS}` (e.g., 8084).

* **🗃️ Persistencia (`mysql-db`)**:
    * **Tecnología**: MySQL 8.0.
    * **Configuración**: Esquemas dedicados por servicio (`services_user`, `services_home`, etc.), inicializados por `init-db.sql`. Datos persistidos en volumen Docker.
    * **Puerto (host def.)**: `${MYSQL_DB_PORT}` (e.g., 3306).

* **🛠️ Herramientas Adicionales**:
    * **`phpmyadmin`**: Administración web para MySQL. Puerto (host def.): `${PHPMYADMIN_PORT}` (e.g., 8086).
    * **⚙️ `service-config`**: Spring Cloud Config Server (uso limitado en `compose.yml` actual, configuración principal por variables de entorno). Puerto (def.): `8085`.

## Stack Tecnológico Clave 💻

* **Backend**: Java 17 ☕, Spring Boot 3.x (MVC, Data JPA, Security).
* **Orquestación/Contenerización**: Docker 🐳, Docker Compose.
* **Microservicios**: Spring Cloud Gateway, Netflix Eureka.
* **Seguridad**: JWT (con `nimbus-jose-jwt`), OAuth2 Resource Server.
* **Base de Datos**: MySQL 🐬.
* **Build**: Gradle 🐘.
* **Documentación API**: Swagger/OpenAPI 3 (`springdoc-openapi`) 📖.
* **Mapeo**: MapStruct.

## 4. Flujo de Autenticación (JWT) 🔑

1.  **Login**: Cliente envía credenciales a `POST /api/v1/auth/sign-in` (`service-user` vía Gateway).
2.  **Emisión de Token**: `service-user` valida y genera un JWT firmado (incluye `userId`, `email`, `role`, `exp`).
3.  **Uso del Token**: Cliente incluye `Authorization: Bearer <token>` en cabeceras para rutas protegidas.
4.  **Validación en Gateway**: `service-gateway` valida firma y expiración del JWT.
5.  **Propagación de Identidad**: `ClaimsToHeadersFilter` añade `X-User-Id` y `X-User-Roles` a la petición antes de reenviar al microservicio interno.
6.  **Autorización en Microservicios**: Servicios internos pueden usar estas cabeceras para lógica de negocio o autorización granular.

## Estructura del Repositorio 📂

```bash
.
├── 🐳 compose.yml             # Orquestación de todos los servicios Docker
├── 📜 init-db.sql             # Script SQL inicial para la BD
├── 📄 README.md               # Este archivo
├── ⚙️ service-config/         # Spring Cloud Config Server
├── 🧭 service-discovery/      # Netflix Eureka Server
├── 🚪 service-gateway/        # Spring Cloud API Gateway
├── 🏠 service-home/           # Microservicio de Propiedades
│   ├── src/main/java/com/pragma/home360/home/
│   │   ├── application/  # DTOs, Mappers App, Servicios App
│   │   ├── domain/       # Modelos de Dominio, Puertos, Casos de Uso
│   │   └── infrastructure/ # Endpoints REST, Adaptadores de Persistencia, Entidades JPA
│   ├── build.gradle
│   └── Dockerfile
├── 💸 service-transactions/   # Microservicio de Transacciones
├── 👤 service-user/           # Microservicio de Usuarios y Autenticación
└── 🚶 service-visits/         # Microservicio de Visitas
└── 🖼️ uploads/                # Directorio en host para imágenes (montado en service-home)
    └── property-images/
```

## Inicio Rápido (Comandos Esenciales) 🚀

1.  **Clonar Repositorio**: `git clone <URL_REPO>`
2.  **Configurar `.env`**: Opcional, para variables de entorno (ver `compose.yml`).
3.  **Levantar Servicios**: `docker-compose up -d --build` (desde la raíz del proyecto).
4.  **Ver Logs**: `docker-compose logs -f [nombre-servicio]`
5.  **Detener Servicios**: `docker-compose down` (o `docker-compose down -v` para eliminar volúmenes).

**Endpoints Clave para Verificación:**
* **Eureka Dashboard**: `http://localhost:${SERVICE_DISCOVERY_PORT}` (def: 8761)
* **API Gateway (Swagger UI)**: `http://localhost:${SERVER_PORT_API_GATEWAY}/swagger-ui.html` (def: 8080)
* **phpMyAdmin**: `http://localhost:${PHPMYADMIN_PORT}` (def: 8086)

