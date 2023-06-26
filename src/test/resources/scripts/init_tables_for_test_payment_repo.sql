create table if not exists  cars
(
    daily_fee decimal(38, 2)                                  not null,
    inventory int                                             not null,
    id        bigint auto_increment
    primary key,
    brand     varchar(255)                                    not null,
    model     varchar(255)                                    not null,
    type      enum ('HATCHBACK', 'SEDAN', 'SUV', 'UNIVERSAL') not null
    );

create table if not exists users
(
    chat_id    bigint                       null,
    id         bigint auto_increment
    primary key,
    email      varchar(255)                 not null,
    first_name varchar(255)                 null,
    last_name  varchar(255)                 null,
    password   varchar(255)                 not null,
    role       enum ('CUSTOMER', 'MANAGER') not null,
    constraint email_UNIQUE
    unique (email)
    );

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
