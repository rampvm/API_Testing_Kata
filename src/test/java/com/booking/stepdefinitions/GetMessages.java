package com.booking.stepdefinitions;

import com.booking.stepdefinitions.utils.ApiClient;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import javax.xml.crypto.Data;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetMessages {
    private Response response;
    private final ApiClient api = new ApiClient();

    @Given("the base url of api is {string}")
    public void theBaseUrlOfApiIs(String baseURL) {
        RestAssured.baseURI=baseURL;

    }

    @When("the user sends a GET request to the path {string}")
    public void theUserSendsAGETRequestToThePath(String endpoint) {
        response=api.get(endpoint);
    }

    @Then("response status code should be {int}")
    public void responseStatusCodeShouldBe(int statusCode) {
        Assertions.assertEquals(statusCode,response.statusCode());
    }

    @And("the response body must have status {string}")
    public void theResponseBodyMustHaveStatus(String status) {
        Assertions.assertEquals(status,response.path("status"));
    }

    @When("the user sends a POST request to the path {string} with valid credentials")
    public void theUserSendsAPOSTRequestToThePathWithValidCredentials(String endpoint, DataTable payload) {

        response=api.post(endpoint,payload.asMaps(String.class, String.class).get(0));
    }

    @And("the response body must have valid token")
    public void theResponseBodyMustHaveValidToken() {
        Assertions.assertNotNull(response.path("token"));
    }

    @When("the user sends a POST request to the path {string} with invalid credentials")
    public void theUserSendsAPOSTRequestToThePathWithInvalidCredentials(String endpoint,DataTable payload) {
        response=api.post(endpoint,payload.asMaps(String.class, String.class).get(0));
    }

    @And("the response body must have error message {string}")
    public void theResponseBodyMustHaveErrorMessage(String error) {
        Assertions.assertEquals("Invalid credentials",response.path("error"));
    }

    @And("the response body must have all the rooms available.")
    public void theResponseBodyMustHaveAllTheRoomsAvailable(DataTable roomDetails) {
        List<Map<String, String>> expectedRooms = roomDetails.asMaps(String.class, String.class);
        List<Map<String, Object>> actualRooms = response.jsonPath().getList("rooms");
        for (int i = 0; i < expectedRooms.size(); i++) {
            Assertions.assertEquals(expectedRooms.get(i).get("roomId"), String.valueOf(actualRooms.get(i).get("roomid")));
            Assertions.assertEquals(expectedRooms.get(i).get("roomName"), String.valueOf(actualRooms.get(i).get("roomName")));
            Assertions.assertEquals(expectedRooms.get(i).get("roomPrice"), String.valueOf(actualRooms.get(i).get("roomPrice")));
            Assertions.assertEquals(expectedRooms.get(i).get("type"), String.valueOf(actualRooms.get(i).get("type")));
        }
    }
}
