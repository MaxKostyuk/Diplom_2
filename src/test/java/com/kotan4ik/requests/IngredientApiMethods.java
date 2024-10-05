package com.kotan4ik.requests;

import com.kotan4ik.models.Ingredient;
import com.kotan4ik.models.IngredientsResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class IngredientApiMethods extends BaseRequest{
    private static final String INGREDIENTS_BASE = "ingredients";

    public static List<Ingredient> getIngredients() {
        Response response = RestAssured.given().get(INGREDIENTS_BASE);
        IngredientsResponse ingredientsResponse = assertDoesNotThrow(() -> fromJson(response.asString(), IngredientsResponse.class));
        return ingredientsResponse.getData();
    }
}
