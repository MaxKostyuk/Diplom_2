package com.kotan4ik.tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static com.kotan4ik.requests.UserApiMethods.*;
import static com.kotan4ik.utils.ErrorMessages.INCORRECT_LOGIN_DATA;
import static com.kotan4ik.utils.TestUtils.generateRandomUserProperties;
import static com.kotan4ik.utils.TestUtils.generateTestUser;

@Epic("User management")
@Feature("Login user")
@DisplayName("Login user tests")
public class LoginTest {
    private static Map<String, String> testValues;
    private String token;

    @BeforeAll
    public static void setUp() {
        testValues = generateRandomUserProperties();
    }

    @BeforeEach
    public void generateUser() {
        token = generateTestUser(testValues);
    }

    @Test
    @DisplayName("Positive test")
    @Description("Positive test for user login. Should return successful response body")
    public void loginPositiveTestShouldReturnSuccessBody() {
        Response response = loginUser(testValues.get("email"), testValues.get("password"), testValues.get("name"));

        checkResponseCode(response, HttpStatus.SC_OK);
        checkCreateLoginResponse(response);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserData")
    @DisplayName("Negative test with incorrect parameter")
    @Description("Parameterized test to login user with incorrect one of parameters: password or email")
    public void loginUserNegativeTestWithIncorrectField(String email, String password) {
        Response response = loginUser(email, password, testValues.get("name"));

        checkResponseCode(response, HttpStatus.SC_UNAUTHORIZED);
        checkErrorResponse(response, INCORRECT_LOGIN_DATA);
    }

    @AfterEach
    public void tearDown() {
        deleteUser(token);
    }

    private static Stream<Arguments> provideInvalidUserData() {
        return Stream.of(
                Arguments.of(testValues.get("email"), testValues.get("password") + 1),
                Arguments.of(testValues.get("email") + 1, testValues.get("password"))
        );
    }
}
