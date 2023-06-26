--liquibase formatted sql
--changeset <ilkogood>:<create-payments-table>
create table if not exists payments
(
    amount      decimal(38, 2)           null,
    id          bigint auto_increment
    primary key,
    rental_id   bigint                   null,
    session_url varchar(500)             null,
    receipt_url varchar(255)             null,
    session_id  varchar(255)             null,
    status      enum ('PAID', 'PENDING') not null,
    type        enum ('FINE', 'PAYMENT') not null
    );

--rollback DROP TABLE payments;
