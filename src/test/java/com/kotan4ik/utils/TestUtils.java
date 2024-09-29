package com.kotan4ik.utils;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.kotan4ik.requests.UserApiMethods.createUser;
import static com.kotan4ik.requests.UserApiMethods.getTokenFromResponse;

public class TestUtils {
    private static final String MAIL_BASE = "testUserName@test.com";
    private static final String NAME_BASE = "testUserName";
    private static final String VALID_PASSWORD = "12345678";
    private static final String VALID_INGREDIENT_1 = "61c0c5a71d1f82001bdaaa6d";
    private static final String VALID_INGREDIENT_2 = "61c0c5a71d1f82001bdaaa6f";

    public static Map<String, String> generateRandomUserProperties() {
        Map<String, String> values = new HashMap<>();
        Random random = new Random();

        int randomPrefix = random.nextInt(1000000000);
        String testEmail = randomPrefix + MAIL_BASE;
        String testName = randomPrefix + NAME_BASE;

        values.put("email", testEmail);
        values.put("password", VALID_PASSWORD);
        values.put("name", testName);

        return values;
    }

    public static String generateTestUser(Map<String, String> userProperties) {
        Response response = createUser(userProperties.get("email"), userProperties.get("password"), userProperties.get("name"));
        return getTokenFromResponse(response);
    }

    public static String generateTestUser() {
        Map<String, String> userProperties = generateRandomUserProperties();
        Response response = createUser(userProperties.get("email"), userProperties.get("password"), userProperties.get("name"));
        return getTokenFromResponse(response);
    }

    public static List<String> getTestIngredients() {
        return List.of(VALID_INGREDIENT_1, VALID_INGREDIENT_2);
    }
}
