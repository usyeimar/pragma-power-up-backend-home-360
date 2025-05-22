# 🏡 Proyecto Home360 - Arquitectura de Microservicios 🌟

¡Hola! 👋 ¡Bienvenido/a al Proyecto "Home360"! Este documento describe la arquitectura de microservicios implementada para nuestra plataforma inmobiliaria. Está diseñado para ser una guía clara y concisa para desarrolladores y cualquier persona interesada en entender cómo funciona nuestro sistema.

## 🎯 Propósito

El objetivo principal de "Home360" es ofrecer una **plataforma inmobiliaria digital robusta, escalable y modular**. Buscamos facilitar la gestión integral de propiedades, usuarios, transacciones y visitas, proporcionando una experiencia de usuario fluida y eficiente, todo ello soportado por una arquitectura moderna de microservicios.

## 🏗️ Arquitectura General

El sistema "Home360" se basa en una **arquitectura de microservicios**. Cada servicio es responsable de una funcionalidad de negocio específica, lo que permite un desarrollo, despliegue y escalado independientes. Estos servicios se comunican entre sí, principalmente de forma síncrona a través de REST APIs.

La orquestación de estos servicios en un entorno de desarrollo y pruebas se realiza mediante **Docker y Docker Compose**.

Componentes clave de la infraestructura incluyen:

* **Netflix Eureka**: Para el descubrimiento de servicios, permitiendo que los microservicios se encuentren dinámicamente.
* **Spring Cloud Gateway**: Actúa como la puerta de enlace API, manejando todas las solicitudes externas y enrutándolas al servicio correspondiente. También se encarga de aspectos transversales como la seguridad y la agregación de documentación.
* **MySQL**: Como base de datos relacional principal, con esquemas separados para diferentes servicios, garantizando el aislamiento de datos.

<p align="center">
  <img src="https://placehold.co/700x450/E6F7FF/0050B3?text=Diagrama+Arquitectura+Home360+Microservicios" alt="Diagrama de Arquitectura Home360">
  <br>
  <em>Ilustración conceptual de la arquitectura de microservicios de Home360.</em>
</p>

### 🧩 Componentes Principales y sus Funciones:

1.  **🚪 Service Gateway (`service-gateway`)**:
    * **Descripción**: Es el único punto de entrada (`Single Point of Entry`) para todas las peticiones de los clientes (navegadores web, aplicaciones móviles, etc.). Su principal responsabilidad es recibir las solicitudes, autenticar y autorizar (si es necesario), y luego enrutarlas al microservicio interno apropiado.
    * **Tecnología Clave**: Spring Cloud Gateway MVC.
    * **Características Destacadas**:
        * **Enrutamiento Dinámico**: Configura rutas para dirigir el tráfico a los diferentes microservicios basándose en los paths de las URLs.
        * **Integración con Service Discovery**: Utiliza Eureka para descubrir las ubicaciones de los microservicios.
        * **Seguridad Centralizada**: Actúa como un OAuth2 Resource Server, validando los tokens JWT recibidos en las cabeceras `Authorization`.
        * **Filtros Personalizados**: Incluye `ClaimsToHeadersFilter` que extrae información del JWT (como `userId` y `roles`) y la añade como cabeceras (`X-User-Id`, `X-User-Roles`) a las solicitudes que se reenvían a los servicios internos. Esto permite a los microservicios identificar al usuario y sus permisos sin necesidad de validar el JWT nuevamente.
        * **Agregación de Documentación API**: Consolida la documentación Swagger/OpenAPI de los microservicios subyacentes, ofreciendo un único punto (`/swagger-ui.html`) para explorar todas las APIs del sistema.
    * **Puerto por Defecto**: `8080` (configurable mediante la variable de entorno `${SERVER_PORT_API_GATEWAY}`).

