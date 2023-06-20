package com.carsharing.repository;

import com.carsharing.model.Payment;
import java.math.BigDecimal;
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
class PaymentRepositoryTest {
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

    private Payment getPayment() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setType(Payment.Type.PAYMENT);
        payment.setStatus(Payment.Status.PENDING);
        payment.setRentalId(1L);
        payment.setAmount(BigDecimal.valueOf(20));
        payment.setSessionUrl("http://session-url.excemple");
        payment.setSessionId("sessionIdExcemple#1");
        return payment;
    }
}
