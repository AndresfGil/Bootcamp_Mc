

CREATE TABLE IF NOT EXISTS `bootcamp_tecnologia` (
    `bootcamp_id` BIGINT NOT NULL,
    `tecnologia_id` BIGINT NOT NULL,
    PRIMARY KEY (`bootcamp_id`, `tecnologia_id`),
    FOREIGN KEY (`bootcamp_id`) REFERENCES `bootcamp`(`id`) ON DELETE CASCADE,
    INDEX `idx_bootcamp_id` (`bootcamp_id`),
    INDEX `idx_tecnologia_id` (`tecnologia_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