2.  **🧭 Service Discovery (`service-discovery`)**:
    * **Descripción**: Implementa el patrón de Descubrimiento de Servicios. Cada microservicio se registra en Eureka al arrancar, y Eureka mantiene un registro de las instancias activas y sus ubicaciones (host y puerto). Esto permite a los servicios (especialmente al API Gateway) encontrar y comunicarse con otros servicios sin necesidad de conocer sus direcciones IP de antemano.
    * **Tecnología Clave**: Netflix Eureka Server.
    * **Puerto por Defecto**: `8761` (configurable mediante `${SERVICE_DISCOVERY_PORT}`).

3.  **⚙️ Service Config (`service-config`)**:
    * **Descripción**: Diseñado para centralizar la gestión de la configuración para todos los microservicios. En un entorno de producción típico, se conectaría a un repositorio Git donde se almacenan los archivos de configuración. Los microservicios, al arrancar, consultarían a este servidor para obtener su configuración específica.
    * **Tecnología Clave**: Spring Cloud Config Server.
    * **Perfil Actual**: Configurado con el perfil `native`, lo que sugiere que en el entorno de desarrollo actual podría estar sirviendo configuraciones desde el sistema de archivos local del contenedor del Config Server. Sin embargo, el `compose.yml` no muestra un montaje explícito de un directorio de configuraciones para este servicio.
    * **Uso en el Proyecto**: Aunque presente, la configuración de los microservicios en el `compose.yml` se realiza predominantemente a través de variables de entorno. Esto podría indicar que el `service-config` se utiliza de forma limitada o está preparado para una configuración más avanzada en entornos de producción.
    * **Puerto por Defecto**: `8085`.

4.  **👤 Service User (`service-user`)**:
    * **Descripción**: Microservicio dedicado a la gestión de usuarios. Maneja el registro de nuevos usuarios, la autenticación (verificación de credenciales y emisión de JWTs), la gestión de perfiles de usuario y roles.
    * **Tecnologías Clave**: Spring Boot, Spring Security, JWT (utilizando `nimbus-jose-jwt` para la creación y validación de tokens).
    * **Base de Datos**: Se conecta al esquema `services_user` en la instancia de MySQL.
    * **Características Destacadas**:
        * Endpoints para `POST /api/v1/auth/sign-in` (para iniciar sesión y obtener un JWT) y `POST /api/v1/users` (para registrar nuevos usuarios, por defecto con rol "VENDEDOR").
        * Implementa `UserDetailsService` de Spring Security para cargar los detalles del usuario durante la autenticación.
        * Utiliza `BCryptPasswordEncoder` para el almacenamiento seguro de contraseñas.
        * `DataInitializer`: Un `CommandLineRunner` que crea un usuario administrador por defecto si no existe al iniciar la aplicación (configurable mediante variables de entorno `DEFAULT_USER_*`).
    * **Puerto por Defecto**: `8081` (configurable mediante `${SERVER_PORT_SERVICE_USER}`).

5.  **🏠 Service Home (`service-home`)**:
    * **Descripción**: Considerado el corazón de la plataforma en cuanto a la gestión de propiedades. Es responsable de toda la lógica de negocio relacionada con los inmuebles, incluyendo sus categorías, ubicaciones geográficas (departamentos, ciudades, barrios), y la gestión de las imágenes asociadas.
    * **Tecnologías Clave**: Spring Boot, Spring Data JPA.
    * **Base de Datos**: Se conecta al esquema `services_home` en la instancia de MySQL.
    * **Características Destacadas**:
        * Operaciones CRUD completas para entidades como `Category`, `Department`, `City`, `Neighborhood`, `Location`, y `Property`.
        * **Gestión de Imágenes**: Permite la carga de imágenes para las propiedades. Las imágenes se almacenan en el sistema de archivos local del contenedor (`./uploads/property-images`, accesible externamente a través del API Gateway en `/media/properties/...`).
        * **Tareas Programadas**: Incluye `PropertyScheduledTasks`, una tarea que se ejecuta periódicamente (cada 60 segundos) para actualizar el estado de las propiedades cuya fecha de publicación activa ha llegado (cambiando de `PUBLICATION_PENDING` a `PUBLISHED`).
        * **Población Inicial de Datos**: `DatabaseSeeder` es un `CommandLineRunner` que carga datos maestros iniciales (departamentos, ciudades, categorías, etc.) si las tablas correspondientes están vacías.
    * **Puerto por Defecto**: `8082` (configurable mediante `${SERVER_PORT_SERVICE_HOME}`).

