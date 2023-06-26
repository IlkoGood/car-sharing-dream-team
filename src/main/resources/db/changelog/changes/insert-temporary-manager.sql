--liquibase formatted sql
--changeset <ilkogood>:<insert into-users-table>
insert into users (chat_id, id, email, first_name, last_name, password, role)
values ('1', '1', 'admin@gmail.com', 'DeleteThis', 'Account',
        '$2a$10$/vMmroQOqcpHb0qCgqwsJ.JMiXyG5wqCLSAzvUDeqhGfughP1QZIa', 'MANAGER');
--rollback DROP TABLE cars;