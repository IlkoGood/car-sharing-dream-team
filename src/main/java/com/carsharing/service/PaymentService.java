package com.carsharing.service;

import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import java.util.List;

public interface PaymentService {

    Payment save(Payment payment);

    void delete(Payment payment);

    List<Payment> getAll();

    Payment getById(Long id);

    List<Payment> getByUserId(Long userId);

    Payment createPaymentSession(Payment payment, Rental rental, Car car);

    String getReceiptUrl(String paymentSessionId);
}
