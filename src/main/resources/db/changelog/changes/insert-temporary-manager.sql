--liquibase formatted sql
--changeset <ilkogood>:<insert into-users-table>
INSERT INTO `car-sharing`.`users`
    (`id`, `email`, `first_name`, `last_name`, `password`, `role`)
    VALUES ('1', 'admin@gmail.com', 'DeleteThis', 'Account',
            '$2a$10$/vMmroQOqcpHb0qCgqwsJ.JMiXyG5wqCLSAzvUDeqhGfughP1QZIa', 'MANAGER');
--rollback DROP TABLE cars;