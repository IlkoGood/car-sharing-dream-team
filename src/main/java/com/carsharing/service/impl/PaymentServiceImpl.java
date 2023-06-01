package com.carsharing.service.impl;

import com.carsharing.model.Payment;
import com.carsharing.model.User;
import com.carsharing.repository.PaymentRepository;
import com.carsharing.service.PaymentService;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void delete(Payment payment) {
        paymentRepository.delete(payment);
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can`t find payment by id: " + id)
        );
    }

    @Override
    public List<Payment> getByUser(User user) {
        return paymentRepository.findAllByRental_User(user);
    }
}
