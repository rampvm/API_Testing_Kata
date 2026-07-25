package com.booking.stepdefinitions.utils;

import com.booking.stepdefinitions.config.BookingContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {

    public Response get(String endpoint) {
        String token = BookingContext.getToken();
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .get(endpoint);
    }

    //For passing invalid Token
    public Response get(String endpoint,String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .get(endpoint);
    }

    public Response get(String endpoint, String token, Map<String, String> queryParams) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .queryParams(queryParams)
                .get(endpoint);
    }

    public Response post(String endpoint) {
        String jsonBody = String.format("{\"token\":\"%s\"}", BookingContext.getToken());
        return given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .post(endpoint);
    }
    public Response post(String endpoint, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(endpoint);
    }

    public Response post(String endpoint, Object body, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(body)
                .post(endpoint);
    }

    public Response put(String endpoint, Object body) {
        String token = BookingContext.getToken();
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(body)
                .post(endpoint);
    }

    public Response put(String endpoint, Object body, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(body)
                .post(endpoint);
    }

    public Response patch(String endpoint, Object body) {
        String token = BookingContext.getToken();
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(body)
                .post(endpoint);
    }

    public Response delete(String endpoint) {
        String token = BookingContext.getToken();
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .delete(endpoint);
    }

    public Response delete(String endpoint,String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .delete(endpoint);
    }
}