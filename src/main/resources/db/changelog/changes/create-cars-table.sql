--liquibase formatted sql
--changeset <ilkogood>:<create-cars-table>
CREATE TABLE IF NOT EXISTS `cars` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `model` VARCHAR(255) NOT NULL,
                        `brand` VARCHAR(255) NOT NULL,
                        `type` ENUM('HATCHBACK','SEDAN','SUV','UNIVERSAL') NOT NULL,
                        `inventory` INT NOT NULL,
                        `daily_fee` DECIMAL(38,2) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--rollback DROP TABLE cars;
