--liquibase formatted sql
--changeset <ilkogood>:<create-cars-table>
CREATE TABLE IF NOT EXISTS `cars` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `model` VARCHAR(255) NOT NULL,
                        `brand` VARCHAR(255) NOT NULL,
                        `type` ENUM('HATCHBACK','SEDAN','SUV','UNIVERSAL') NOT NULL,
                        `inventory` INT NOT NULL,
                        `daily_fee` DECIMAL(38,2) NOT NULL,
                        PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8mb4;

--rollback DROP TABLE cars;
