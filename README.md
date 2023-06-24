# Car Sharing Service
![](C:\Users\nadia\Desktop\b9f4062c-3831-4f1a-bf1a-3a79b8042257.jfif)

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
        - POST: /register (register a new user)
        - POST: /login (authenticates a user and  get JWT tokens)
    - Car :
        - POST: /cars (add a new car)
        - GET: /cars/{id} (get the car information by id)
        - PUT: /cars/{id} (update car information by id)
        - DELETE: /cars/{id} (delete car by id)
        - GET: /cars (display all available cars)
    - Payment :
        - POST: /payments (create payment session)
        - GET: /payments (get user's payments)
        - GET: /payments/success{id} (check successful Stripe payments)
        - GET: /payments/cancel{id} (return payment paused message)
    - Rental :
        - POST: /rentals (add a new rental)
        - GET: /rentals (get rentals by user ID and whether the rental is still active or not)
        - GET: /rentals/{id} (get specific rental by id)
        - PUT: /rentals/{id}/return (set actual return date)
    - User :
        - GET: /users/me (get my profile info)
        - PATCH: /users/me (update profile info)
        - PUT: /users/{id}/role (update user role)
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
1. Install Docker on your machine.
2. Clone this remote repository to your local repository.
3. In the resources/application.properties set your credentials
4. Create a .env file in the root directory of your project and set it with the necessary environment variables.
5. Build your application by running the Maven package command: mvn package.
6. Run the command : docker-compose up.

You can test the application using swagger using the address http://localhost:8080/swagger.html
