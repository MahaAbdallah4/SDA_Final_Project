@US04
Feature: Customer Product Page

  Background:
    Given user is logged in as a customer

  @US04_TC01
  Scenario: Verify that the customer can view products on the customer page
    When user navigates to the customer page
    Then products should be visible on the page