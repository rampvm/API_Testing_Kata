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


  Scenario: Verify the booking id for the booking end point when sending booking details
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking request
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  123456            |    abcdefghijklmnop|           |    2026-07-23      |    2026-07-24       | 123@email.com    |  123456789012   |
    Then response status code should be 201
    And the response body must have bookingId

  Scenario: Verify the error messages for the booking end point when sending invalid firstname
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking firstname
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  1                 |    abcdefghij      |           |    2026-07-23      |    2026-07-24       | 123@email.com    |  12345678901   |
    Then response status code should be 400
    And the response body must have errors message "size must be between 3 and 18"

  Scenario: Verify the error messages for the booking end point when sending invalid lastname
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking lastname
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  abcdefghij        |    1               |           |    2026-07-23      |    2026-07-24       | 123@email.com    |  12345678901   |
    Then response status code should be 400
    And the response body must have errors message "size must be between 3 and 30"

  Scenario: Verify the error messages for the booking end point when sending invalid email
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking email
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  abcdefghij        |    abcdefghij      |           |    2026-07-23      |    2026-07-24       | 123              |  12345678901   |
    Then response status code should be 400
    And the response body must have errors message "must be a well-formed email address"

  Scenario: Verify the error messages for the booking end point when sending invalid phoneNumber
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking phoneNumber
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  abcdefghij        |    abcdefghij      |           |    2026-07-23      |    2026-07-24       | 123@email.com    |  0123          |
    Then response status code should be 400
    And the response body must have errors message "size must be between 11 and 21"