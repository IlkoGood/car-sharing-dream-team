package com.carsharing.repository;

import com.carsharing.model.Rental;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findRentalsByUserId(Long userId);

    @Query("SELECT r FROM Rental r "
            + "WHERE r.returnDate < :date_now AND r.actualReturnDate IS NULL")
    List<Rental> findOverdueRentals(@Param("date_now") LocalDateTime dateTime);
}
