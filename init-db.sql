-- init-dbs.sql
-- Este script se ejecutará automáticamente por el contenedor de PostgreSQL al iniciarse por primera vez.

-- Crear la base de datos para service-user si no existe
SELECT 'CREATE DATABASE user_service_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'user_service_db')\gexec
GRANT ALL PRIVILEGES ON DATABASE user_service_db TO db_app_user;

-- Crear la base de datos para service-home si no existe
SELECT 'CREATE DATABASE home_service_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'home_service_db')\gexec
GRANT ALL PRIVILEGES ON DATABASE home_service_db TO db_app_user;

-- Crear la base de datos para service-visits si no existe
SELECT 'CREATE DATABASE visits_service_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'visits_service_db')\gexec
GRANT ALL PRIVILEGES ON DATABASE visits_service_db TO db_app_user;

-- Crear la base de datos para service-transactions si no existe
SELECT 'CREATE DATABASE transactions_service_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'transactions_service_db')\gexec
GRANT ALL PRIVILEGES ON DATABASE transactions_service_db TO db_app_user;

-- Nota: \gexec es una meta-instrucción de psql que ejecuta la consulta anterior.
-- El usuario 'db_app_user' es el que definiste en tu compose.yml (POSTGRES_USER).
-- Este script asume que el usuario 'db_app_user' tiene permisos para crear bases de datos,
-- lo cual es el comportamiento por defecto si POSTGRES_PASSWORD se establece al crear el contenedor.