6.  **🚶 Service Visits (`service-visits`)**:
    * **Descripción**: Este microservicio está destinado a gestionar la programación y seguimiento de visitas a las propiedades.
    * **Tecnologías Clave**: Spring Boot.
    * **Base de Datos**: Se conecta al esquema `services_visits` en la instancia de MySQL.
    * **Estado Actual**: La funcionalidad detallada parece estar en una fase inicial de desarrollo según los archivos proporcionados.
    * **Puerto por Defecto**: `8083` (configurable mediante `${SERVER_PORT_SERVICE_VISITS}`).

7.  **💸 Service Transactions (`service-transactions`)**:
    * **Descripción**: Responsable de manejar todas las transacciones financieras y contractuales relacionadas con las propiedades, como procesos de alquiler o venta.
    * **Tecnologías Clave**: Spring Boot.
    * **Base de Datos**: Se conecta al esquema `services_transactions` en la instancia de MySQL.
    * **Estado Actual**: Similar a `service-visits`, la implementación detallada parece estar pendiente.
    * **Puerto por Defecto**: `8084` (configurable mediante `${SERVER_PORT_SERVICE_TRANSACTIONS}`).

8.  **🗃️ MySQL Database (`mysql-db`)**:
    * **Descripción**: Instancia de base de datos relacional que sirve como almacén de persistencia para todos los microservicios.
    * **Tecnología**: MySQL versión 8.0 (imagen oficial de Docker).
    * **Configuración**:
        * El script `init-db.sql` se ejecuta al iniciar el contenedor por primera vez, creando las bases de datos (`services_user`, `services_home`, `services_visits`, `services_transactions`) y otorgando los privilegios necesarios al usuario de la aplicación (`365home_app`).
        * Los datos se persisten en un volumen Docker (`mysql-db-data`) para evitar la pérdida de datos entre reinicios del contenedor.
        * Incluye un `healthcheck` para asegurar que la base de datos esté operativa antes de que otros servicios dependientes intenten conectarse.
    * **Puerto Expuesto (Host)**: `3306` (configurable mediante `${MYSQL_DB_PORT}`).

9.  **🛠️ phpMyAdmin (`phpmyadmin`)**:
    * **Descripción**: Herramienta de administración web para MySQL, que facilita la visualización, consulta y manipulación de los datos en la base de datos.
    * **Tecnología**: phpMyAdmin (imagen oficial de Docker).
    * **Acceso**: Se conecta al servicio `mysql-db`.
    * **Puerto Expuesto (Host)**: `8086` (configurable mediante `${PHPMYADMIN_PORT}`).

## 🛠️ Stack Tecnológico Principal

* **Lenguaje de Programación**: Java 17 ☕
* **Framework Backend**: Spring Boot 3.x 🌱 (con Spring MVC, Spring Data JPA, Spring Security)
* **Gestión de Dependencias y Construcción**: Gradle 🐘
* **Contenerización y Orquestación**: Docker 🐳 y Docker Compose
* **Patrones y Componentes de Microservicios**:
    * **API Gateway**: Spring Cloud Gateway MVC
    * **Descubrimiento de Servicios**: Spring Cloud Netflix Eureka
    * **Configuración Centralizada**: Spring Cloud Config Server
    * **Seguridad (Autenticación/Autorización)**: Spring Security (con OAuth2 Resource Server y JWT)
