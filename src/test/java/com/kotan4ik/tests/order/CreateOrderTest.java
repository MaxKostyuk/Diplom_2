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

import static com.kotan4ik.ErrorMessages.INGREDIENTS_SHOULD_BE_PROVIDED;
import static com.kotan4ik.requests.OrderApiMethods.*;
import static com.kotan4ik.requests.UserApiMethods.*;

@Epic("Order management")
@Feature("Create order")
@DisplayName("Create order tests")
public class CreateOrderTest {
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
    }

    @Test
    @DisplayName("Positive test")
    @Description("Positive test for order creation with correct ingredients and authorization")
    public void createOrderPositiveTestShouldReturnSuccessfulOrderBody() {
        Response response = createOrder(ingredients, token);

        checkResponseCode(response, HttpStatus.SC_OK);
        checkSuccessfulOrderResponse(response);
    }

    @Test
    @DisplayName("Negative test without authorization")
    @Description("Negative for creation of order. Without authorization. Should return status code 500")
    public void createOrderNegativeTestWithoutAuthorizationShouldReturn500() {
        Response response = createOrder(ingredients);

        checkResponseCode(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Negative test without ingredients")
    @Description("Negative test for creation order without ingredients. Should return status code 400 and error body")
    public void createOrderNegativeTestWithoutIngredientsShouldReturn400AndErrorBody() {
        Response response = createOrder(token);

        checkResponseCode(response, HttpStatus.SC_BAD_REQUEST);
        checkErrorResponse(response, INGREDIENTS_SHOULD_BE_PROVIDED);
    }

    @Test
    @DisplayName("Negative test with incorrect ingredients")
    @Description("Negative test for order creation with incorrect ingredients and with authorization. Should return status code 500 and error body")
    public void createOrderNegativeTestWithIncorrectIngredientsShouldReturn500() {
        ingredients = List.of("INVALID" + VALID_INGREDIENT_1);

        Response response = createOrder(ingredients, token);

        checkResponseCode(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @AfterEach
    public void tearDown() {
        deleteUser(token);
    }
}
