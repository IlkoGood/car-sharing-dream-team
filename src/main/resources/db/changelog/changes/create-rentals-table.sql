--liquibase formatted sql
--changeset <ilkogood>:<create-rentals-table>

create table if not exists rentals
(
    actual_return_date datetime(6) null,
    car_id             bigint      null,
    id                 bigint auto_increment
    primary key,
    rental_return_date datetime(6) null,
    rental_start_date  datetime(6) null,
    user_id            bigint      null
    );

--rollback DROP TABLE rentals;
