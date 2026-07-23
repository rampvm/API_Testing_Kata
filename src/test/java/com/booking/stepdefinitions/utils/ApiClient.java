package com.booking.stepdefinitions.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {

    public Response get(String endpoint) {
        return given()
                .contentType(ContentType.JSON)
                .get(endpoint);
    }

    public Response post(String endpoint, Map<String, String> body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(endpoint);
    }
}
