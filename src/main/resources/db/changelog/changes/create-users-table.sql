--liquibase formatted sql
--changeset <ilkogood>:<create-users-table>
CREATE TABLE IF NOT EXISTS `users`(
                    `chat_id` bigint DEFAULT NULL,
                    `id` bigint NOT NULL AUTO_INCREMENT,
                    `email` varchar(255) DEFAULT NULL,
                    `first_name` varchar(255) DEFAULT NULL,
                    `last_name` varchar(255) DEFAULT NULL,
                    `password` varchar(255) DEFAULT NULL,
                    `role` enum('CUSTOMER','MANAGER') NOT NULL,
                    PRIMARY KEY (`id`),
                    UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--rollback DROP TABLE users;
