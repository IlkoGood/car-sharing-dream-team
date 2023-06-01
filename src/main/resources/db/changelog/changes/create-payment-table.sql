--liquibase formatted sql
--changeset <nk.voloshchuk>:<create-users-table>
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    status ENUM('PENDING', 'PAID') NOT NULL,
    type ENUM('PAYMENT', 'FINE') NOT NULL,
    rental_id BIGINT NOT NULL,
    session_url LONGTEXT,
    session_id BIGINT NOT NULL,
    amount DECIMAL NOT NULL
);

--rollback DROP TABLE users;