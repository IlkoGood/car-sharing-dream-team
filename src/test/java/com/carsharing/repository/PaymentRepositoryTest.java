package com.carsharing.repository;

import com.carsharing.model.Payment;
import com.carsharing.util.UtilModelObjects;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
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

@org.springframework.core.annotation.Order(1)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest extends UtilModelObjects {
    @Container
    static MySQLContainer<?> database = new MySQLContainer<>("mysql:8")
            .withDatabaseName("test_payment_repo")
            .withUsername("username")
            .withPassword("password");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
    }

    @Autowired
    private PaymentRepository paymentRepository;

    @Order(1)
    @Test
    @Sql("/scripts/init_data_for_test_payment_repo.sql")
    void getPaymentsByUserId_Ok() {
        Long id = 50L;
        System.out.println(paymentRepository.findById(id).get());
        List<Payment> expected = new ArrayList<>();
        Payment payment = getPayment();
        payment.setId(id);
        payment.setRentalId(id);
        payment.setAmount(BigDecimal.valueOf(22.22));
        expected.add(payment);
        List<Payment> actual = paymentRepository.findPaymentsByUserId(id);
        Assertions.assertEquals(expected, actual);
    }
}
