package com.carsharing.repository;

import com.carsharing.model.Rental;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RentalRepositoryTest {
    @Container
    static MySQLContainer<?> database = new MySQLContainer<>("mysql:8")
            .withDatabaseName("springboot")
            .withUsername("springboot")
            .withPassword("springboot");

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
    }

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    @Sql("/scripts/rentalRepoTest.sql")
    void findOverdueRentals_ok() {
        List<Rental> overdueRentals =
                rentalRepository.findOverdueRentals(LocalDateTime.of(2023, 06, 04, 0, 0, 0));
        Assertions.assertEquals(1, overdueRentals.size());
        Assertions.assertNull(overdueRentals.get(0).getActualReturnDate());
    }
}