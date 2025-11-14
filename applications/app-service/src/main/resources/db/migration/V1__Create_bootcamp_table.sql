

CREATE TABLE IF NOT EXISTS `bootcamp` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(100) NOT NULL,
    `descripcion` VARCHAR(500) NOT NULL,
    `fecha_lanzamiento` DATE NOT NULL,
    `duracion` INT NOT NULL,
    `capacidades_ids` JSON NOT NULL,
    INDEX `idx_nombre` (`nombre`),
    INDEX `idx_fecha_lanzamiento` (`fecha_lanzamiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

