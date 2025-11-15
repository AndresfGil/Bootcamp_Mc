-- Migración Flyway V1: Crear todas las tablas del sistema de bootcamps
-- Base de datos: bootcamp

-- Tabla principal de bootcamps
CREATE TABLE IF NOT EXISTS `bootcamp` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(100) NOT NULL,
    `descripcion` VARCHAR(500) NOT NULL,
    `fecha_lanzamiento` DATE NOT NULL,
    `duracion` INT NOT NULL,
    `capacidades_ids` JSON NOT NULL,
    `tecnologias_ids` JSON NULL,
    INDEX `idx_nombre` (`nombre`),
    INDEX `idx_fecha_lanzamiento` (`fecha_lanzamiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla intermedia para relación muchos a muchos entre bootcamp y capacidades
CREATE TABLE IF NOT EXISTS `bootcamp_capacidad` (
    `bootcamp_id` BIGINT NOT NULL,
    `capacidad_id` BIGINT NOT NULL,
    PRIMARY KEY (`bootcamp_id`, `capacidad_id`),
    FOREIGN KEY (`bootcamp_id`) REFERENCES `bootcamp`(`id`) ON DELETE CASCADE,
    INDEX `idx_bootcamp_id` (`bootcamp_id`),
    INDEX `idx_capacidad_id` (`capacidad_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla intermedia para relación muchos a muchos entre bootcamp y tecnologías
CREATE TABLE IF NOT EXISTS `bootcamp_tecnologia` (
    `bootcamp_id` BIGINT NOT NULL,
    `tecnologia_id` BIGINT NOT NULL,
    PRIMARY KEY (`bootcamp_id`, `tecnologia_id`),
    FOREIGN KEY (`bootcamp_id`) REFERENCES `bootcamp`(`id`) ON DELETE CASCADE,
    INDEX `idx_bootcamp_id` (`bootcamp_id`),
    INDEX `idx_tecnologia_id` (`tecnologia_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de inscripciones
CREATE TABLE IF NOT EXISTS `inscripcion` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `persona_id` BIGINT NOT NULL,
    `bootcamp_id` BIGINT NOT NULL,
    `fecha_inscripcion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`bootcamp_id`) REFERENCES `bootcamp`(`id`) ON DELETE CASCADE,
    INDEX `idx_persona_id` (`persona_id`),
    INDEX `idx_bootcamp_id` (`bootcamp_id`),
    INDEX `idx_fecha_inscripcion` (`fecha_inscripcion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