* **Base de Datos**: MySQL 8.0 🐬
* **Herramienta de Administración de BD**: phpMyAdmin
* **Documentación de APIs**: Swagger/OpenAPI 3 (integrado mediante `springdoc-openapi`) 📖
* **Mapeo de Objetos (DTOs/Entidades)**: MapStruct
* **Tareas Programadas**: Spring Scheduling

## 🚀 Guía de Inicio Rápido (Desarrollo Local)

Sigue estos pasos para configurar y ejecutar el entorno de desarrollo de "Home360":

1.  **Prerrequisitos Indispensables**:
    * Docker instalado y en ejecución.
    * Docker Compose instalado.
    * Git para clonar el repositorio.

2.  **Clonar el Repositorio**:
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd <NOMBRE_DEL_DIRECTORIO_DEL_PROYECTO>
    ```

3.  **Configuración de Variables de Entorno (Opcional pero Recomendado)**:
    * Crea un archivo `.env` en la raíz del proyecto. Este archivo es utilizado por Docker Compose para configurar variables de entorno.
    * Puedes copiar las variables del archivo `compose.yml` y ajustar sus valores según sea necesario (ej. puertos, credenciales de la base de datos, secretos de JWT).
        ```env
        # Ejemplo de contenido para .env
        SERVER_PORT_API_GATEWAY=8080
        SERVICE_DISCOVERY_PORT=8761
        SERVER_PORT_SERVICE_USER=8081
        # ... (otras variables) ...
        MYSQL_ROOT_PASSWORD=tu_super_secreto_root_password
        MYSQL_USER=365home_app
        MYSQL_PASSWORD=tu_password_segura_para_app
        MYSQL_DB_PORT=3306
        # ... (JWT secrets, etc.)
        APP_JWT_SECRET=UnaClaveSecretaMuyLargaYComplejaParaHS512DeAlMenos64Bytes!NotRealSecretChangeMe
        ```
    * Si no se proporciona un archivo `.env`, Docker Compose utilizará los valores por defecto especificados directamente en `compose.yml` (si los hay) o las variables de entorno ya existentes en tu sistema.

4.  **Construir las Imágenes y Levantar los Servicios**:
    * Navega a la raíz del proyecto (donde se encuentra el archivo `compose.yml`).
    * Ejecuta el siguiente comando en tu terminal:
        ```bash
        docker-compose up -d --build
        ```
        * `--build`: Fuerza la reconstrucción de las imágenes Docker si ha habido cambios en los `Dockerfile` o en el código fuente.
        * `-d`: Ejecuta los contenedores en segundo plano (modo "detached").

5.  **Verificación del Estado de los Servicios**:
    * Puedes ver los logs de todos los servicios con: `docker-compose logs -f`
    * Para ver los logs de un servicio específico: `docker-compose logs -f <nombre-del-servicio>` (ej. `docker-compose logs -f service-user`).
    * **Service Discovery (Eureka Dashboard)**: Abre tu navegador y ve a `http://localhost:${SERVICE_DISCOVERY_PORT}` (por defecto `http://localhost:8761`). Deberías ver los microservicios (`service-user`, `service-home`, etc.) registrándose.
    * **API Gateway (Swagger UI Agregada)**: Accede a `http://localhost:${SERVER_PORT_API_GATEWAY}/swagger-ui.html` (por defecto `http://localhost:8080/swagger-ui.html`). Esta interfaz te permitirá explorar y probar los endpoints de todos los microservicios que están expuestos a través del gateway.
    * **phpMyAdmin**: Disponible en `http://localhost:${PHPMYADMIN_PORT}` (por defecto `http://localhost:8086`).
        * **Servidor**: `mysql-db` (este es el nombre del servicio de MySQL en la red Docker).
        * **Usuario Root**: `root` / **Contraseña**: La que hayas configurado en `${MYSQL_ROOT_PASSWORD}`.
        * **Usuario de Aplicación**: `${MYSQL_USER}` (def. `365home_app`) / **Contraseña**: `${MYSQL_PASSWORD}`.

