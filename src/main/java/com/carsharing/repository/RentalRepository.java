package com.carsharing.repository;

import com.carsharing.model.Rental;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findRentalsByUserId(Long userId);

    List<Rental> findRentalsByReturnDateBeforeAndActualReturnDateIsNull(LocalDateTime dateTime);
}
