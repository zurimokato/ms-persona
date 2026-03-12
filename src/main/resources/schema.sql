CREATE TABLE IF NOT EXISTS persona (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email  VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS bootcamp_persona (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_persona   BIGINT NOT NULL,
    id_bootcamp  BIGINT NOT NULL,
    CONSTRAINT fk_bp_persona FOREIGN KEY (id_persona) REFERENCES persona(id),
    CONSTRAINT uk_bp UNIQUE (id_persona, id_bootcamp)
);