6.  **Acceso Directo a Microservicios (para depuración)**:
    Durante el desarrollo, puede ser útil acceder a los microservicios directamente (aunque en producción todo el tráfico debería pasar por el API Gateway):
    * `service-user`: `http://localhost:${SERVER_PORT_SERVICE_USER}` (def. `8081`)
    * `service-home`: `http://localhost:${SERVER_PORT_SERVICE_HOME}` (def. `8082`)
    * `service-visits`: `http://localhost:${SERVER_PORT_SERVICE_VISITS}` (def. `8083`)
    * `service-transactions`: `http://localhost:${SERVER_PORT_SERVICE_TRANSACTIONS}` (def. `8084`)
    * `service-config`: `http://localhost:8085`

7.  **Proceso de Inicialización de Datos**:
    * **Base de Datos**: El script `init-db.sql` se ejecuta automáticamente la primera vez que se inicia el contenedor `mysql-db`, creando los esquemas (`services_user`, `services_home`, etc.) y el usuario de la aplicación.
    * **Usuario Administrador por Defecto (`service-user`)**: El servicio de usuarios (`service-user`) tiene un componente `DataInitializer` que crea un usuario con rol de administrador (configurable vía variables `app.default-user.*`) si no existe uno con el email especificado. Esto es útil para tener un usuario inicial para pruebas.
    * **Datos Maestros (`service-home`)**: El servicio de propiedades (`service-home`) incluye un `DatabaseSeeder` que carga datos iniciales como departamentos, ciudades, categorías, etc., para facilitar el desarrollo y las pruebas.

8.  **Detener los Servicios**:
    Para detener todos los servicios, ejecuta:
    ```bash
    docker-compose down
    ```
    Si también quieres eliminar los volúmenes (¡cuidado, esto borrará los datos de la base de datos!):
    ```bash
    docker-compose down -v
    ```

## 📂 Estructura Detallada del Repositorio

El proyecto está organizado en una estructura de monorepo, donde cada microservicio principal reside en su propio directorio.

```bash
.
├── service-config/         # ⚙️ Servidor de Configuración (Spring Cloud Config)
│   ├── src/
│   ├── build.gradle
│   └── Dockerfile
├── service-discovery/      # 🧭 Servidor de Descubrimiento (Netflix Eureka)
│   ├── src/
│   ├── build.gradle
│   └── Dockerfile
├── service-gateway/        # 🚪 API Gateway (Spring Cloud Gateway MVC)
│   ├── src/
│   ├── build.gradle
│   └── Dockerfile
├── service-home/           # 🏠 Microservicio de Gestión de Propiedades
│   ├── src/
│   │   ├── main/java/com/pragma/home360/home/
│   │   │   ├── application/  # Lógica de aplicación (DTOs, Mappers, Servicios de App)
│   │   │   ├── domain/       # Lógica de Dominio (Modelos, Puertos, Casos de Uso)
│   │   │   └── infrastructure/ # Adaptadores de Infraestructura (Endpoints, Repositorios, Entidades)
│   │   └── resources/
│   ├── build.gradle
│   └── Dockerfile
├── service-transactions/   # 💸 Microservicio de Gestión de Transacciones
│   ├── src/
│   ├── build.gradle
│   └── Dockerfile
├── service-user/           # 👤 Microservicio de Gestión de Usuarios y Autenticación
│   ├── src/
│   │   ├── main/java/com/pragma/user360/
│   │   │   ├── application/
│   │   │   ├── configurations/ # Configuraciones de Beans, Seguridad
│   │   │   ├── domain/
│   │   │   └── infrastructure/
│   │   └── resources/
│   ├── build.gradle
│   └── Dockerfile
├── service-visits/         # 🚶 Microservicio de Gestión de Visitas
│   ├── src/
│   ├── build.gradle
│   └── Dockerfile
├── uploads/                # 🖼️ Directorio (en el host) para imágenes de propiedades (montado en service-home)
│   └── property-images/
├── compose.yml             # 🐳 Archivo principal de Docker Compose para orquestar todos los servicios
├── init-db.sql             # 📜 Script SQL para la inicialización de las bases de datos MySQL
└── README.md               # 📄 Este archivo de documentación
```

