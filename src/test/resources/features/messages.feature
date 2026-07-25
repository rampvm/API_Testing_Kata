@message
Feature: Validating the Booking.com for all the available api for the endpoint "https://automationintesting.online/api"

  @health
  Scenario: Verify the application health check endpoint
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/booking/actuator/health"
    Then response status code should be 200
    And the response body must have status "UP"

  @sanity
  @regression
  @e2e
  Scenario: Verify successful authentication for valid credentials will generate token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/auth/login" with valid credentials
    |username |password |
    |admin    |password |
    Then response status code should be 200
    And the response body must have valid token

  @regression
  Scenario: Verify unsuccessful authentication with invalid credentials will generate error message
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/auth/login" with invalid credentials
      |username |password |
      |admin    |test123  |
    Then response status code should be 401
    And the response body must have error message "Invalid credentials"

  @sanity
  @regression
  Scenario: Verify the rooms exist for booking from the booking.com application
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/room"
    Then response status code should be 200
    And the response body must have all the rooms available.
    |roomId|roomName|roomPrice|type  |
    |  1   | 101    | 100     |Single|
    |  2   | 102    | 150     |Double|
    |  3   | 103    | 225     |Suite |

  @regression
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

  @regression
  Scenario: Verify the availability of rooms for a given dates with a valid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When user sends a GET request to the "/room" with a specific checkin and checkout dates
      | checkin         |     checkout     |
      | 2026-07-23      |    2026-07-24    |
    Then response status code should be 200
    And user must see the available rooms in response

  @sanity
  @regression
  @e2e
  Scenario: Verify the booking id for the booking endpoint when sending booking details with valid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with valid booking request
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|  email           |   phone        |
      |  1   |  123456            |    abcdefghijklmnop|           |    2026-07-23      |    2026-07-24       | 123@email.com    |  123456789012  |
    Then response status code should be 201
    And the response body must have bookingId

  @sanity
  @regression
  Scenario: Verify the exist booking from the booking.com application with valid auth token and booking id
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/booking" for existing bookingId
    Then response status code should be 200
    And the response body must have all the booking details
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  123456            |    abcdefghijklmnop|           |    2026-07-23      |    2026-07-24       | 123@email.com    |  123456789012  |

  @sanity
  @regression
  Scenario: Verify the updation of the exist booking from the booking.com application with valid auth token and booking id
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a PUT request to the path "/booking" for existing bookingId
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  updatefirstname   |    updatelastname  |           |    2026-07-23      |    2026-07-24       | 1234@email.com    |  98745632198  |
    Then response status code should be 200
    And the response body must have all the booking details
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  updatefirstname   |    updatelastname  |           |    2026-07-23      |    2026-07-24       | 1234@email.com    |  98745632198  |

  @sanity
  @regression
  Scenario: Verify the partial update of the exist booking from the booking.com application with valid auth token and booking id
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a PATCH request to the path "/booking" for existing bookingId
      |  firstname         |    lastname        |depositpaid|
      |  updatefirstname   |    updatelastname  |   true    |
    Then response status code should be 200
    And the response body must have all the booking details

  @regression
  Scenario: Verify the error messages for the booking end point when sending invalid firstname
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking firstname
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  1                 |    abcdefghij      |           |    2026-07-23      |    2026-07-24       | 123@email.com    |  12345678901   |
    Then response status code should be 400
    And the response body must have errors message "size must be between 3 and 18"

  @regression
  Scenario: Verify the error messages for the booking end point when sending invalid lastname
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking lastname
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  abcdefghij        |    1               |           |    2026-07-23      |    2026-07-24       | 123@email.com    |  12345678901   |
    Then response status code should be 400
    And the response body must have errors message "size must be between 3 and 30"

  @regression
  Scenario: Verify the error messages for the booking end point when sending invalid email
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking email
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  abcdefghij        |    abcdefghij      |           |    2026-07-23      |    2026-07-24       | 123              |  12345678901   |
    Then response status code should be 400
    And the response body must have errors message "must be a well-formed email address"

  @regression
  Scenario: Verify the error messages for the booking end point when sending invalid phoneNumber
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with invalid booking phoneNumber
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  abcdefghij        |    abcdefghij      |           |    2026-07-23      |    2026-07-24       | 123@email.com    |  0123          |
    Then response status code should be 400
    And the response body must have errors message "size must be between 11 and 21"

  @regression
  Scenario: Verify the error messages for the booking end point when sending Checkout date before checkin date
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/booking" with Checkout date before checkin date
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  123456789456123   |    123456789456123 |    false  |    2026-07-25      |    2026-07-24       | 123@email.com    |  12345678912   |
     #Response code i am getting 409 not 400 (may be not defined in sandbox)
    Then response status code should be 409
    And the response body have errors message "Failed to create booking"

  @regression
  Scenario: Verify the return code when retrieve booking from the booking.com application with invalid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/booking" for existing bookingId with invalid token
     #Response code for auth failure i am getting 403 not 401 (may be not defined in sandbox)
    Then response status code should be 403

  @regression
  Scenario: Verify the return code when update exist booking from the booking.com application with invalid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a PUT request to the path "/booking" for existing bookingId with invalid token
      |roomid|  firstname         |    lastname        |depositpaid|bookingdates.checkin|bookingdates.checkout|email             |phone           |
      |  1   |  updatefirstname   |    updatelastname  |           |    2026-07-23      |    2026-07-24       | 1234@email.com   |  98745632198  |
     #Response code for auth failure i am getting 403 not 401 (may be not defined in sandbox)
    Then response status code should be 403

  @regression
  Scenario: Verify the return code when partial update of the exist booking from the booking.com application with invalid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a PATCH request to the path "/booking" for existing bookingId with invalid token
      |  firstname         |    lastname        |depositpaid|
      |  updatefirstname   |    updatelastname  |   true    |
     #Response code for auth failure i am getting 403 not 401 (may be not defined in sandbox)
    Then response status code should be 403

  @sanity
  @e2e
  Scenario: Verify the delete of booking from the booking.com application with valid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a DELETE request to the path "/booking" for existing bookingId
     #Response code for delete is 202 not 201 (may be not defined in sandbox)
    Then response status code should be 202
     #no message in the response
    And the response body must have message "Booking deleted successfully"

  @regression
  Scenario: Verify the delete of booking from the booking.com application with invalid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a DELETE request to the path "/booking" for existing bookingId with invalid token
     #Response code for auth failure i am getting 403 not 401 (may be not defined in sandbox)
    Then response status code should be 403

  @regression
  Scenario: Verify the contact form via message for the booking endpoint when sending valid message
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/message"
      |name              |      email           |   phone        |subject     |description                                  |
      |  abcdefghijklmnop|     123@email.com    |  123456789012  |  tests     | sampletext here so we can add any 123456789 |
    Then response status code should be 200
    And the response body must have success : true

  @regression
  Scenario: Verify the contact form via message for the booking endpoint when sending without email
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/message"
      |name              |      email           |   phone        |subject     |description                                  |
      |  abcdefghijklmnop|                      |  123456789012  |  tests     | sampletext here so we can add any 123456789 |
    Then response status code should be 400
    And the response body must have error message "Email must be set" and "Email may not be blank"

  @regression
  Scenario: Verify the contact form via message for the booking endpoint when sending phone number with only 5 numbers
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/message"
      |name              |      email           |   phone        |subject     |description                                  |
      |  abcdefghijklmnop|      123@email.com   |  12345         |  tests     | sampletext here so we can add any 123456789 |
    Then response status code should be 400
    And the response must have error message "Phone must be between 11 and 21 characters."

  @regression
  Scenario: Verify the contact form via message for the booking endpoint when sending subject with less than 5 char
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/message"
      |   name           |      email           |   phone        |subject     |description                                  |
      |  abcdefghijklmnop|      123@email.com   |  123456789012  |  test      | sampletext here so we can add any 123456789 |
    Then response status code should be 400
    And the response must have error message "Subject must be between 5 and 100 characters."

  @regression
  Scenario: Verify the contact form via message for the booking endpoint when sending description with only 10 char
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/message"
      |name              |      email           |   phone        |subject     |description   |
      |  abcdefghijklmnop|      123@email.com   |  123456789012  |  tests     | sampletext   |
    Then response status code should be 400
    And the response must have error message "Message must be between 20 and 2000 characters."

  @extravalidate
  Scenario: Validate the token which is generated on login
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "auth/validate" with generated tokenid
    Then response status code should be 200
    And the response must have valid as true

  @extravalidate
  Scenario: Validate the invalid token which is generate an error
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "auth/validate" with generated tokenid
    Then response status code should be 403
    And the response must have valid as error "Invalid token"

  @extravalidate
  Scenario: Verify new room creation for the booking endpoint when sending the room details for valid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/room" with room details
      |  roomName   |      type     |  accessible   |description                                |image                                              |roomPrice| features    |
      |  105        |      Family   |  true         |  Please enter a description for this room | https://www.mwtestconsultancy.co.uk/img/room1.jpg |300      |WiFi,TV,Radio|
    Then response status code should be 200
    And the response must have success true

  @extravalidate
  Scenario: Verify new room creation for the booking endpoint when sending the wrong details for valid auth token give error
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/room" with room details
      |  roomName   |      type     |  accessible   |description                                |image                                              |roomPrice| features    |
      |             |      Family   |  true         |  Please enter a description for this room | https://www.mwtestconsultancy.co.uk/img/room1.jpg |300      |WiFi,TV,Radio|
    Then response status code should be 400
    And the response must have error "Room name must be set"

  @extravalidate
  Scenario: Verify new room creation for the booking endpoint when sending the wrong details for valid auth token give error
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a POST request to the path "/room" with room details
      |  roomName   |      type     |  accessible   |description                      |image                                              |roomPrice| features    |
      |   106       |               |  true         |  Please enter a description     | https://www.mwtestconsultancy.co.uk/img/room1.jpg |300      |WiFi,TV,Radio|
    Then response status code should be 400
    And the response must have error "Type must be set"

  @extravalidate
  Scenario: Verify the reports on booking from the booking.com application with valid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/report"
    Then response status code should be 200
    And the response must have booking details for all rooms

  @extravalidate
  Scenario: Verify the hotel details of booking.com application with valid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/branding"
    Then response status code should be 200
    And the response must have details of the hotel with location and address etc

  @extravalidate
  Scenario: Verify the message received from users for the booking.com application with valid auth token
    Given the base url of api is "https://automationintesting.online/api"
    When the user sends a GET request to the path "/message"
    Then response status code should be 200
    And the response must have message sent by users

