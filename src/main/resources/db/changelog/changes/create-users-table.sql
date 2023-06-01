--liquibase formatted sql
--changeset <ilkogood>:<create-users-table>
CREATE TABLE IF NOT EXISTS users
(
    id bigint auto_increment,
    email varchar(255) not null,
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255) not null,
    role varchar(255) not null,
    chat_id BIGINT,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

--rollback DROP TABLE users;