**Dentro de cada directorio de microservicio (ej. `service-home/`)**:

* `src/main/java/com/pragma/...(nombrepaquete)`: Contiene el código fuente Java del servicio, usualmente siguiendo una arquitectura hexagonal o por capas (application, domain, infrastructure).
* `src/main/resources/`:
    * `application.yml` o `application.yaml`: Archivo de configuración principal de Spring Boot para el servicio.
    * Otros recursos como scripts de migración de base de datos (si se usara Flyway/Liquibase), plantillas, etc.
* `build.gradle`: Define las dependencias y tareas de construcción para el módulo Gradle.
* `Dockerfile`: Especifica cómo construir la imagen Docker para ese microservicio.

## 🔑 Flujo de Seguridad y Autenticación (JWT)

1.  **Inicio de Sesión**: El usuario envía sus credenciales (email y contraseña) al endpoint `POST /api/v1/auth/sign-in` del `service-user` (a través del API Gateway).
2.  **Validación y Emisión de Token**: `service-user` valida las credenciales. Si son correctas, genera un Token JWT firmado. Este token incluye información del usuario (como ID, email y rol) y una fecha de expiración.
3.  **Envío del Token al Cliente**: El JWT se devuelve al cliente.
4.  **Peticiones Subsecuentes**: Para acceder a recursos protegidos, el cliente debe incluir el JWT en la cabecera `Authorization` de cada petición, con el prefijo `Bearer `:
    ```
    Authorization: Bearer <TU_TOKEN_JWT_AQUI>
    ```
5.  **Validación en API Gateway**: `service-gateway` intercepta la petición.
    * Valida la firma y la expiración del JWT.
    * Si el token es válido, el filtro `ClaimsToHeadersFilter` extrae el `userId` (del `subject` del JWT) y el `role` (de un claim personalizado).
    * Estas informaciones se añaden como cabeceras HTTP (`X-User-Id`, `X-User-Roles`) a la petición antes de reenviarla al microservicio correspondiente.
6.  **Procesamiento en Microservicios Internos**: Los microservicios internos reciben la petición con las cabeceras `X-User-Id` y `X-User-Roles`. Pueden usar esta información para lógica de negocio específica del usuario o para aplicar autorizaciones más granulares si es necesario, confiando en que el API Gateway ya ha realizado la autenticación.

## 📝 Notas Adicionales y Consideraciones

* **Variables de Entorno**: La configuración de la aplicación se maneja fuertemente mediante variables de entorno, lo cual es una buena práctica para la contenerización y diferentes entornos (desarrollo, staging, producción). Asegúrate de revisar y configurar adecuadamente el archivo `.env` o las variables de tu sistema.
* **Almacenamiento de Imágenes (`service-home`)**: Las imágenes de las propiedades se almacenan en el sistema de archivos del host, en el directorio `./uploads/property-images`, que está montado dentro del contenedor de `service-home` en `/app/uploads/property-images`. El servicio genera URLs relativas (ej. `/media/properties/<propertyId>/<imageName>`) que son resueltas por el API Gateway para servir los archivos.
* **Logs de Contenedores**: Para monitorizar o depurar, los logs de cada servicio son accesibles mediante Docker Compose: `docker-compose logs -f [nombre-del-servicio]`.
* **Escalabilidad**: La arquitectura de microservicios permite escalar cada servicio de forma independiente según la carga. En un entorno de producción, se utilizarían orquestadores como Kubernetes.
* **Pruebas**: Cada microservicio contiene pruebas unitarias y de integración (ej. `CategoryUseCaseTest.java` en `service-home`). Es fundamental mantener y ampliar la cobertura de pruebas.

---
