package com.carsharing.controller;

import com.carsharing.dto.mapper.impl.PaymentMapper;
import com.carsharing.dto.response.PaymentResponseDto;
import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.carsharing.security.AccessService;
import com.carsharing.security.jwt.JwtTokenProvider;
import com.carsharing.service.CarService;
import com.carsharing.service.PaymentService;
import com.carsharing.service.RentalService;
import com.carsharing.service.UserService;
import com.carsharing.util.UtilModelObjects;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest extends UtilModelObjects {
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private RentalService rentalService;
    @MockBean
    private CarService carService;
    @MockBean
    private PaymentMapper paymentMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private AccessService accessService;
    @Autowired
    private PaymentController paymentController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final User defaultManager = getUser();
    private final Payment defaultPayment = getPayment();
    private final Rental defaultRental = getRental();
    private final Car defaultCar = getCar();
    private final PaymentResponseDto responseDto = getPaymentResponseDto();

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void createPaymentSession_Ok() {
        Mockito.when(userService.findByEmail(defaultManager.getEmail())).thenReturn(defaultManager);
        Mockito.when(paymentMapper.mapToModel(getPaymentRequestDto())).thenReturn(defaultPayment);
        Mockito.when(rentalService.getById(defaultPayment.getRentalId())).thenReturn(defaultRental);
        Mockito.when(carService.getById(defaultRental.getCarId())).thenReturn(defaultCar);
        Mockito.when(paymentService.createPaymentSession(defaultPayment, defaultRental, defaultCar))
                .thenReturn(defaultPayment);
        Mockito.when(paymentService.save(defaultPayment)).thenReturn(defaultPayment);
        Mockito.when(paymentMapper.mapToDto(defaultPayment)).thenReturn(getPaymentResponseDto());

        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        User customer = getCustomer();
        Authentication authCustomer =
                new UsernamePasswordAuthenticationToken(customer.getEmail(),
                        customer.getPassword());
        Mockito.when(accessService.checkUserAccess(authCustomer, defaultManager.getId()))
                .thenReturn(true);
        Assertions.assertThrows(RuntimeException.class,
                () -> paymentController.createPaymentSession(authCustomer, getPaymentRequestDto()));
        RestAssuredMockMvc
                .given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(getPaymentRequestDto())
                .when()
                .post("/payments")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(responseDto.getId().intValue()))
                .body("status", Matchers.equalTo(responseDto.getStatus().toString()))
                .body("type", Matchers.equalTo(responseDto.getType().toString()))
                .body("rental", Matchers.equalTo(responseDto.getRental().intValue()))
                .body("sessionUrl", Matchers.equalTo(responseDto.getSessionUrl()))
                .body("sessionId", Matchers.equalTo(responseDto.getSessionId()))
                .body("amount", Matchers.equalTo(responseDto.getAmount().floatValue()));
    }

    @Test
    void getPaymentByUserId_Ok() {
        Long userId = defaultManager.getId();
        Mockito.when(userService.findByEmail(defaultManager.getEmail()))
                .thenReturn(defaultManager);
        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        List<Payment> payments = getPayments(2);
        Mockito.when(paymentService.getByUserId(userId)).thenReturn(payments);
        Mockito.when(paymentMapper.mapToDto(Mockito.any(Payment.class))).thenCallRealMethod();
        User customer = getCustomer();
        Authentication authCustomer =
                new UsernamePasswordAuthenticationToken(customer.getEmail(),
                        customer.getPassword());
        Mockito.when(accessService.checkUserAccess(authCustomer, defaultManager.getId()))
                .thenReturn(true);
        Assertions.assertThrows(RuntimeException.class,
                () -> paymentController.getPaymentByUserId(defaultManager.getId(), authCustomer));
        RestAssuredMockMvc
                .given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .queryParam("userId", defaultManager.getId())
                .when()
                .get("/payments")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(payments.size()))
                .body("[0].id", Matchers.equalTo(payments.get(0).getId().intValue()))
                .body("[0].status", Matchers.equalTo(payments.get(0).getStatus().toString()))
                .body("[0].type", Matchers.equalTo(payments.get(0).getType().toString()))
                .body("[0].rental", Matchers.equalTo(payments.get(0).getRentalId().intValue()))
                .body("[0].sessionUrl", Matchers.equalTo(payments.get(0).getSessionUrl()))
                .body("[0].sessionId", Matchers.equalTo(payments.get(0).getSessionId()))
                .body("[1].id", Matchers.equalTo(payments.get(1).getId().intValue()))
                .body("[1].status", Matchers.equalTo(payments.get(1).getStatus().toString()))
                .body("[1].type", Matchers.equalTo(payments.get(1).getType().toString()))
                .body("[1].rental", Matchers.equalTo(payments.get(1).getRentalId().intValue()))
                .body("[1].sessionUrl", Matchers.equalTo(payments.get(1).getSessionUrl()))
                .body("[1].sessionId", Matchers.equalTo(payments.get(1).getSessionId()));
    }

    @Test
    void getSucceedRedirection_Ok() {
        Payment payment = getPayment();
        payment.setStatus(Payment.Status.PAID);
        payment.setSessionUrl(null);
        payment.setReceiptUrl("http://receipt-url.excemple");
        Mockito.when(paymentService.getById(payment.getId())).thenReturn(payment);
        Mockito.when(paymentService.getReceiptUrl(payment.getSessionId())).thenReturn(payment.getReceiptUrl());
        Mockito.when(paymentService.save(payment)).thenReturn(payment);
        RestAssuredMockMvc
                .when()
                .get("/payments/success/{id}", payment.getId().intValue())
                .then()
                .statusCode(302);
        Mockito.verify(paymentService).getById(payment.getId());
        Mockito.verify(paymentService).getReceiptUrl(payment.getSessionId());
        Mockito.verify(paymentService).save(payment);
    }

    @Test
    void getCanceledRedirection_Ok() {
        Mockito.when(paymentService.getById(defaultPayment.getId())).thenReturn(defaultPayment);
        RestAssuredMockMvc
                .when()
                .get("/payments/cancel/" + defaultPayment.getId().intValue())
                .then()
                .statusCode(302);
        Mockito.verify(paymentService).getById(defaultPayment.getId());
    }
}
