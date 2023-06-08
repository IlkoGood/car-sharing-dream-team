--liquibase formatted sql
--changeset <ilkogood>:<create-payments-table>
CREATE TABLE IF NOT EXISTS `payments` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `rental_id` BIGINT NOT NULL,
                            `amount` DECIMAL(38,2) DEFAULT NULL,
                            `session_id` VARCHAR(255) DEFAULT NULL,
                            `session_url` VARCHAR(500) DEFAULT NULL,
                            `receipt_url` VARCHAR(255) DEFAULT NULL,
                            `status` ENUM('PAID','PENDING') DEFAULT NULL,
                            `type` ENUM('FINE','PAYMENT') DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--rollback DROP TABLE payments;
