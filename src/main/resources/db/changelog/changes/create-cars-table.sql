--liquibase formatted sql
--changeset <ilkogood>:<create-cars-table>
CREATE TABLE IF NOT EXISTS `cars` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `model` varchar(255) DEFAULT NULL,
                        `brand` varchar(255) DEFAULT NULL,
                        `type` ENUM('HATCHBACK','SEDAN','SUV','UNIVERSAL') NOT NULL,
                        `inventory` int NOT NULL,
                        `daily_fee` decimal(38,2) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--rollback DROP TABLE cars;
