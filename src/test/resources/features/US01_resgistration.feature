@Registration
Feature: Registration Feature

    #TC001
  @TC01_HappyPathRegistration
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