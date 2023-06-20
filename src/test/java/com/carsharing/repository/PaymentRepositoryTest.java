package com.carsharing.repository;

import com.carsharing.model.Payment;
import java.util.List;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@AllArgsConstructor
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest {
    @Container
    static MySQLContainer<?> database = new MySQLContainer<>("mysql:8")
            .withDatabaseName("springboot")
            .withUsername("springboot")
            .withPassword("springboot");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("springboot.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("sptingboot.datasource.username", database::getUsername);
        propertyRegistry.add("sptingboot.datasource.password", database::getPassword);
    }

    private final PaymentRepository paymentRepository;

    @Test
    void getPaymentsByUserId_Ok() {
        //List<Payment> actual = paymentRepository.findPaymentsByUserId(1L);
    }
}