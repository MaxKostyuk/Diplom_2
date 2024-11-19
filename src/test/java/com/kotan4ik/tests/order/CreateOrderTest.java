package com.kotan4ik.tests.order;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.kotan4ik.requests.OrderApiMethods.*;
import static com.kotan4ik.requests.UserApiMethods.deleteUser;
import static com.kotan4ik.utils.ErrorMessages.INGREDIENTS_SHOULD_BE_PROVIDED;
import static com.kotan4ik.utils.TestUtils.generateTestUser;
import static com.kotan4ik.utils.TestUtils.getTestIngredients;

@Epic("Order management")
@Feature("Create order")
@DisplayName("Create order tests")
public class CreateOrderTest {
    private static List<String> ingredients;
    private String token;

    @BeforeAll
    public static void setUp() {
        ingredients = getTestIngredients(2);
    }

    @BeforeEach
    public void generateUser() {
        token = generateTestUser();
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
        List<String> invalidIngredients = List.of("INVALID" + ingredients.get(0));

        Response response = createOrder(invalidIngredients, token);

        checkResponseCode(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @AfterEach
    public void tearDown() {
        deleteUser(token);
    }
}
