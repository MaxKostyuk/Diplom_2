package com.kotan4ik.requests;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;

public class BaseRequest {
    private static ObjectMapper mapper;

    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    protected static <T> T fromJson(String jsonString, Class<T> classOfT) throws Exception {
        return mapper.readValue(jsonString, classOfT);
    }
}
