package com.carsharing.controller;

import com.carsharing.dto.mapper.impl.RentalMapper;
import com.carsharing.dto.response.RentalResponseDto;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.carsharing.security.AccessService;
import com.carsharing.security.jwt.JwtTokenProvider;
import com.carsharing.service.RentalService;
import com.carsharing.service.UserService;
import com.carsharing.util.UtilForTests;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.format.DateTimeFormatter;
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
class RentalControllerTest extends UtilForTests {
    @MockBean
    private RentalService rentalService;
    @MockBean
    private RentalMapper rentalMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private AccessService accessService;
    @Autowired
    private RentalController rentalController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final User defaultManager = getUser();
    private final Rental defaultRental = getRental();
    private final RentalResponseDto responseDto = getRentalResponseDto();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void create_Ok() {
        Mockito.when(userService.findByEmail(defaultManager.getEmail()))
                .thenReturn(defaultManager);
        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        Mockito.when(rentalMapper.mapToModel(getRentalRequestDto())).thenReturn(defaultRental);
        Mockito.when(rentalMapper.mapToDto(defaultRental)).thenReturn(getRentalResponseDto());
        Mockito.when(rentalService.save(defaultRental)).thenReturn(defaultRental);
        User customer = getCustomer();
        Authentication authCustomer =
                new UsernamePasswordAuthenticationToken(customer.getEmail(),
                        customer.getPassword());
        Mockito.when(accessService.checkUserAccess(authCustomer, defaultManager.getId()))
                .thenReturn(true);
        Assertions.assertThrows(RuntimeException.class,
                () -> rentalController.create(getRentalRequestDto(), authCustomer));
        RestAssuredMockMvc
                .given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(getRentalRequestDto())
                .when()
                .post("/rentals")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(responseDto.getId().intValue()))
                .body("returnDate", Matchers.equalTo(responseDto.getReturnDate().format(formatter)))
                .body("rentalDate", Matchers.equalTo(responseDto.getRentalDate().format(formatter)))
                .body("carId", Matchers.equalTo(responseDto.getCarId().intValue()))
                .body("userId", Matchers.equalTo(responseDto.getUserId().intValue()));
    }

    @Test
    void get_Ok() {
        Mockito.when(userService.findByEmail(defaultManager.getEmail()))
                .thenReturn(defaultManager);
        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        Long userId = 1L;
        Boolean isActive = true;
        int count = 2;
        List<Rental> rentals = getRentals(isActive, count);
        Mockito.when(rentalService.getByParam(userId, isActive)).thenReturn(rentals);
        Mockito.when(rentalMapper.mapToDto(Mockito.any(Rental.class))).thenCallRealMethod();
        User customer = getCustomer();
        Authentication authCustomer =
                new UsernamePasswordAuthenticationToken(customer.getEmail(),
                        customer.getPassword());
        Mockito.when(accessService.checkUserAccess(authCustomer, defaultManager.getId()))
                .thenReturn(true);
        Assertions.assertThrows(RuntimeException.class,
                () -> rentalController.get(authCustomer, userId, isActive));
        RestAssuredMockMvc
                .given()
                .queryParam("userId", userId)
                .queryParam("isActive", isActive)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/rentals")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(count))
                .body("[0].id", Matchers.equalTo(rentals.get(0).getId().intValue()))
                .body("[0].returnDate", Matchers.equalTo(rentals.get(0).getReturnDate().format(formatter)))
                .body("[0].rentalDate", Matchers.equalTo(rentals.get(0).getRentalDate().format(formatter)))
                .body("[0].carId", Matchers.equalTo(rentals.get(0).getCarId().intValue()))
                .body("[0].userId", Matchers.equalTo(rentals.get(0).getUserId().intValue()))
                .body("[1].id", Matchers.equalTo(rentals.get(1).getId().intValue()))
                .body("[1].returnDate", Matchers.equalTo(rentals.get(1).getReturnDate().format(formatter)))
                .body("[1].rentalDate", Matchers.equalTo(rentals.get(1).getRentalDate().format(formatter)))
                .body("[1].carId", Matchers.equalTo(rentals.get(1).getCarId().intValue()))
                .body("[1].userId", Matchers.equalTo(rentals.get(1).getUserId().intValue()));
    }

    @Test
    void getById_Ok() {
        Mockito.when(userService.findByEmail(defaultManager.getEmail()))
                .thenReturn(defaultManager);
        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        Mockito.when(rentalService.getById(defaultRental.getId())).thenReturn(defaultRental);
        Mockito.when(rentalMapper.mapToDto(defaultRental)).thenReturn(getRentalResponseDto());
        User customer = getCustomer();
        Authentication authCustomer =
                new UsernamePasswordAuthenticationToken(customer.getEmail(),
                        customer.getPassword());
        Mockito.when(accessService.checkUserAccess(authCustomer, defaultManager.getId()))
                .thenReturn(true);
        Assertions.assertThrows(RuntimeException.class,
                () -> rentalController.getById(authCustomer, defaultRental.getId()));
        RestAssuredMockMvc
                .given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/rentals/{id}", defaultRental.getId())
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(responseDto.getId().intValue()))
                .body("returnDate", Matchers.equalTo(responseDto.getReturnDate().format(formatter)))
                .body("rentalDate", Matchers.equalTo(responseDto.getRentalDate().format(formatter)))
                .body("carId", Matchers.equalTo(responseDto.getCarId().intValue()))
                .body("userId", Matchers.equalTo(responseDto.getUserId().intValue()));
    }

    @Test
    void close_Ok() {
        Mockito.when(userService.findByEmail(defaultManager.getEmail()))
                .thenReturn(defaultManager);
        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        Mockito.when(rentalService.getById(defaultRental.getId())).thenReturn(defaultRental);
        Mockito.when(rentalService.save(defaultRental)).thenReturn(defaultRental);
        Mockito.when(rentalService.closeRental(defaultRental)).thenReturn(defaultRental);
        Mockito.when(rentalMapper.mapToDto(defaultRental)).thenReturn(getRentalResponseDto());
        Mockito.when(rentalMapper.mapToModel(getRentalRequestDto())).thenReturn(defaultRental);
        RestAssuredMockMvc
                .given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .put("/rentals/{id}/return", defaultRental.getId())
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(responseDto.getId().intValue()))
                .body("returnDate", Matchers.equalTo(responseDto.getReturnDate().format(formatter)))
                .body("rentalDate", Matchers.equalTo(responseDto.getRentalDate().format(formatter)))
                .body("actualReturnDate", Matchers.equalTo(responseDto.getActualReturnDate().format(formatter)))
                .body("carId", Matchers.equalTo(responseDto.getCarId().intValue()))
                .body("userId", Matchers.equalTo(responseDto.getUserId().intValue()));
        User customer = getCustomer();
        Authentication authCustomer =
                new UsernamePasswordAuthenticationToken(customer.getEmail(),
                        customer.getPassword());
        Mockito.when(accessService.checkUserAccess(authCustomer, defaultManager.getId()))
                .thenReturn(true);
        Assertions.assertThrows(RuntimeException.class,
                () -> rentalController.close(authCustomer, defaultRental.getId()));
    }
}
