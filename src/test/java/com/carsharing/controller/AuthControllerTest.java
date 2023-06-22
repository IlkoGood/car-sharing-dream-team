package com.carsharing.controller;

import com.carsharing.exception.AuthenticationException;
import com.carsharing.model.User;
import com.carsharing.security.AuthService;
import com.carsharing.security.jwt.JwtTokenProvider;
import com.carsharing.util.UtilModelObjects;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
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
class AuthControllerTest extends UtilModelObjects {
    @MockBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final User defaultUser = getCustomer();

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void register_Ok() {
        Mockito.when(authService.register(defaultUser.getEmail(), defaultUser.getPassword()))
                .thenReturn(defaultUser);
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body(getRegistrationDto())
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(defaultUser.getId().intValue()))
                .body("email",Matchers.equalTo(defaultUser.getEmail()))
                .body("role", Matchers.equalTo("CUSTOMER"));
    }

    @Test
    void authenticate_Ok() throws AuthenticationException {
        String token = jwtTokenProvider.generateToken(defaultUser.getEmail());
        Mockito.when(authService.login(defaultUser.getEmail(), defaultUser.getPassword()))
                .thenReturn(token);
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body(getLoginDto())
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("accessToken", Matchers.equalTo(token));
    }
}
