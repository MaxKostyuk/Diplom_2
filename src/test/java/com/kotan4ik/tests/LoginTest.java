package com.kotan4ik.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static com.kotan4ik.ErrorMessages.INCORRECT_LOGIN_DATA;
import static com.kotan4ik.requests.UserApiMethods.*;

@Epic("User management")
@Feature("Login user")
@DisplayName("Login user tests")
public class LoginTest {
    private static final String MAIL_BASE = "testUserName@test.com";
    private static final String NAME_BASE = "testUserName";
    private static final String VALID_PASSWORD = "12345678";
    private static String testEmail;
    private static String testName;
    private String token;

    @BeforeAll
    public static void setUp() {
        Random random = new Random();
        int randomPrefix = random.nextInt(1000000000);
        testEmail = randomPrefix + MAIL_BASE;
        testName = randomPrefix + NAME_BASE;
    }

    @Test
    @DisplayName("Positive test")
    @Description("Positive test for user login. Should return successful response body")
    public void loginPositiveTestShouldReturnSuccessBody() {
        Response response = createUser(testEmail, VALID_PASSWORD, testName);
        token = getTokenFromResponse(response);

        response = loginUser(testEmail, VALID_PASSWORD, testName);
        checkCreateLoginResponse(response);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserData")
    @DisplayName("Negative test with incorrect parameter")
    @Description("Parameterized test to login user with incorrect one of parameters: password or email")
    public void loginUserNegativeTestWithIncorrectField(String email, String password) {
        Response response = createUser(testEmail, VALID_PASSWORD, testName);
        token = getTokenFromResponse(response);

        response = loginUser(email, password, testName);
        checkErrorResponse(response, INCORRECT_LOGIN_DATA);
    }

    @AfterEach
    public void tearDown() {
        deleteUser(token);
    }

    private static Stream<Arguments> provideInvalidUserData() {
        return Stream.of(
                Arguments.of(testEmail, VALID_PASSWORD + 1),
                Arguments.of(testEmail + 1, VALID_PASSWORD)
        );
    }
}
