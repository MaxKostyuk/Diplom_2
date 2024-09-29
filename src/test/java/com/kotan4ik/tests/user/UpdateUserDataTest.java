package com.kotan4ik.tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.Map;

import static com.kotan4ik.requests.UserApiMethods.*;
import static com.kotan4ik.utils.ErrorMessages.UNAUTHORIZED;
import static com.kotan4ik.utils.TestUtils.generateRandomUserProperties;
import static com.kotan4ik.utils.TestUtils.generateTestUser;

@Epic("User management")
@Feature("Update user data")
@DisplayName("Update user data tests")
public class UpdateUserDataTest {
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
    @Description("Positive test for update user data. Should return successful response body")
    public void updateUserDatePositiveTestShouldReturnSuccessfulBody() {
        String newEmail = 1 + testValues.get("email");
        String newName = 1 + testValues.get("name");

        Response response = updateUserData(newEmail, newName, token);

        checkResponseCode(response, HttpStatus.SC_OK);
        checkUpdateUserDataResponse(response, newEmail, newName);
    }

    @Test
    @DisplayName("Negative test without authorization")
    @Description("Negative test for update user data. Should return code 401 and error body with corresponding error message")
    public void updateUserDataNegativeTestWithoutAuthorizationShouldReturn401AndErrorBody() {
        Response response = updateUserData(null, null);

        checkResponseCode(response, HttpStatus.SC_UNAUTHORIZED);
        checkErrorResponse(response, UNAUTHORIZED);
    }

    @AfterEach
    public void tearDown() {
        deleteUser(token);
    }
}
