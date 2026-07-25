package com.booking.stepdefinitions;

import com.booking.stepdefinitions.config.BookingContext;
import com.booking.stepdefinitions.config.BookingPayload;
import com.booking.stepdefinitions.utils.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.internal.common.assertion.AssertionSupport;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetMessages {
    private Response response;
    private static final ApiClient api = new ApiClient();
    private final ObjectMapper mapper = new ObjectMapper();


    @Before
    public void setup() {
        this.response = null;
    }


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
        response = api.post(endpoint, payload.asMaps(String.class, String.class).get(0));
    }

    @And("the response body must have valid token")
    public void theResponseBodyMustHaveValidToken() {
        Assertions.assertNotNull(response.path("token"));
        String extractedToken = response.jsonPath().getString("token");
        BookingContext.setToken(extractedToken);
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

    @When("the user sends a GET request to the path {string} with {string}")
    public void theUserSendsAGETRequestToThePathWith(String endPoint, String roomId) {
        response = api.get("/room/" + roomId);
    }

    @Then("the response body must have features {string}, roomName {string}, price {int}, and type {string}")
    public void theResponseBodyMustHaveRoomNameRoomPriceAndType(String features, String roomname, int roomprice, String type) {
        List<String> expectedFeatures = Arrays.asList(features.split(","));
        Assertions.assertEquals(expectedFeatures, response.jsonPath().getList("features"));
        Assertions.assertEquals(roomname, response.path("roomName"));
        Assertions.assertEquals(roomprice, response.jsonPath().getInt("roomPrice"));
        Assertions.assertEquals(type, response.path("type"));
    }

    @When("the user sends a POST request to the path {string} with valid booking request")
    public void theUserSendsAPOSTRequestToThePathWithValidBookingRequest(String endPoint, DataTable payLoad) {
        Map<String, String> row = payLoad.asMaps(String.class, String.class).get(0);
            BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
            response=api.post(endPoint,payload);
        }


    @And("the response body must have errors message")
    public void theResponseBodyMustHaveErrorsMessage() {
        List<String> errorMessages = response.jsonPath().getList("errors");
        assertTrue(
                errorMessages.contains("must be greater than or equal to 1"),
                "Expected error message missing! Actual errors found: " + errorMessages
        );
    }

    @And("the response body must have bookingId")
    public void theResponseBodyMustHaveBookingId() {
        Object rawBookingId = response.path("bookingid");
        Assertions.assertNotNull(rawBookingId);
        BookingContext.setBookingId(rawBookingId);
    }

    @When("the user sends a POST request to the path {string} with invalid booking firstname")
    public void theUserSendsAPOSTRequestToThePathWithInvalidBookingFirstname(String endPoint,DataTable payLoad) {
        Map<String, String> row = payLoad.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.post(endPoint,payload);
    }

    @And("the response body must have errors message {string}")
    public void theResponseBodyMustHaveErrorsMessage(String error) {
        List<String> errors = response.jsonPath().getList("errors");
        assertTrue(errors.contains(error),
                "Expected message missing from errors list! Found: " + errors);
    }

    @When("the user sends a POST request to the path {string} with invalid booking lastname")
    public void theUserSendsAPOSTRequestToThePathWithInvalidBookingLastname(String endPoint,DataTable payLoad) {
        Map<String, String> row = payLoad.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.post(endPoint,payload);
    }

    @When("the user sends a POST request to the path {string} with invalid booking email")
    public void theUserSendsAPOSTRequestToThePathWithInvalidBookingEmail(String endPoint,DataTable payLoad) {
        Map<String, String> row = payLoad.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.post(endPoint,payload);
    }

    @When("the user sends a POST request to the path {string} with invalid booking phoneNumber")
    public void theUserSendsAPOSTRequestToThePathWithInvalidBookingPhoneNumber(String endPoint,DataTable payLoad) {
        Map<String, String> row = payLoad.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.post(endPoint,payload);
    }

    @When("the user sends a GET request to the path {string} for existing bookingId")
    public void theUserSendsAGETRequestToThePathForExistingBookingId(String endpoint) {
        String bookingId = BookingContext.getBookingId();
        if ("0".equals(bookingId)) {
            throw new IllegalStateException("Test context is empty! Execute booking creation first.");
        }
        response = api.get(endpoint + "/" + bookingId);
    }

    @And("the response body must have all the booking details")
    public void theResponseBodyMustHaveAllTheBookingDetails(DataTable expResponseBody) {
        Map<String, String> expected = expResponseBody.asMaps(String.class, String.class).get(0);
        Assertions.assertEquals(Integer.parseInt(expected.get("roomid")),response.jsonPath().getInt("roomid"),"Room ID mismatch!");
        Assertions.assertEquals(expected.get("firstname"),response.jsonPath().getString("firstname"),"Firstname mismatch!");
        Assertions.assertEquals(expected.get("lastname"),response.jsonPath().getString("lastname"),"Lastname mismatch!");
        Assertions.assertEquals(expected.get("bookingdates.checkin"),response.jsonPath().getString("bookingdates.checkin"),"Check-in date mismatch!");
        Assertions.assertEquals(expected.get("bookingdates.checkout"),response.jsonPath().getString("bookingdates.checkout"),"Check-out date mismatch!");
    }

    @When("the user sends a PUT request to the path {string} for existing bookingId")
    public void theUserSendsAPUTRequestToThePathForExistingBookingId(String endPoint,DataTable newPayload) {
        Map<String, String> row = newPayload.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.put(endPoint,payload);
    }

    @When("the user sends a GET request to the path {string} for existing bookingId with invalid token")
    public void theUserSendsAGETRequestToThePathForExistingBookingIdWithInvalidToken(String endpoint) {
        String bookingId = BookingContext.getBookingId();
        if ("0".equals(bookingId)) {
            throw new IllegalStateException("Test context is empty! Execute booking creation first.");
        }
        response = api.get(endpoint + "/" + bookingId,"123456abc");
    }

    @When("the user sends a PUT request to the path {string} for existing bookingId with invalid token")
    public void theUserSendsAPUTRequestToThePathForExistingBookingIdWithInvalidToken(String endPoint,DataTable newPayload) {
        Map<String, String> row = newPayload.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.put(endPoint,payload,"abc123456");
    }

    @When("the user sends a PATCH request to the path {string} for existing bookingId")
    public void theUserSendsAPATCHRequestToThePathForExistingBookingId(String endPoint,DataTable patchPayload) {
        Map<String, String> row = patchPayload.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.put(endPoint,payload);
    }

    @When("the user sends a PATCH request to the path {string} for existing bookingId with invalid token")
    public void theUserSendsAPATCHRequestToThePathForExistingBookingIdWithInvalidToken(String endPoint,DataTable newPayload) {
        Map<String, String> row = newPayload.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.put(endPoint,payload,"abc123456");
    }

    @When("the user sends a DELETE request to the path {string} for existing bookingId")
    public void theUserSendsADELETERequestToThePathForExistingBookingId(String endpoint) {
        String bookingId = BookingContext.getBookingId();
        response=api.delete(endpoint+ "/" + bookingId);
    }

    @And("the response body must have message {string}")
    public void theResponseBodyMustHaveMessage(String message) {
        Assertions.assertEquals(message,response.path("message"));
    }

    @When("the user sends a DELETE request to the path {string} for existing bookingId with invalid token")
    public void theUserSendsADELETERequestToThePathForExistingBookingIdWithInvalidToken(String endPoint) {
        String bookingId = BookingContext.getBookingId();
        response=api.delete(endPoint+ "/" + bookingId,"abc123456");
    }

    @When("the user sends a POST request to the path {string} with Checkout date before checkin date")
    public void theUserSendsAPOSTRequestToThePathWithCheckoutDateBeforeCheckinDate(String endPoint, DataTable newPayload) {
        Map<String, String> row = newPayload.asMaps(String.class, String.class).get(0);
        BookingPayload payload = mapper.convertValue(row, BookingPayload.class);
        response=api.post(endPoint,payload);
    }

    @And("the response body have errors message {string}")
    public void theResponseBodyHaveErrorsMessage(String error) {
        Assertions.assertEquals(error,response.path("error"));
    }

    @When("user sends a GET request to the {string} with a specific checkin and checkout dates")
    public void userSendsAGETRequestToTheWithASpecificCheckinAndCheckoutDates(String endpoint,DataTable dates) {
        Map<String, String> queryParams = dates.asMaps(String.class, String.class).get(0);
        response = api.get(endpoint,BookingContext.getToken(),queryParams);
    }

    @And("user must see the available rooms in response")
    public void userMustSeeTheAvailableRoomsInResponse() {
        Assertions.assertNotNull(response.path("rooms"));
        java.util.List<Object> roomsList = response.path("rooms");
        Assertions.assertFalse(roomsList.isEmpty(), "The rooms array should not be empty!");
    }

    @When("the user sends a POST request to the path {string}")
    public void theUserSendsAPOSTRequestToThePath(String endPoint,DataTable message) {
        Map<String, String> row = message.asMaps(String.class, String.class).get(0);
        response=api.post(endPoint,row);
    }

    @And("the response body must have success : true")
    public void theResponseBodyMustHaveSuccessTrue() {
        Assertions.assertEquals(true,response.path("success"));
    }

    @And("the response body must have error message {string} and {string}")
    public void theResponseBodyMustHaveErrorMessageAnd(String error1, String error2) {
        List<String> errors = response.jsonPath().getList("$", String.class);
        Assertions.assertTrue(errors.contains(error1));
        Assertions.assertTrue(errors.contains(error2));
    }

    @And("the response must have error message {string}")
    public void theResponseMustHaveErrorMessage(String error) {
        List<String> errors = response.jsonPath().getList("$", String.class);
        Assertions.assertEquals(error, errors.get(0));
    }

    @When("the user sends a POST request to the path {string} with generated tokenid")
    public void theUserSendsAPOSTRequestToThePathWithGeneratedTokenid(String endpoing) {
        response=api.post(endpoing);
    }

    @And("the response must have valid as true")
    public void theResponseMustHaveValidAsTrue() {
        Assertions.assertEquals(true,response.path("valid"));
    }

    @And("the response must have valid as error {string}")
    public void theResponseMustHaveValidAsErrorInvalidToken(String error) {
        Assertions.assertEquals(error,response.path("error"));
    }

    @When("the user sends a POST request to the path {string} with room details")
    public void theUserSendsAPOSTRequestToThePathWithRoomDetails(String endpoint, DataTable roomDetails) {
        Map<String, String> rawData = roomDetails.asMaps(String.class, String.class).get(0);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("roomName", rawData.get("roomName"));
        requestBody.put("type", rawData.get("type"));
        requestBody.put("accessible", Boolean.parseBoolean(rawData.get("accessible")));
        requestBody.put("description", rawData.get("description"));
        requestBody.put("image", rawData.get("image"));
        requestBody.put("roomPrice", Integer.parseInt(rawData.get("roomPrice")));
        List<String> featureList = Arrays.asList(rawData.get("features").split(",\\s*"));
        requestBody.put("features", featureList);
        
        response=api.post(endpoint,requestBody,BookingContext.getToken());
    }

    @And("the response must have success true")
    public void theResponseMustHaveSuccessTrue() {
        Assertions.assertEquals(true,response.path("success"));
    }

    @And("the response must have error {string}")
    public void theResponseMustHaveError(String error) {
        List<String> errors = response.path("errors");
        Assertions.assertTrue(errors.contains(error));
    }

    @And("the response must have booking details for all rooms")
    public void theResponseMustHaveBookingDetailsForAllRooms() {
        Assertions.assertNotNull(response.path("report"));
        java.util.List<Object> reportsList = response.path("report");
        Assertions.assertFalse(reportsList.isEmpty(), "No rooms are booked and nothing to show!");

    }

    @And("the response must have details of the hotel with location and address etc")
    public void theResponseMustHaveDetailsOfTheHotelWithLocationAndAddressEtc() {
        Assertions.assertNotNull(response.body());
        Map<String, Object> address = response.jsonPath().getMap("address");
        Map<String, Object> contact = response.jsonPath().getMap("contact");

        Assertions.assertEquals("Shady Meadows B&B", response.path("name"));
        Assertions.assertEquals("Dilbery", address.get("county"));
        Assertions.assertEquals("Shady Meadows B&B", address.get("line1"));
        Assertions.assertEquals("N1 1AA", address.get("postCode"));
        Assertions.assertEquals("fake@fakeemail.com", contact.get("email"));
        Assertions.assertEquals("012345678901", contact.get("phone"));

    }

    @And("the response must have message sent by users")
    public void theResponseMustHaveMessageSentByUsers() {
        Assertions.assertNotNull(response.path("messages"));
        java.util.List<Object> reportsList = response.path("messages");
        Assertions.assertFalse(reportsList.isEmpty(), "No Messages to display!");
    }
}
