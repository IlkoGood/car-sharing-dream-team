package com.carsharing.controller;

import com.carsharing.dto.request.UserRequestDto;
import com.carsharing.model.User;
import com.carsharing.security.jwt.JwtTokenProvider;
import com.carsharing.service.UserService;
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

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider tokenProvider;
    private final User defaultUser = getDefaultUser();

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void getUserInfo_ok() {
        Mockito.when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(defaultUser));
        String jwtToken = tokenProvider.generateToken("test@test.com");
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/users/me")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(21));
    }

    @Test
    void updateUser_ok() {
        Mockito.when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(defaultUser));

        User updatedUser = getUpdatedUser();
        Mockito.when(userService.save(defaultUser)).thenReturn(updatedUser);

        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setFirstName(updatedUser.getFirstName());
        requestDto.setLastName(updatedUser.getLastName());

        String jwtToken = tokenProvider.generateToken("test@test.com");
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .patch("/users/me")
                .then()
                .statusCode(200)
                .body("firstName", Matchers.equalTo("FirstName"))
                .body("lastName", Matchers.equalTo("LastName"));
    }

    @Test
    void updateRole_ok() {
        Mockito.when(userService.findByEmail("test@test.com")).thenReturn(Optional.of(defaultUser));
        Mockito.when(userService.findById(21L)).thenReturn(defaultUser);

        User updatedUser = getUpdatedUser();
        updatedUser.setRole(User.Role.CUSTOMER);
        Mockito.when(userService.save(defaultUser)).thenReturn(updatedUser);

        String jwtToken = tokenProvider.generateToken("test@test.com");
        RestAssuredMockMvc.given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .put("/users/21/role?role=CUSTOMER")
                .then()
                .statusCode(200)
                .body("role", Matchers.equalTo("CUSTOMER"));
    }

    private User getDefaultUser() {
        User user = new User();
        user.setId(21L);
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(User.Role.MANAGER);
        return user;
    }

    private User getUpdatedUser() {
        User updatedUser = new User();
        updatedUser.setId(defaultUser.getId());
        updatedUser.setEmail(defaultUser.getEmail());
        updatedUser.setPassword(defaultUser.getPassword());
        updatedUser.setRole(defaultUser.getRole());
        updatedUser.setFirstName("FirstName");
        updatedUser.setLastName("LastName");
        return updatedUser;
    }
}