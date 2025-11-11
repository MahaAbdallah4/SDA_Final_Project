@Registration
Feature: Registration Feature

    #TC001
  @TC01_HappyPathRegistration @ApiTest
  Scenario: Registration Happy Path
    Given user goes to homepage
    When user clicks registration link
    And user enters email for sign up "faker"
    And user enters full name for sign up "John Doe"
    And user enters password for sign up
    And user enters confirm password for sign up
    And user clicks the sing up button
    Then user should see success message for registration
    And assert the registration via API

    #TC002
  @TC02_NegativeRegistration
  Scenario: Verify error message when required fields are missing
    Given user goes to homepage
    When user clicks registration link
    And user enters email for sign up "testYaser@gmail.com"
    And user enters full name for sign up ""
    And user enters password for sign up
    And user enters confirm password for sign up
    And user clicks the sing up button
    Then User should see: The name field is required. error message
    And assert the negative registration via API using email "testYaser@gmail.com"

    #TC003
  @TC03_NegativeRegistration
  Scenario: Verify invalid email format
    Given user goes to homepage
    When user clicks registration link
    And user enters email for sign up "invalid_email.com"
    And user enters full name for sign up "John Doe"
    And user enters password for sign up
    And user enters confirm password for sign up
    And user clicks the sing up button
    Then user should see invalid email error message
    And assert the negative registration via API using email "invalid_email.com"

    #TC004
  @TC04_NegativeRegistration
  Scenario: Verify validation for invalid name characters
    Given user goes to homepage
    When user clicks registration link
    And user enters email for sign up "faker"
    And user enters full name for sign up "J@*hn"
    And user enters password for sign up
    And user enters confirm password for sign up
    And user clicks the sing up button
    Then user should see: invalid name message error
    And assert the negative registration invalid name via API using name "J@*hn"

    #TC005
  @TC05_NegativeRegistration
  Scenario: Verify validation for short password
    Given user goes to homepage
    When user clicks registration link
    And user enters email for sign up "faker"
    And user enters full name for sign up "John"
    And user enters short password for sign up "Pass1"
    And user enters confirm password for sign up "Pass1"
    And user clicks the sing up button
    Then user should see: The password field must be at least 6 characters. error message
    And assert the negative registration via API using email "faker"

    #TC06
  @TC06_NegativeRegistration
  Scenario: Verify error message when passwords do not match
    Given user goes to homepage
    When user clicks registration link
    And user enters email for sign up "faker"
    And user enters full name for sign up "John Doe"
    And user enters password for sign up
    And user enters confirm password for sign up "Pass1"
    And user clicks the sing up button
    Then user should see: The password field confirmation does not match. error message
    And assert the negative registration via API using email "faker"

    #TC07
  @TC07_BackendValidation @ApiTest
  Scenario: Verify backend validation of user registration
    Given user has successfully registered via the UI
    When a GET request is sent to the API using the registered email
    Then API should return status code 200
    And response should include the correct Name,and Email