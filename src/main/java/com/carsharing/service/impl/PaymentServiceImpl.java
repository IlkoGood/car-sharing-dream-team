package com.carsharing.service.impl;

import com.carsharing.api.stripe.PaymentProvider;
import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.carsharing.repository.PaymentRepository;
import com.carsharing.service.PaymentService;
import com.carsharing.service.UserService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final double FINE_MULTIPLIER = 1.2;
    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final PaymentProvider paymentProvider;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can`t find payment by id: " + id));
    }

    @Override
    public List<Payment> getByUserId(Long userId) {
        return userId == null ? paymentRepository.findAll()
                : paymentRepository.findPaymentsByUserId(userId);
    }

    @Override
    public Payment createPaymentSession(Payment payment, Rental rental, Car car) {
        Optional<Payment> checkExistPayment =
                paymentRepository.findPaymentByRentalId(rental.getId());
        if (checkExistPayment.isPresent()) {
            throw new RuntimeException("Payment[id: "
                    + checkExistPayment.get().getId() + "] with rental id: '"
                    + rental.getId() + "' has already exist!");
        }
        if (rental.getActualReturnDate() == null) {
            throw new RuntimeException("Rental[id: " + rental.getId() + "] is still active!");
        }
        BigDecimal moneyToPay = this.calculatePayment(rental, car);
        BigDecimal moneyToFine = this.calculateFine(rental, car);
        if (moneyToFine.doubleValue() > 0) {
            payment.setType(Payment.Type.FINE);
        }
        payment.setAmount(moneyToPay.add(moneyToFine));
        payment = paymentRepository.save(payment);
        User user = userService.findById(rental.getUserId());
        Session session = paymentProvider.createPaymentSession(
                moneyToPay,
                moneyToFine,
                payment,
                rental,
                car,
                user
        );
        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        return payment;
    }

    @Override
    public String getReceiptUrl(String paymentSessionId) {
        return paymentProvider.getCharge(paymentSessionId).getReceiptUrl();
    }

    private BigDecimal calculatePayment(Rental rental, Car car) {
        long daysRental = ChronoUnit.DAYS.between(rental.getRentalDate(),
                rental.getReturnDate()) + 1;
        BigDecimal dailyFee = car.getDailyFee();
        return dailyFee.multiply(BigDecimal.valueOf(daysRental));
    }

    private BigDecimal calculateFine(Rental rental, Car car) {
        long daysRental = ChronoUnit.DAYS.between(rental.getRentalDate(),
                rental.getReturnDate()) + 1;
        long daysActual = ChronoUnit.DAYS.between(rental.getRentalDate(),
                rental.getActualReturnDate()) + 1;
        BigDecimal dailyFee = car.getDailyFee();
        BigDecimal moneyToFine = BigDecimal.ZERO;
        if (daysActual > daysRental) {
            moneyToFine = dailyFee.multiply(
                BigDecimal.valueOf((daysActual - daysRental) * FINE_MULTIPLIER));
        }
        return moneyToFine;
    }
}
