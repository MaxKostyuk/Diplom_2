package com.kotan4ik.requests;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kotan4ik.models.ErrorResponse;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class BaseRequest {
    private static final ObjectMapper mapper;

    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.registerModule(new JavaTimeModule());
    }

    protected static <T> T fromJson(String jsonString, Class<T> classOfT) throws Exception {
        return mapper.readValue(jsonString, classOfT);
    }

    @Step("Checking error response is valid and has message {errorMessage}")
    public static void checkErrorResponse(Response response, String errorMessage) {
        ErrorResponse responseObject = assertDoesNotThrow(() -> fromJson(response.asString(), ErrorResponse.class),
                "JSON response has incompatible structure");
        assertAll(() -> assertFalse(responseObject.isSuccess(), "Success field should be false"),
                () -> assertNotNull(responseObject.getMessage(), "Error message shouldn't be null"),
                () -> assertEquals(errorMessage, responseObject.getMessage(), "Received error message is equal to expected"));
    }

    @Step("Checking response code is equal to expected {expCode}")
    public static void checkResponseCode(Response response, int expCode) {
        assertEquals(expCode, response.statusCode(), "Response code isn't equal to expected");
    }
}
