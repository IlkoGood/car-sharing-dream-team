<img src="https://i2.paste.pics/OMYLR.png">
<h1 style="color: gold; align-self: auto">Car Sharing Service</h1>

Car Sharing Service is a web application that provides a comprehensive solution for car rental needs. Users can easily register and log in using their credentials they can easily browse and book available cars based on their preferences. Role-based authorization is implemented, allowing administrators and regular users to access different functionalities. To ensure the security of transactions, our service integrates with the reliable Stripe payment system. Users can make payments conveniently and securely using Stripe, this integration guarantees a smooth and trustworthy payment process. Furthermore, we have implemented a notification system through a Telegram bot. Users can stay updated on their rental status, receive payment reminders, and be notified of any changes in the condition of the cars they have rented. This feature enhances communication and provides users with real-time information. Experience the convenience, reliability, and security offered by the Car Sharing Service.

# Features

- Inventory Management: Easily manage the inventory of available cars for car sharing.
- Rental Management: Efficiently handle the process of car rentals, including booking, pickup, and return.
- Customer Management: Keep track of customer information and manage customer accounts.
- Notification System: Provide real-time notifications to users regarding rental updates, payment reminders, and changes in car availability.
- Payment Handling: Enable secure and convenient payment processing for car rentals.

# Project structure

- `api.stripe` : contains the integration with the Stripe payment system for handling payment-related operations in the car sharing ser vice.
- `telegrambot` : contains settings related to the integration with Telegram bot.
- `config` : contains configuration files for various aspects of the application.
- `controller` :
    - Authentication :
        - `POST`: `/register` (register a new user)
        - `POST`: `/login` (authenticates a user and  get JWT tokens)
    - Car :
        - `POST`: `/cars` (add a new car)
        - `GET`: `/cars/{id}` (get the car information by id)
        - `PUT`: `/cars/{id}` (update car information by id)
        - `DELETE`: `/cars/{id}` (delete car by id)
        - `GET`: `/cars` (display all available cars)
    - Payment :
        - `POST`: `/payments` (create payment session)
        - `GET`: `/payments` (get user's payments)
        - `GET`: `/payments/success/{id}` (check successful Stripe payments)
        - `GET`: `/payments/cancel/{id}` (return payment paused message)
    - Rental :
        - `POST`: `/rentals` (add a new rental)
        - `GET`: `/rentals` (get rentals by user ID and whether the rental is still active or not)
        - `GET`: `/rentals/{id}` (get specific rental by id)
        - `PUT`: `/rentals/{id}/return` (set actual return date)
    - User :
        - `GET`: `/users/me` (get my profile info)
        - `PATCH`: `/users/me` (update profile info)
        - `PUT`: `/users/{id}/role` (update user role)
- `dto` : to transfer data between different layers or components of an application.
- `exception` : contains custom exception classes for handling errors within the project.
- `model` : stores information about entities and their properties.
- `repository` : responsible for providing a way to perform create, read, update and delete (CRUD) operations on the data.
- `security` : contains security settings and configurations for controlling access to the application based on roles.
- `service` : interfaces and their implementations are responsible for the business logic of the application.
- `resources` : configuration file.

# Technologies
- Java `17`
- Spring Boot `3.1.0`
- Springdoc OpenAPI (Swagger) `2.1.0`
- Stripe API `22.21.0`
- MySQL `8.0.32`
- jjwt `0.11.5`
- Telegram Bots `5.3.0`
- Docker `24.0.2`
- Liquibase
- Lombok
- Maven `3.8.7`


# How to run
`REMARK! When starting the application, ADMIN already injected into the database, use it:`

| Role    | email           | password   |
|---------|-----------------|------------|
| MANAGER | admin@gmail.com | admin12345 |

1. Install [Docker](https://www.docker.com/) on your machine
2. Clone [this](https://github.com/NickolayVoloshchuk/car-sharing-dream-team.git) remote repository to your local repository
3. Register your telegram bot
    _How to get a Telegram username and token_
    1. Go to the dialog with the `BotFather bot` in the telegram.
    2. Press `/start` and select the `/newbot` command.
    3. Give your bot a name and a username (a unique name that ends in "_bot").
    4. You will receive a token.
    5. In a `.env` file set the necessary environment variables.
4. In the `src/main/resources/liquibase.properties` and `.env.excemple` set your credentials
5. Rename `.env.excemple` to `.env`
6. Build The application by running the Maven package command: `mvn clean package`
7. Run the command : 
```bash
   docker-compose up --build
```
8. Use `/start` into your telegram bot chat
9. Write `admin@gmail.com` OR your custom user into your telegram bot chat

Use [our Postman collection](https://elements.getpostman.com/redirect?entityId=27159960-d602fee2-a12d-465e-80f5-ca6cb0edad54&entityType=collection) for easy communication with application

OR

You can test the application using [swagger](http://localhost:7777/swagger.html)

# Authors

[Illia Kovtun](https://github.com/IlkoGood)

[Nadiia Huryk](https://github.com/NadiaHuryk)

[Oleksandr Krasnov](https://github.com/redmanO-o)

[Mykola Voloshchuk](https://github.com/NickolayVoloshchuk)