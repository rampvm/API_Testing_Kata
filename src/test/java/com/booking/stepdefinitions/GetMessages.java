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
        Assertions.assertEquals(response.statusCode(),statusCode);
    }

    @And("the response body must have status {string}")
    public void theResponseBodyMustHaveStatus(String status) {
        Assertions.assertEquals(response.path("status"),status);
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
        Assertions.assertEquals(response.path("error"),"Invalid credentials");
    }

}
