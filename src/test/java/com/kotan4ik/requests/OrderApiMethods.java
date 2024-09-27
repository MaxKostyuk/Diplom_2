package com.kotan4ik.requests;

import com.kotan4ik.models.OrderSuccessfulResponse;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderApiMethods extends BaseRequest {
    private static final String ORDER_BASE = "orders/";


    @Step("Create order request with ingredients = {ingredients} and token = {token}")
    public static Response createOrder(List<String> ingredientList, String token) {
        Map<String, List<String>> ingredientMap = Map.of("ingredients", ingredientList);

        return RestAssured.given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(ingredientMap)
                .post(ORDER_BASE);
    }

    @Step("Create order request with ingredients ={ingredients} and without authorization")
    public static Response createOrder(List<String> ingredientList) {
        Map<String, List<String>> ingredientMap = Map.of("ingredients", ingredientList);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(ingredientMap)
                .post(ORDER_BASE);
    }

    @Step("Create order request with token = {token} and without ingredients")
    public static Response createOrder(String token) {
        Map<String, List<String>> ingredientMap = Map.of("ingredients", new ArrayList<>());

        return RestAssured.given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(ingredientMap)
                .post(ORDER_BASE);
    }

    @Step("Checking successful order response is valid")
    public static void checkSuccessfulOrderResponse(Response response) {
        OrderSuccessfulResponse responseObject = assertDoesNotThrow(() -> fromJson(response.asString(), OrderSuccessfulResponse.class));
        assertAll(() -> assertTrue(responseObject.isSuccess(), "Success field should be true"),
                () -> assertNotNull(responseObject.getName(), "Name shouldn't be null"),
                () -> assertNotNull(responseObject.getOrder(), "Order shouldn't be null"));
    }

}
