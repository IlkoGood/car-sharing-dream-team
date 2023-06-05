--liquibase formatted sql
--changeset <ilkogood>:<create-rentals-table>

CREATE TABLE IF NOT EXISTS `rentals` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `actual_return_date` DATETIME(6) DEFAULT NULL,
                           `rental_start_date` DATETIME(6) DEFAULT NULL,
                           `rental_return_date` DATETIME(6) DEFAULT NULL,
                           `car_id` BIGINT DEFAULT NULL,
                           `user_id` BIGINT DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `rental_car_fk` (`car_id`),
                           KEY `rental_user_fk` (`user_id`),
                           CONSTRAINT `rental_car_fk` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`),
                           CONSTRAINT `rental_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--rollback DROP TABLE rentals;
