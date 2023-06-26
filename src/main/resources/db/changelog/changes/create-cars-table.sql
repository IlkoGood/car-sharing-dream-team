--liquibase formatted sql
--changeset <ilkogood>:<create-cars-table>
create table if not exists cars
(
    daily_fee decimal(38, 2)                                  not null,
    inventory int                                             not null,
    id        bigint auto_increment
    primary key,
    brand     varchar(255)                                    not null,
    model     varchar(255)                                    not null,
    type      enum ('HATCHBACK', 'SEDAN', 'SUV', 'UNIVERSAL') not null
    );

--rollback DROP TABLE cars;
