package com.carsharing.util;

import com.carsharing.dto.request.CarRequestDto;
import com.carsharing.dto.request.PaymentRequestDto;
import com.carsharing.dto.request.RentalRequestDto;
import com.carsharing.dto.request.UserLoginDto;
import com.carsharing.dto.request.UserRegistrationDto;
import com.carsharing.dto.response.CarResponseDto;
import com.carsharing.dto.response.PaymentResponseDto;
import com.carsharing.dto.response.RentalResponseDto;
import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.stripe.model.Charge;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UtilModelObjects {
    private final User defaultUser = getUser();
    private final User defaultCustomer = getCustomer();
    private final Car defaultCar = getCar();
    private final Rental defaultRental = getRental();
    private final Payment defaultPayment = getPayment();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    protected User getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("encrypt(12345user)");
        user.setRole(User.Role.MANAGER);
        user.setChatId(1L);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        return user;
    }

    protected Car getCar() {
        Car car = new Car();
        car.setId(1L);
        car.setType(Car.Type.UNIVERSAL);
        car.setBrand("BMW");
        car.setModel("X8");
        car.setInventory(5);
        car.setDailyFee(BigDecimal.TEN);
        return car;
    }

    protected Rental getRental() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setRentalDate(LocalDateTime.parse("2023-06-20T00:00:00"));
        rental.setReturnDate(LocalDateTime.parse("2023-06-20T23:59:59"));
        rental.setActualReturnDate(LocalDateTime.parse("2023-06-21T12:00:00"));
        rental.setCarId(1L);
        rental.setUserId(1L);
        return rental;
    }

    protected Payment getPayment() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setType(Payment.Type.PAYMENT);
        payment.setStatus(Payment.Status.PENDING);
        payment.setRentalId(1L);
        payment.setAmount(BigDecimal.valueOf(22.0));
        payment.setSessionUrl("http://session-url.excemple");
        payment.setSessionId("sessionIdExcemple#");
        return payment;
    }

    protected List<User> getUsers(int count) {
        List<User> admins = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            User admin = getUser();
            admin.setId(i);
            admin.setChatId(i);
            admin.setEmail(i + admin.getEmail());
            admins.add(admin);
        }
        return admins;
    }

    protected List<Car> getCars(int count) {
        List<Car> cars = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Car car = getCar();
            car.setId(i);
            cars.add(car);
        }
        return cars;
    }

    protected List<Rental> getRentals(int count) {
        List<Rental> rentals = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Rental rental = getRental();
            rental.setId(i);
            rentals.add(rental);
        }
        return rentals;
    }
    protected List<Rental> getRentals(boolean isActive, int count) {
        List<Rental> rentals = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Rental rental = getRental();
            rental.setId(i);
            if (isActive) {
                rental.setActualReturnDate(null);
            }
            rentals.add(rental);
        }
        return rentals;
    }

    protected List<Payment> getPayments(int count) {
        List<Payment> payments = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Payment payment = getPayment();
            payment.setId(i);
            payment.setRentalId(i);
            payment.setAmount(BigDecimal.valueOf(i * 20));
            payment.setSessionId(payment.getSessionId() + i);
            payments.add(payment);
        }
        return payments;
    }

    protected User getCustomer() {
        User customer = getUser();
        customer.setId(2L);
        customer.setEmail("customer@gmail.com");
        customer.setRole(User.Role.CUSTOMER);
        customer.setChatId(2L);
        return customer;
    }

    protected User getUpdatedUser() {
        User updatedUser = new User();
        updatedUser.setId(defaultUser.getId());
        updatedUser.setEmail(defaultUser.getEmail());
        updatedUser.setPassword(defaultUser.getPassword());
        updatedUser.setRole(defaultUser.getRole());
        updatedUser.setFirstName("FirstName");
        updatedUser.setLastName("LastName");
        return updatedUser;
    }

    protected User getDefaultUser() {
        User user = new User();
        user.setId(21L);
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(User.Role.MANAGER);
        return user;
    }

    protected Session getSession() {
        Session session = new Session();
        session.setId("sessionIdExcemple#");
        session.setUrl("http://session-url.excemple");
        return session;
    }

    protected String getReceipt() {
        return "http://receipt-url.excemple";
    }

    protected Charge getCharge() {
        Charge charge = new Charge();
        charge.setId("chargeIdExcemple");
        charge.setReceiptUrl(getReceipt());
        return charge;
    }

    protected RentalRequestDto getRentalRequestDto() {
        RentalRequestDto dto = new RentalRequestDto();
        dto.setRentalDate(defaultRental.getRentalDate().format(formatter));
        dto.setReturnDate(defaultRental.getReturnDate().format(formatter));
        dto.setActualReturnDate(defaultRental.getActualReturnDate());
        dto.setUserId(defaultRental.getUserId().toString());
        dto.setCarId(defaultRental.getCarId().toString());
        return dto;
    }

    protected RentalResponseDto getRentalResponseDto() {
        RentalResponseDto dto = new RentalResponseDto();
        dto.setId(defaultRental.getId());
        dto.setRentalDate(defaultRental.getRentalDate());
        dto.setReturnDate(defaultRental.getReturnDate());
        dto.setActualReturnDate(defaultRental.getActualReturnDate());
        dto.setUserId(defaultRental.getUserId());
        dto.setCarId(defaultRental.getCarId());
        return dto;
    }

    protected PaymentResponseDto getPaymentResponseDto() {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(defaultPayment.getId());
        dto.setType(defaultPayment.getType());
        dto.setStatus(defaultPayment.getStatus());
        dto.setRental(defaultPayment.getRentalId());
        dto.setAmount(defaultPayment.getAmount());
        dto.setSessionUrl(defaultPayment.getSessionUrl());
        dto.setSessionId(defaultPayment.getSessionId());
        return dto;
    }

    protected PaymentRequestDto getPaymentRequestDto() {
        PaymentRequestDto dto = new PaymentRequestDto();
        dto.setRentalId(defaultPayment.getRentalId().toString());
        dto.setType(defaultPayment.getType().name());
        return dto;
    }

    protected CarRequestDto getCarRequestDto() {
        CarRequestDto dto = new CarRequestDto();
        dto.setBrand(defaultCar.getBrand());
        dto.setModel(defaultCar.getModel());
        dto.setType(defaultCar.getType().name());
        dto.setInventory(String.valueOf(defaultCar.getInventory()));
        dto.setDailyFee(defaultCar.getDailyFee());
        return dto;
    }

    protected CarResponseDto getCarResponseDto() {
        CarResponseDto dto = new CarResponseDto();
        dto.setId(defaultCar.getId());
        dto.setBrand(defaultCar.getBrand());
        dto.setModel(defaultCar.getModel());
        dto.setType(defaultCar.getType());
        dto.setInventory(defaultCar.getInventory());
        dto.setDailyFee(defaultCar.getDailyFee());
        return dto;
    }

    protected UserRegistrationDto getRegistrationDto() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail(defaultCustomer.getEmail());
        dto.setPassword(defaultCustomer.getPassword());
        dto.setRepeatPassword(defaultCustomer.getPassword());
        return dto;
    }

    protected UserLoginDto getLoginDto() {
        UserLoginDto dto = new UserLoginDto();
        dto.setEmail(defaultCustomer.getEmail());
        dto.setPassword(defaultCustomer.getPassword());
        return dto;
    }
}
