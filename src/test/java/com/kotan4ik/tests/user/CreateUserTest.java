package com.kotan4ik.tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static com.kotan4ik.requests.UserApiMethods.*;
import static com.kotan4ik.utils.ErrorMessages.NOT_ENOUGH_DATA;
import static com.kotan4ik.utils.ErrorMessages.USER_ALREADY_EXISTS;
import static com.kotan4ik.utils.TestUtils.generateRandomUserProperties;

@Epic("User management")
@Feature("Create user")
@DisplayName("Create user tests")
public class CreateUserTest {
    private static Map<String, String> testValues;
    private String token;

    @BeforeAll
    public static void setUp() {
        testValues = generateRandomUserProperties();
    }

    @Test
    @DisplayName("Positive test")
    @Description("Positive test to create user")
    public void createUserPositiveShouldReturnSuccessBody() {
        Response response = createUser(testValues.get("email"), testValues.get("password"), testValues.get("name"));
        token = getTokenFromResponse(response);

        checkResponseCode(response, HttpStatus.SC_OK);
        checkCreateLoginResponse(response);
    }

    @Test
    @DisplayName("Negative test - user with these data already exists")
    @Description("Creating user twice with the same data. Second time should be error response")
    public void createUserNegativeUserAlreadyExists() {
        Response response = createUser(testValues.get("email"), testValues.get("password"), testValues.get("name"));
        token = getTokenFromResponse(response);

        response = createUser(testValues.get("email"), testValues.get("password"), testValues.get("name"));
        checkResponseCode(response, HttpStatus.SC_FORBIDDEN);
        checkErrorResponse(response, USER_ALREADY_EXISTS);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserData")
    @DisplayName("Negative test with missing parameter")
    @Description("Parameterized test to create user with missing one of parameters: password, email or user name")
    public void createUserNegativeTestWithMissingField(String email, String password, String name) {
        Response response = createUser(email, password, name);

        checkResponseCode(response, HttpStatus.SC_FORBIDDEN);
        checkErrorResponse(response, NOT_ENOUGH_DATA);
    }

    @AfterEach
    public void tearDown() {
        if (token != null)
            deleteUser(token);
    }

    private static Stream<Arguments> provideInvalidUserData() {
        return Stream.of(
                Arguments.of(null, testValues.get("password"), testValues.get("name")),
                Arguments.of(testValues.get("email"), null, testValues.get("name")),
                Arguments.of(testValues.get("email"), testValues.get("password"), null)
        );
    }
}
