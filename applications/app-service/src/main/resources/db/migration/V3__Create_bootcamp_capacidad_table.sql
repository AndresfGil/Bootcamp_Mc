

CREATE TABLE IF NOT EXISTS `bootcamp_capacidad` (
    `bootcamp_id` BIGINT NOT NULL,
    `capacidad_id` BIGINT NOT NULL,
    PRIMARY KEY (`bootcamp_id`, `capacidad_id`),
    FOREIGN KEY (`bootcamp_id`) REFERENCES `bootcamp`(`id`) ON DELETE CASCADE,
    INDEX `idx_bootcamp_id` (`bootcamp_id`),
    INDEX `idx_capacidad_id` (`capacidad_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

