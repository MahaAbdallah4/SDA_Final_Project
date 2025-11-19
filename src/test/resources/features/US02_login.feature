 @Regression @Login
Feature: Login Functionality

  Background:
    Given user will go to homepage

#TC_US02_001-Verify successful login with valid credentials (Existing Accounts)
   @Customer
  Scenario: Successful login with valid customer credentials from config
    When user enters valid customer credentials
    And user clicks login button
    Then user should be logged in successfully

#TC_US02_002-Verify successful login with valid credentials (New Accounts)

  Scenario: Successful login with valid credentials
    When user enters email "customer@sda.com" and password "Password.12345"
    And user clicks login button
    Then user should be logged in successfully

#TC_US02_003-Verify error message with invalid credentials
  @Smoke @Negative
  Scenario: Login with invalid credentials
    When user enters email "invalid@test.com" and password "WrongPassword"
    And user clicks login button
    Then user should see error message
    And user should remain on login page

#TC_US02_004-Verify validation for invalid email format
  @Smoke @Negative
  Scenario: Validation for invalid email format
    When user enters email "john.com" and password "Password.12345"
    And user clicks login button
    Then user should see error message "Please include an '@' in the email address. 'john.com' is missing an '@'."
    And user should remain on login page

#TC_US02_006-Verify API behavior for valid and invalid login attempts
  @API @Smoke
  Scenario: Verify login via API
    When user logs in via API with valid credentials
    Then API should return success status code

  @Smoke @Admin
  Scenario: Successful login as Admin
    When user enters email "admin@sda.com" and password "Password.12345"
    And user clicks login button
    Then admin should be logged in successfully

  @Smoke  @StoreManager
  Scenario: Successful login as Store Manager
    When user enters email "storemanager@sda.com" and password "Password.12345"
    And user clicks login button
    Then admin should be logged in successfully

#TC_US02_005-Verify validation for missing required fields
  @Smoke @Negative
  Scenario: Login with empty email
    When user enters email "" and password "Password.12345"
    And user clicks login button
    Then user should see empty "email" error message

  @Negative
  Scenario: Login with empty password
    When user enters email "customer@sda.com" and password ""
    And user clicks login button
    Then user should see empty "password" error message