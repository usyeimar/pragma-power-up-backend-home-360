SELECT 'CREATE DATABASE services_user'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'services_user')\gexec

-- Crear la base de datos para service-home si no existe
SELECT 'CREATE DATABASE services_home'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'services_home')\gexec

-- Crear la base de datos para service-visits si no existe
SELECT 'CREATE DATABASE services_visits'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'services_visits')\gexec

-- Crear la base de datos para service-transactions si no existe
SELECT 'CREATE DATABASE services_transactions'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'services_transactions')\gexec
