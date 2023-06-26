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
