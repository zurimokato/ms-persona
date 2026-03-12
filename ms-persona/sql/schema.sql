CREATE DATABASE IF NOT EXISTS db_persona;
USE db_persona;

CREATE TABLE IF NOT EXISTS persona (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email  VARCHAR(150) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS bootcamp_persona (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_persona   BIGINT NOT NULL,
    id_bootcamp  BIGINT NOT NULL,
    CONSTRAINT fk_bp_persona FOREIGN KEY (id_persona) REFERENCES persona(id),
    CONSTRAINT uk_bp UNIQUE (id_persona, id_bootcamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
