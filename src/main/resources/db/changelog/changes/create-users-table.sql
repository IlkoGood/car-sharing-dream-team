--liquibase formatted sql
--changeset <ilkogood>:<create-users-table>
CREATE TABLE IF NOT EXISTS `users`(
                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                    `email` VARCHAR(255) NOT NULL,
                    `first_name` VARCHAR(255) DEFAULT NULL,
                    `last_name` VARCHAR(255) DEFAULT NULL,
                    `password` varchar(255) NOT NULL,
                    `role` ENUM('MANAGER', 'COSTUMER') NOT NULL,
                    'chat_id' BIGINT DEFAULT NULL,
                    PRIMARY KEY (`id`),
                    UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--rollback DROP TABLE users;
