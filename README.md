# 🏡 Home360: Tu casa a un click

Bienvenido a **Home360**, una plataforma inmobiliaria digital construida con una arquitectura de microservicios moderna
y robusta. Este proyecto implementa los principios de **Diseño Orientado al Dominio (DDD)** con una estructura *
*Hexagonal (Puertos y Adaptadores)**, priorizando la modularidad, escalabilidad y mantenibilidad. La orquestación en
desarrollo se gestiona con **Docker** y **Docker Compose**.

## 📚 Tabla de Contenidos

- [🧭 Resumen General](#resumen-general)
- [🏗️ Diagrama de Arquitectura](#diagrama-de-arquitectura)
- [⚙️ Descripción de los Microservicios](#descripción-de-los-microservicios)
- [💻 Stack Tecnológico](#stack-tecnológico)
- [🔑 Flujo de Autenticación (JWT)](#flujo-de-autenticación-jwt)
- [🌊 Flujo de una Solicitud Típica](#flujo-de-una-solicitud-típica)
- [📂 Estructura del Repositorio](#estructura-del-repositorio)
- [🚀 Guía de Inicio Rápido](#guía-de-inicio-rápido)
- [🌐 Endpoints Clave para Verificación](#endpoints-clave-para-verificación)

## 🧭 Resumen General

**Home360** es una solución integral para la gestión de propiedades inmobiliarias. El sistema se descompone en varios
microservicios especializados, cada uno manejando un aspecto específico del dominio:

- **Gestión de Usuarios y Autenticación**
- **Administración de Propiedades e Imágenes**
- **Gestión de Visitas** (en desarrollo)
- **Procesamiento de Transacciones** (en desarrollo)

La comunicación entre el cliente y los servicios se centraliza a través de un **API Gateway**, y los servicios se
registran y descubren dinámicamente usando **Netflix Eureka**. La persistencia de datos se maneja con **MySQL**, con
esquemas dedicados por servicio.

## 🏗️ Diagrama de Arquitectura

Un vistazo general de cómo interactúan los componentes principales:

```
+-----------+      +---------------------+      +-------------------------+      +---------------------+      +-----------------+
|  Usuario  |----->|     API Gateway     |----->|   Service Discovery     |<---->|    Microservicio    |----->| Base de Datos   |
| (Cliente) |      | (service-gateway)   |      |   (service-discovery)   |      | (ej: service-home)  |      | (mysql-db)      |
+-----------+      +---------------------+      +-------------------------+      +---------------------+      +-----------------+
      ^                        |                                                            |                        |
      |                        |                                                            |                        |
      +------------------------+----------------------Respuesta-----------------------------+------------------------+
```

## ⚙️ Descripción de los Microservicios

### 🚪 API Gateway (service-gateway)

- **Tecnología**: Spring Cloud Gateway MVC
- **Responsabilidad**: Punto único de entrada (SPE). Enrutamiento, validación de JWT (OAuth2 Resource Server),
  agregación de Swagger UI.
- **Puerto**: 8080

### 🧭 Service Discovery (service-discovery)

- **Tecnología**: Netflix Eureka Server
- **Responsabilidad**: Registro y descubrimiento dinámico de instancias de microservicios.
- **Puerto**: 8761

### 👤 Usuarios (service-user)

- **Gestión**: Usuarios, autenticación (emisión de JWT), roles.
- **Base de Datos**: services_user
- **Puerto**: 8081

### 🏠 Propiedades (service-home)

- **Gestión**: CRUD de propiedades, categorías, ubicaciones (departamentos, ciudades, barrios), gestión de imágenes (
  almacenamiento local), tareas programadas para actualización de estado de propiedades.
- **Base de Datos**: services_home
- **Puerto**: 8082

### 🚶Visitas  (service-visits)

- **Gestión**: Visitas a propiedades (en desarrollo inicial).
- **Base de Datos**: services_visits
- **Puerto (predeterminado)**: ${SERVER_PORT_SERVICE_VISITS} (ej: 8083)

### 💸 Pagos & Trasancciones (service-transactions)

- **Gestión**: Transacciones inmobiliarias (en desarrollo inicial).
- **Base de Datos**: services_transactions
- **Puerto (predeterminado)**: ${SERVER_PORT_SERVICE_TRANSACTIONS} (ej: 8084)

### 🗃️ Persistencia (mysql-db)

- **Tecnología**: MySQL 8.0
- **Configuración**: Esquemas dedicados por servicio, inicializados por `init-db.sql`. Datos persistidos en volumen
  Docker.
- **Puerto (host predeterminado)**:  (ej: 3306)

### 🛠️ Herramientas Adicionales

- **📊 phpmyadmin**: Administración web para MySQL. Puerto: 8088
- **⚙️ service-config**: Spring Cloud Config Server (uso limitado actualmente). Puerto: 8085

## 💻 Stack Tecnológico

- **Backend**: Java 17 ☕, Spring Boot 3.x (MVC, Data JPA, Security)
- **Orquestación/Contenerización**: Docker 🐳, Docker Compose
- **Microservicios**: Spring Cloud Gateway, Netflix Eureka
- **Seguridad**: JWT (con nimbus-jose-jwt), OAuth2 Resource Server
- **Base de Datos**: MySQL 🐬
- **Build**: Gradle 🐘
- **Documentación API**: Swagger/OpenAPI 3 (springdoc-openapi) 📖
- **Mapeo**: MapStruct

## 🔑 Flujo de Autenticación (JWT)

1. **Login**: Cliente envía credenciales a `POST /api/v1/auth/sign-in` (service-user vía Gateway).
2. **Emisión de Token**: service-user valida y genera un JWT firmado (incluye userId, email, role, exp).
3. **Uso del Token**: Cliente incluye `Authorization: Bearer <token>` en cabeceras para rutas protegidas.
4. **Validación en Gateway**: service-gateway valida firma y expiración del JWT.
5. **Propagación de Identidad**: ClaimsToHeadersFilter añade `X-User-Id` y `X-User-Roles` a la petición antes de
   reenviar al microservicio interno.
6. **Autorización en Microservicios**: Servicios internos pueden usar estas cabeceras para lógica de negocio o
   autorización granular.

## 🌊 Flujo de una Solicitud Típica

Este es el viaje típico de una solicitud desde el usuario hasta la base de datos y de regreso:

1. **🌐 Inicio de la Solicitud (Usuario/Cliente)**:

- Un usuario interactúa con la aplicación cliente (navegador web, aplicación móvil).
- La aplicación cliente genera una solicitud HTTP/S dirigida al punto de entrada (ej: `GET /api/v1/properties`,
  `POST /api/v1/auth/sign-in`).

2. **🚪 Recepción en el API Gateway (service-gateway - Puerto 8080)**:

- Todas las solicitudes externas ingresan a través del API Gateway.

3. **🚦 Enrutamiento**: El Gateway determina a qué microservicio interno debe dirigir la solicitud basándose en la ruta y
   la configuración.

4. **🛡️ Validación de Seguridad (JWT)**:

- Si la ruta está protegida, el Gateway verifica la presencia y validez de un Token JWT en la cabecera `Authorization`.
- Valida la firma y la fecha de expiración del token. Si no es válido, la solicitud es rechazada (ej: 401 Unauthorized o
  403 Forbidden).

5. **👤 Propagación de Identidad**: Si el JWT es válido, el filtro `ClaimsToHeadersFilter` extrae claims relevantes (ej:
   userId, roles) y los añade como cabeceras (`X-User-Id`, `X-User-Roles`) a la solicitud antes de reenviarla.

6. **📖 Agregación de Documentación**: El Gateway también expone la interfaz de Swagger UI consolidada.

7. **🧭 Descubrimiento del Servicio (service-discovery - Eureka Server - Puerto 8761)**:

- Antes de reenviar la solicitud, el API Gateway (y cualquier microservicio que necesite comunicarse con otro) consulta
  al Service Discovery (Eureka).
- Eureka proporciona la ubicación de red (dirección IP y puerto) de una instancia disponible del microservicio destino.

8. **🧩 Procesamiento en el Microservicio Específico (ej: service-home - Puerto 8082)**:

- La solicitud, enriquecida con las cabeceras de identidad si aplica, llega al microservicio correspondiente.

9. **🧠 Lógica de Dominio y Aplicación**:

- El controlador REST del microservicio recibe la solicitud.
- Se invocan los servicios de aplicación, que orquestan los casos de uso del dominio.
- Se utilizan los modelos de dominio y los puertos (interfaces) definidos en la arquitectura hexagonal.

10. **🔐 Autorización Granular (Opcional)**: El microservicio puede usar las cabeceras `X-User-Id` y `X-User-Roles` para
    lógicas de autorización más específicas.

11. **🗃️ Interacción con la Base de Datos (mysql-db - Puerto 3306 en host)**:

- El adaptador de persistencia del microservicio (implementando un puerto de repositorio) interactúa con la base de
  datos MySQL.
- Se realizan operaciones CRUD sobre el esquema dedicado del servicio.
- Se utilizan entidades JPA y repositorios Spring Data JPA.

12. **↩️ Generación y Retorno de la Respuesta**:

- El microservicio procesa la solicitud y genera una respuesta (ej: datos de propiedades en JSON, un nuevo JWT).
- La respuesta viaja de regreso al API Gateway.

13. **↪️ Entrega de la Respuesta al Cliente**:

- El API Gateway recibe la respuesta del microservicio.
- Puede realizar transformaciones adicionales mínimas si es necesario.
- Finalmente, el API Gateway envía la respuesta HTTP/S de vuelta al cliente original.

### 🤔 Consideraciones Adicionales del Flujo

- **⚙️ Configuración Centralizada (service-config - Puerto 8085)**: Podría ser consultado por los microservicios al
  arrancar para obtener su configuración externa.
- **🤝 Comunicación Inter-Servicios**: Si un microservicio necesita datos de otro, sigue un flujo similar: Microservicio
  A → Service Discovery → Microservicio B → Base de Datos de B → Respuesta a Microservicio A.
- **❗ Manejo de Errores**: Cada componente es responsable de manejar errores. El API Gateway puede estandarizar los
  formatos de error.

## 📂 Estructura del Repositorio

```
├── 🐳 compose.yml             # Orquestación de todos los servicios Docker
├── 📜 init-db.sql             # Script SQL inicial para la BD
├── 📄 README.md               # Este archivo
├── ⚙️ service-config/         # Spring Cloud Config Server
├── 🧭 service-discovery/      # Netflix Eureka Server
├── 🚪 service-gateway/        # Spring Cloud API Gateway
├── 🏠 service-home/           # Microservicio de Propiedades
│   ├── src/main/java/com/pragma/home360/home/
│   │   ├── application/  # DTOs, Mappers de Aplicación, Servicios de Aplicación
│   │   ├── domain/       # Modelos de Dominio, Puertos (Interfaces), Casos de Uso
│   │   └── infrastructure/ # Endpoints REST, Adaptadores de Persistencia (JPA Entities, Repositories)
│   ├── build.gradle
│   └── Dockerfile
├── 💸 service-transactions/   # Microservicio de Transacciones (en desarrollo)
├── 👤 service-user/           # Microservicio de Usuarios y Autenticación
└── 🚶 service-visits/         # Microservicio de Visitas (en desarrollo)
└── 🖼️ uploads/                # Directorio en el host para imágenes (montado en service-home)
    └── property-images/
```

## 🚀 Guía de Inicio Rápido

### Requisitos Previos

Antes de comenzar, asegúrate de tener instalados los siguientes componentes en tu máquina:

- [Docker](https://www.docker.com/get-started) & [Docker Compose](https://docs.docker.com/compose/)
- [Git](https://git-scm.com/)

### 📥 Clone the Repository

   ```bash
   git clone https://github.com/usyeimar/pragma-power-up-backend-home-360.git
   cd pragma-power-up-backend-home-360
   ```

### 🍄 Configurar Archivo .env (Opcional)

- Puedes crear un archivo `.env` en la raíz del proyecto para personalizar variables de entorno (puertos, credenciales,
  etc.). Consulta `compose.yml` para ver las variables disponibles y sus valores por defecto.

### 🐳 Run the Stack

Desde la raíz del proyecto, ejecuta:

  ```bash
  docker-compose up -d --build
  ```

### Ver Logs de los Servicios

Para monitorizar un servicio específico:

  ```bash
  docker-compose logs -f [nombre-del-servicio]
  ```

(ej: `docker-compose logs -f service-home`)

Para ver todos los logs:
  ```bash
  docker-compose logs -f
  ```

### Detener los Servicios
   ```bash
   docker-compose down
   ```

Si además deseas eliminar los volúmenes de Docker (¡cuidado, esto borrará los datos de la BD!):
  ```bash
  docker-compose down -v
  ```

## 🌐 Endpoints Clave para Verificación

- **Dashboard de Eureka**:
    - `http://localhost:8761`
- **API Gateway (Swagger UI - Documentación de APIs)**:
    - `http://localhost:8080/swagger-ui.html`
- **phpMyAdmin (Gestor de Base de Datos)**:
    - `http://localhost:8088`
