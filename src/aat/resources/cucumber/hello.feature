Feature: Test Hello

  Scenario: Submit a hello world request
    When a POST request is sent to hello
    Then the response code is 200
