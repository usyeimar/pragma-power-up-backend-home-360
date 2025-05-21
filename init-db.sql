CREATE DATABASE IF NOT EXISTS services_user CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS services_home CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS services_visits CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS services_transactions CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


GRANT ALL PRIVILEGES ON `services_user`.* TO '365home_app'@'%';
GRANT ALL PRIVILEGES ON `services_home`.* TO '365home_app'@'%';
GRANT ALL PRIVILEGES ON `services_visits`.* TO '365home_app'@'%';
GRANT ALL PRIVILEGES ON `services_transactions`.* TO '365home_app'@'%';
FLUSH PRIVILEGES;
