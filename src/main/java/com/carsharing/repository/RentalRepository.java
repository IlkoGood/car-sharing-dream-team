package com.carsharing.repository;

import com.carsharing.model.Rental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findRentalsByUser_Id(Long userId);

    List<Rental> findRentalsByActualReturnDateIsNull();

    List<Rental> findRentalsByActualReturnDateIsNotNull();

    Optional<Rental> findById(Long id);
}
