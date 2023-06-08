--liquibase formatted sql
--changeset <ilkogood>:<create-rentals-table>

CREATE TABLE IF NOT EXISTS `rentals` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `actual_return_date` DATETIME(6) DEFAULT NULL,
                           `rental_start_date` DATETIME(6) DEFAULT NULL,
                           `rental_return_date` DATETIME(6) DEFAULT NULL,
                           `car_id` BIGINT DEFAULT NULL,
                           `user_id` BIGINT DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--rollback DROP TABLE rentals;
