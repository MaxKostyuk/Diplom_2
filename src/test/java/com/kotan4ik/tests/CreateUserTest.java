package com.kotan4ik.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.kotan4ik.ErrorMessages.USER_ALREADY_EXISTS;
import static com.kotan4ik.requests.UserApiMethods.*;

@Epic("User management")
@Feature("Create user")
@DisplayName("Create user tests")
public class CreateUserTest {
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
        testEmail =  randomPrefix + MAIL_BASE;
        testName = randomPrefix + NAME_BASE;
    }

    @AfterEach
    public void tearDown() {
        deleteUser(token);
    }

    @Test
    @DisplayName("Positive test")
    @Description("Positive test to create user")
    public void createUserPositiveShouldReturnSuccessBody() {
        Response response = createUser(testEmail, VALID_PASSWORD, testName);
        checkCreateLoginResponse(response);

        token = getTokenFromResponse(response);
    }

    @Test
    @DisplayName("Negative test - user with these data already exists")
    @Description("Creating user twice with the same data. Second time should be error response")
    public void createUserNegativeUserAlreadyExists() {
        Response response = createUser(testEmail, VALID_PASSWORD, testName);
        token = getTokenFromResponse(response);

        response = createUser(testEmail, VALID_PASSWORD, testName);
        checkErrorResponse(response, USER_ALREADY_EXISTS);
    }
}