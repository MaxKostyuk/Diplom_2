package com.kotan4ik.requests;

import com.kotan4ik.models.ErrorResponse;
import com.kotan4ik.models.SuccessfulCreateLoginResponse;
import com.kotan4ik.models.User;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class UserApiMethods extends BaseRequest {
    private static final String REGISTER_BASE = "auth/register";
    private static final String USER_BASE = "auth/user";

    @Step("Sending createUser request with email = {email}, password = {password}, name = {name}")
    public static Response createUser(String email, String password, String name) {
        User user = new User(email, password, name);

        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(user)
                .post(REGISTER_BASE);
    }

    @Step("Deleting user with token = {token}")
    public static Response deleteUser(String token) {
        return RestAssured.given()
                .auth()
                .oauth2(token)
                .delete(USER_BASE);
    }

    @Step("Parsing response to get token")
    public static String getTokenFromResponse(Response response) {
        SuccessfulCreateLoginResponse responseObject = assertDoesNotThrow(() -> fromJson(response.asString(), SuccessfulCreateLoginResponse.class),
                "JSON response has incompatible structure");
        String tokenString = responseObject.getAccessToken();
        return tokenString.split(" ")[1];
    }

    @Step("Checking create user or login response is valid")
    public static void checkCreateLoginResponse(Response response) {
        SuccessfulCreateLoginResponse responseObject = assertDoesNotThrow(() -> fromJson(response.asString(), SuccessfulCreateLoginResponse.class),
                "JSON response has incompatible structure");
        assertAll(() -> assertTrue(responseObject.isSuccess(), "Success field should be true"),
                () -> assertNotNull(responseObject.getUser(), "User shouldn't be null"),
                () -> assertNotNull(responseObject.getUser().getName(), "User name shouldn't be null"),
                () -> assertNotNull(responseObject.getUser().getEmail(), "User email shouldn't be null"),
                () -> assertNull(responseObject.getUser().getPassword(), "Password should be null"),
                () -> assertNotNull(responseObject.getAccessToken(), "Response should have access token"),
                () -> assertNotNull(responseObject.getRefreshToken(), "Response should have refresh token"));
    }

    @Step("Checking error response is valid and has message {errorMessage}")
    public static void checkErrorResponse(Response response, String errorMessage) {
        ErrorResponse responseObject = assertDoesNotThrow(() -> fromJson(response.asString(), ErrorResponse.class),
                "JSON response has incompatible structure");
        assertAll(() -> assertFalse(responseObject.isSuccess(), "Success field should be false"),
                () -> assertNotNull(responseObject.getMessage(), "Error message shouldn't be null"),
                () -> assertEquals(errorMessage, responseObject.getMessage(), "Received error message is equal to expected"));
    }
}
