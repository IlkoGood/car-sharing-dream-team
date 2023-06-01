package com.carsharing.service;

import com.carsharing.model.Payment;
import com.carsharing.model.User;
import java.util.List;

public interface PaymentService {

    Payment save(Payment payment);

    void delete(Payment payment);

    List<Payment> getAll();

    Payment getById(Long id);

    List<Payment> getByUser(User user);
}
