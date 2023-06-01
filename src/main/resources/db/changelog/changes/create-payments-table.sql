--liquibase formatted sql
--changeset <ilkogood>:<create-payments-table>
CREATE TABLE IF NOT EXISTS `payments` (
                            `rental_id` bigint NOT NULL,
                            `amount` decimal(38,2) DEFAULT NULL,
                            `session_id` varchar(255) DEFAULT NULL,
                            `session_url` varchar(255) DEFAULT NULL,
                            `status` enum('PAID','PENDING') DEFAULT NULL,
                            `type` enum('FINE','PAYMENT') DEFAULT NULL,
                            PRIMARY KEY (`rental_id`),
                            CONSTRAINT `rental_id_fk` FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--rollback DROP TABLE payments;
