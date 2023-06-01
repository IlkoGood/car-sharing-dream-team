package com.carsharing.repository;

import com.carsharing.model.Payment;
import com.carsharing.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByRental_User(User user);
    Optional<Payment> findById(Long id);
}
