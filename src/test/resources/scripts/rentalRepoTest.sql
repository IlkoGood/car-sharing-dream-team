INSERT INTO `cars` (`id`, `model`, `brand`, `type`, `inventory`, `daily_fee`)
VALUES ('1', 'camry', 'toyota', 'SEDAN', '5', '20.00');

INSERT INTO `users` (`id`, `email`, `password`, `role`)
VALUES ('2', 'customer@gmail.com', '$2a$10$/vMmroQOqcpHb0qCgqwsJ.JMiXyG5wqCLSAzvUDeqhGfughP1QZIa', 'CUSTOMER');

INSERT INTO `rentals` (`id`, `rental_start_date`, `rental_return_date`, `car_id`, `user_id`)
VALUES ('1', '2023-05-31 21:00:00.000000', '2023-06-02 20:59:59.000000', '1', '1');