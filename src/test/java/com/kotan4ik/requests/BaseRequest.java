package com.kotan4ik.requests;

import io.restassured.RestAssured;

public class BaseRequest {
    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";
    }
}
