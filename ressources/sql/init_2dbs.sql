-- Supprime la base de données si elle existe déjà
DROP DATABASE IF EXISTS prod_db;

-- Crée la base de données
CREATE DATABASE prod_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Crée un nouvel utilisateur 'user_prod_db' avec un mot de passe 'password_prod_db'
CREATE USER IF NOT EXISTS 'user_prod_db'@'%' IDENTIFIED BY 'password_prod_db';

-- Accorde tous les privilèges sur la base de données 'prod_db' à l'utilisateur 'user_prod_db'
GRANT ALL PRIVILEGES ON prod_db.* TO 'user_prod_db'@'%';

-- Applique immédiatement les privilèges
FLUSH PRIVILEGES;

-- Supprime la base de données si elle existe déjà
DROP DATABASE IF EXISTS test_db;

-- Crée la base de données
CREATE DATABASE test_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Crée un nouvel utilisateur 'user_test_db' avec un mot de passe 'password_test_db'
CREATE USER IF NOT EXISTS 'user_test_db'@'%' IDENTIFIED BY 'password_test_db';

-- Accorde tous les privilèges sur la base de données 'test_db' à l'utilisateur 'user_test_db'
GRANT ALL PRIVILEGES ON test_db.* TO 'user_test_db'@'%';
