--liquibase formatted sql
--changeset <ilkogood>:<create-rentals-table>

CREATE TABLE IF NOT EXISTS `rentals` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `actual_return_date` datetime(6) DEFAULT NULL,
                           `rental_start_date` datetime(6) DEFAULT NULL,
                           `rental_return_date` datetime(6) DEFAULT NULL,
                           `car_id` bigint DEFAULT NULL,
                           `user_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `car_id_fk` (`car_id`),
                           KEY `user_id_fk` (`user_id`),
                           CONSTRAINT `car_id_fk` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`),
                           CONSTRAINT `user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--rollback DROP TABLE rentals;
