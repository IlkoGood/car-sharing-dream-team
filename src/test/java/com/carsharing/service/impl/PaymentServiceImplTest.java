package com.carsharing.service.impl;

import com.carsharing.api.stripe.impl.PaymentProviderImpl;
import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.carsharing.repository.PaymentRepository;
import com.carsharing.service.UserService;
import com.stripe.model.Charge;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentProviderImpl paymentProvider;
    @Mock
    private UserService userService;

    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_Ok() {
        Payment expected = getPayment();
        Mockito.when(paymentRepository.save(expected)).thenReturn(expected);
        Payment actual = paymentService.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getById_Ok() {
        long id = 1L;
        Payment expected = getPayment();
        Mockito.when(paymentRepository.findById(id)).thenReturn(Optional.of(expected));
        Payment actual = paymentService.getById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getById_nonExisting_throwsException() {
        long id = 1L;
        Mockito.when(paymentRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> paymentService.getById(id));
    }

    @Test
    void getByUserId_ForCustomer_Ok() {
        long userId = 1L;
        List<Payment> expected = getPayments(3);
        Mockito.when(paymentRepository.findPaymentsByUserId(userId)).thenReturn(expected);
        List<Payment> actual = paymentService.getByUserId(userId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByUserId_ForManager_Ok() {
        Long userId = null;
        List<Payment> expected = getPayments(3);
        Mockito.when(paymentRepository.findAll()).thenReturn(expected);
        List<Payment> actual = paymentService.getByUserId(userId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createPaymentSession_Ok() {
        Payment expected = getPayment();
        Payment payment = getPayment();
        payment.setSessionId(null);
        payment.setSessionUrl(null);
        Rental rental = getRental();
        Car car = getCar();
        User user = getUser();
        Mockito.when(paymentRepository.findPaymentByRentalId(rental.getId())).thenReturn(Optional.empty());
        Mockito.when(paymentRepository.save(payment)).thenReturn(payment);
        Mockito.when(userService.findById(rental.getId())).thenReturn(user);
        Mockito.when(paymentProvider.createPaymentSession(
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(12.0),
                payment,
                rental,
                car,
                user
        )).thenReturn(getSession());
        Payment actual = paymentService.createPaymentSession(payment, rental, car);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createPaymentSession_paymentExistForRental_throwsException() {
        Rental rental = getRental();
        Mockito.when(paymentRepository.findPaymentByRentalId(rental.getId()))
                .thenReturn(Optional.of(getPayment()));
        Assertions.assertThrows(RuntimeException.class,
                () -> paymentService.createPaymentSession(new Payment(), rental, new Car()));
    }

    @Test
    void createPaymentSession_rentalIsStillActive_throwsException() {
        Rental rental = getRental();
        rental.setActualReturnDate(null);
        Assertions.assertThrows(RuntimeException.class,
                () -> paymentService.createPaymentSession(new Payment(), rental, new Car()));
    }

    @Test
    void getReceiptUrl_Ok() {
        String expected = getReceipt();
        Session session = getSession();
        Mockito.when(paymentProvider.getCharge(session.getId())).thenReturn(getCharge());
        String actual = paymentService.getReceiptUrl(session.getId());
        Assertions.assertEquals(expected, actual);
    }

    private Payment getPayment() {
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

    private List<Payment> getPayments( int count) {
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

    private Rental getRental() {
        String date = "19-06-2023 12:34:56";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setRentalDate(LocalDateTime.parse(date, formatter));
        rental.setReturnDate(LocalDateTime.parse(date, formatter));
        rental.setActualReturnDate(LocalDateTime.parse(date, formatter).plusDays(1));
        rental.setUserId(1L);
        rental.setCarId(1L);
        return rental;
    }

    private Car getCar() {
        Car car = new Car();
        car.setId(1L);
        car.setBrand("BMW");
        car.setModel("X8");
        car.setInventory(5);
        car.setType(Car.Type.HATCHBACK);
        car.setDailyFee(BigDecimal.TEN);
        return car;
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("encryptOf(123456test)");
        user.setRole(User.Role.MANAGER);
        user.setChatId(1L);
        return user;
    }

    private Session getSession() {
        Session session = new Session();
        session.setId("sessionIdExcemple#");
        session.setUrl("http://session-url.excemple");
        return session;
    }

    private String getReceipt() {
        return "http://receipt-url.excemple";
    }

    private Charge getCharge() {
        Charge charge = new Charge();
        charge.setId("chargeIdExcemple");
        charge.setReceiptUrl(getReceipt());
        return charge;
    }
}
