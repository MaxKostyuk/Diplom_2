package com.kotan4ik.tests.order;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static com.kotan4ik.utils.ErrorMessages.UNAUTHORIZED;
import static com.kotan4ik.requests.OrderApiMethods.*;
import static com.kotan4ik.requests.UserApiMethods.*;

@Epic("Order management")
@Feature("Get user orders")
@DisplayName("Get user orders tests")
public class GetUserOrderTest {
    private static final String MAIL_BASE = "testUserName@test.com";
    private static final String NAME_BASE = "testUserName";
    private static final String VALID_PASSWORD = "12345678";
    private static final String VALID_INGREDIENT_1 = "61c0c5a71d1f82001bdaaa6d";
    private static final String VALID_INGREDIENT_2 = "61c0c5a71d1f82001bdaaa6f";
    private static String testEmail;
    private static String testName;
    private static String token;
    private static List<String> ingredients;

    @BeforeAll
    public static void setUp() {
        Random random = new Random();
        int randomPrefix = random.nextInt(1000000000);
        testEmail = randomPrefix + MAIL_BASE;
        testName = randomPrefix + NAME_BASE;
        ingredients = List.of(VALID_INGREDIENT_1, VALID_INGREDIENT_2);

        Response response = createUser(testEmail, VALID_PASSWORD, testName);
        token = getTokenFromResponse(response);
        createOrder(ingredients, token);
    }

    @Test
    @DisplayName("Positive test")
    @Description("Positive test of getting user orders. Should return code 200 and GetOrdersResponse body")
    public void getUserOrderPositiveTestShouldReturn200AndBetOrdersResponseBody() {
        Response response = getUserOrders(token);

        checkResponseCode(response, HttpStatus.SC_OK);
        checkSuccessfulGetUserOrdersResponse(response);
    }

    @Test
    @DisplayName("Negative test without authorization")
    @Description("Negative test of getting user orders. Without authorization. Should return status code 401 and error body")
    public void getUserOrderNegativeTestWithoutAuthorizationShouldReturn401AndErrorBody() {
        Response response = getUserOrders();

        checkResponseCode(response, HttpStatus.SC_UNAUTHORIZED);
        checkErrorResponse(response, UNAUTHORIZED);
    }

    @AfterEach
    public void tearDown() {
        deleteUser(token);
    }
}
