package com.kotan4ik.requests;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;

public class BaseRequest {
    private static ObjectMapper mapper;

    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.registerModule(new JavaTimeModule());
    }

    protected static <T> T fromJson(String jsonString, Class<T> classOfT) throws Exception {
        return mapper.readValue(jsonString, classOfT);
    }
}
