CREATE SCHEMA IF NOT EXISTS `car_sharing_db` DEFAULT CHARACTER SET utf8;
USE `car_sharing_db`;

CREATE TABLE IF NOT EXISTS `cars`
(
    `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `model` VARCHAR(255) NOT NULL,
    `brand` VARCHAR(255) NOT NULL,
    `type` ENUM('SEDAN', 'SUV', 'HATCHBACK', 'UNIVERSAL') NOT NULL,
    `inventory` BIGINT(11),
    `daily_fee` DECIMAL NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `users`
(
    `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL,
    `first_name` VARCHAR(255),
    `last_name` VARCHAR(255),
    `password` VARCHAR(255) NOT NULL,
    `role` ENUM('MANAGER', 'COSTUMER') NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `rentals`
(
    `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `rental_date` DATE NOT NULL,
    `return_date` DATE NOT NULL,
    `actual_return_date` DATE NOT NULL,
    `car_id`,
    `user_id`,
    );

CREATE TABLE IF NOT EXISTS `payments`
(
    `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `status` ENUM('PENDING', 'PAID') NOT NULL,
    `type` ENUM('PAYMENT', 'FINE') NOT NULL,
    `rental_id` ,
    `session_url` ,
    `session_id` ,
    `amount` DECIMAL NOT NULL
    );