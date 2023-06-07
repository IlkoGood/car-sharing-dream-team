package com.carsharing.repository;

import com.carsharing.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findPaymentsByRental_UserId(Long userId);
}
