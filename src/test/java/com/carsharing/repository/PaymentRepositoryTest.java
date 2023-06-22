package com.carsharing.repository;

import com.carsharing.model.Payment;
import com.carsharing.util.UtilModelObjects;
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
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest extends UtilModelObjects {
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

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @Sql("/scripts/init_data_for_test_payment_repo.sql")
    void getPaymentsByUserId_Ok() {
        Long id = 50L;
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
