package com.carsharing.controller;

import com.carsharing.dto.mapper.impl.CarMapper;
import com.carsharing.dto.request.CarRequestDto;
import com.carsharing.dto.response.CarResponseDto;
import com.carsharing.model.Car;
import com.carsharing.model.User;
import com.carsharing.security.jwt.JwtTokenProvider;
import com.carsharing.service.CarService;
import com.carsharing.service.UserService;
import com.carsharing.util.UtilForTests;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest extends UtilForTests {
    @MockBean
    private CarService carService;
    @MockBean
    private UserService userService;
    @MockBean
    private CarMapper carMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final User defaultManager = getUser();
    private final Car defaultCar = getCar();
    private final CarResponseDto responseDto = getCarResponseDto();

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void create_Ok() {
        Mockito.when(userService.findByEmail(defaultManager.getEmail()))
                .thenReturn(defaultManager);
        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        Mockito.when(carService.save(defaultCar)).thenReturn(defaultCar);
        Mockito.when(carMapper.mapToModel(getCarRequestDto())).thenReturn(defaultCar);
        Mockito.when(carMapper.mapToDto(defaultCar)).thenReturn(getCarResponseDto());
        RestAssuredMockMvc
                .given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(getCarRequestDto())
                .when()
                .post("/cars")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(responseDto.getId().intValue()))
                .body("model", Matchers.equalTo(responseDto.getModel()))
                .body("brand", Matchers.equalTo(responseDto.getBrand()))
                .body("type", Matchers.equalTo(responseDto.getType().toString()))
                .body("inventory", Matchers.equalTo(responseDto.getInventory()))
                .body("dailyFee", Matchers.equalTo(responseDto.getDailyFee().intValue()));
    }

    @Test
    void get_Ok() {
        Mockito.when(carService.getById(defaultCar.getId())).thenReturn(defaultCar);
        Mockito.when(carMapper.mapToDto(defaultCar)).thenReturn(responseDto);
        RestAssuredMockMvc
                .when()
                .get("/cars/" + defaultCar.getId().intValue())
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(responseDto.getId().intValue()))
                .body("model", Matchers.equalTo(responseDto.getModel()))
                .body("brand", Matchers.equalTo(responseDto.getBrand()))
                .body("type", Matchers.equalTo(responseDto.getType().toString()))
                .body("inventory", Matchers.equalTo(responseDto.getInventory()))
                .body("dailyFee", Matchers.equalTo(responseDto.getDailyFee().intValue()));
    }

    @Test
    void update_Ok() {
        Mockito.when(userService.findByEmail(defaultManager.getEmail()))
                .thenReturn(defaultManager);
        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        Mockito.when(carService.update(defaultCar)).thenReturn(defaultCar);
        Mockito.when(carMapper.mapToModel(getCarRequestDto())).thenReturn(defaultCar);
        Mockito.when(carMapper.mapToDto(defaultCar)).thenReturn(getCarResponseDto());
        RestAssuredMockMvc
                .given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(getCarRequestDto())
                .when()
                .put("/cars/" + defaultCar.getId().intValue())
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(responseDto.getId().intValue()))
                .body("model", Matchers.equalTo(responseDto.getModel()))
                .body("brand", Matchers.equalTo(responseDto.getBrand()))
                .body("type", Matchers.equalTo(responseDto.getType().toString()))
                .body("inventory", Matchers.equalTo(responseDto.getInventory()))
                .body("dailyFee", Matchers.equalTo(responseDto.getDailyFee().intValue()));
    }

    @Test
    void delete_Ok() {
        Mockito.when(userService.findByEmail(defaultManager.getEmail()))
                .thenReturn(defaultManager);
        String jwtToken = jwtTokenProvider.generateToken(defaultManager.getEmail());
        RestAssuredMockMvc
                .given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(getCarRequestDto())
                .when()
                .delete("/cars/" + defaultCar.getId().intValue())
                .then()
                .statusCode(200);
    }

    @Test
    void getAll_Ok() {
        int countOfCar = 2;
        List<Car> cars = getCars(countOfCar);
        Mockito.when(carService.getAll()).thenReturn(cars);
        Mockito.when(carMapper.mapToDto(Mockito.any(Car.class))).thenCallRealMethod();
        RestAssuredMockMvc
                .when()
                .get("/cars")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(countOfCar))
                .body("[0].model", Matchers.equalTo(cars.get(0).getModel()))
                .body("[0].brand", Matchers.equalTo(cars.get(0).getBrand()))
                .body("[0].type", Matchers.equalTo(cars.get(0).getType().toString()))
                .body("[0].inventory", Matchers.equalTo(cars.get(0).getInventory()))
                .body("[0].dailyFee", Matchers.equalTo(cars.get(0).getDailyFee().intValue()))
                .body("[1].id", Matchers.equalTo(cars.get(1).getId().intValue()))
                .body("[1].model", Matchers.equalTo(cars.get(1).getModel()))
                .body("[1].brand", Matchers.equalTo(cars.get(1).getBrand()))
                .body("[1].type", Matchers.equalTo(cars.get(1).getType().toString()))
                .body("[1].inventory", Matchers.equalTo(cars.get(1).getInventory()))
                .body("[1].dailyFee", Matchers.equalTo(cars.get(1).getDailyFee().intValue()));
    }
}
