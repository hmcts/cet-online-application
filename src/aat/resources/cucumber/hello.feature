Feature: Test Hello

  Scenario: Submit a hello world request
    When a POST request is sent to hello
    Then the response code is 200
    When a GET request is sent to fee
    Then the response code is 200
    And the fee is 240.00
