package com.kotan4ik.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.kotan4ik.ErrorMessages.UNAUTHORIZED;
import static com.kotan4ik.requests.UserApiMethods.*;

@Epic("User management")
@Feature("Update user data")
@DisplayName("Update user data tests")
public class UpdateUserDataTest {
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
    @Description("Positive test for update user data. Should return successful response body")
    public void updateUserDatePositiveTestShouldReturnSuccessfulBody() {
        Response response = createUser(testEmail, VALID_PASSWORD, testName);
        token = getTokenFromResponse(response);
        String newEmail = 1 + testEmail;
        String newName = 1 + testName;

        response = updateUserData(newEmail, newName, token);
        checkResponseCode(response, HttpStatus.SC_OK);
        checkUpdateUserDataResponse(response, newEmail, newName);
    }

    @Test
    @DisplayName("Negative test without authorization")
    @Description("Negative test for update user data. Should return code 401 and error body with corresponding error message")
    public void updateUserDataNegativeTestWithoutAuthorizationShouldReturn401AndErrorBody() {
        Response response = createUser(testEmail, VALID_PASSWORD, testName);
        token = getTokenFromResponse(response);

        response = updateUserData(null, null);
        checkResponseCode(response, HttpStatus.SC_UNAUTHORIZED);
        checkErrorResponse(response, UNAUTHORIZED);
    }

    @AfterEach
    public void tearDown() {
        deleteUser(token);
    }
}
