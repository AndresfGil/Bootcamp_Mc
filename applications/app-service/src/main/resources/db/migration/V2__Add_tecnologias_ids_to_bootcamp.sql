-- Migración Flyway V2: Agregar columna tecnologias_ids a bootcamp
-- Base de datos: bootcamp

ALTER TABLE `bootcamp` 
ADD COLUMN `tecnologias_ids` JSON NULL AFTER `capacidades_ids`;

