@US04
Feature: Customer Product Page

  Background:
    Given user is logged in as a customer

  @Smoke @US04_TC01
  Scenario: Verify that the customer can view products on the customer page
    When user navigates to the customer page
    Then products should be visible on the page

  @US04_TC002
  Scenario: Verify that products load quickly and display all required details
    When user refreshes or opens the product page
    Then products should load quickly
    And each product should display correct Name, Price, Description, and Image
    And each product should display valid description without HTML tags