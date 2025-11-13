@Users
Feature: Admin can view and search all users

  Scenario: View all users
    Given Admin is logged in
    When Admin clicks on "Users" menu
    Then All users should be displayed

  Scenario: Search for existing user by email
    Given Admin is logged in
    When Admin clicks on "Users" menu
    And Admin searches for email "admin@sda.com"
    Then User with email "admin@sda.com" should be displayed


  Scenario: Search for non-existing user by email
    Given Admin is logged in
    When Admin clicks on "Users" menu
    And Admin searches for email "test@unknown.com"
    Then Message "No user found" should be displayed

