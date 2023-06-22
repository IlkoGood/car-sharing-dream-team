INSERT INTO users (chat_id, id, email, first_name, last_name, password, role)
VALUES (50, 50, 'admin50@gmail.com', 'Name', 'Surname', 'password', 'MANAGER');

INSERT INTO cars (id, model, brand, type, inventory, daily_fee)
VALUES (50, 'X8', 'BMW', 'UNIVERSAL', 5, 30);

INSERT INTO rentals (id, actual_return_date, rental_start_date, rental_return_date, car_id, user_id)
VALUES (50, '2023-06-21 23:57:06.980142', '2023-06-18 21:00:00.000000', '2023-06-19 20:59:59.000000', 50, 50);

INSERT INTO payments (id, rental_id, amount, session_id, session_url, receipt_url, status, type)
VALUES (50, 50, 22.22, 'sessionIdExcemple#', 'http://session-url.excemple', null, 'PENDING', 'PAYMENT');
