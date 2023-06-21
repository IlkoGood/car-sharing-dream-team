package com.carsharing.repository;

import com.carsharing.model.Payment;
import com.carsharing.util.UtilForTests;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest extends UtilForTests {
    @Container
    static MySQLContainer<?> database = new MySQLContainer<>("mysql:8")
            .withDatabaseName("car-sharing")
            .withUsername("root")
            .withPassword("11111");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("springboot.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("sptingboot.datasource.username", database::getUsername);
        propertyRegistry.add("sptingboot.datasource.password", database::getPassword);
    }

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void getPaymentsByUserId_Ok() {
        List<Payment> expected = new ArrayList<>();
        expected.add(paymentRepository.save(getPayment()));
        List<Payment> actual = paymentRepository.findPaymentsByUserId(1L);
        Assertions.assertEquals(expected, actual);
    }
}
