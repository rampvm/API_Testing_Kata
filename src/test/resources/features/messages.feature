@message
Feature: Validating the Booking.com for all the available api for the endpoint "https://automationintesting.online/api"

  Scenario: Verify the application health check endpoint
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/booking/actuator/health"
    Then response status code should be 200
    And the response body must have status "UP"

  Scenario: Verify successful authentication for valid credentials will generate token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/auth/login" with valid credentials
    |username |password |
    |admin    |password |
    Then response status code should be 200
    And the response body must have valid token

  Scenario: Verify unsuccessful authentication with invalid credentials will generate error message
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/auth/login" with invalid credentials
      |username |password |
      |admin    |test123  |
    Then response status code should be 401
    And the response body must have error message "Invalid credentials"

  Scenario: Verify the rooms exist for booking from the booking application
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/room"
    Then response status code should be 200
    And the response body must have all the rooms available.
    |roomId|roomName|roomPrice|type  |
    |  1   | 101    | 100     |Single|
    |  2   | 102    | 150     |Double|
    |  3   | 103    | 225     |Suite |

  Scenario Outline: Verify the room details using roomId for booking from the booking application
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/room" with "<roomId>"
    Then response status code should be 200
    And the response body must have features "<features>", roomName "<roomName>", price <roomPrice>, and type "<type>"
    Examples:
      |roomId|roomName|roomPrice|type  |features        |
      |  1   | 101    | 100     |Single|TV,WiFi,Safe    |
      |  2   | 102    | 150     |Double|TV,Radio,Safe   |
      |  3   | 103    | 225     |Suite |Radio,WiFi,Safe |
