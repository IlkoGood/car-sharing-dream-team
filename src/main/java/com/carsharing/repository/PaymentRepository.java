package com.carsharing.repository;

import com.carsharing.model.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p"
            + " JOIN Rental r ON p.rentalId = r.id"
            + " WHERE r.userId = :user_id")
    List<Payment> findPaymentsByUserId(@Param("user_id") Long userId);

    Optional<Payment> findPaymentByRentalId(Long rentalId);
}
