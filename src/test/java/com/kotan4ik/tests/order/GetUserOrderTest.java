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
import static com.kotan4ik.utils.ErrorMessages.UNAUTHORIZED;
import static com.kotan4ik.utils.TestUtils.generateTestUser;
import static com.kotan4ik.utils.TestUtils.getTestIngredients;

@Epic("Order management")
@Feature("Get user orders")
@DisplayName("Get user orders tests")
public class GetUserOrderTest {
    private static List<String> ingredients;
    private String token;

    @BeforeAll
    public static void setUp() {
        ingredients = getTestIngredients();

    }

    @BeforeEach
    public void generateTestData() {
        token = generateTestUser();
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
