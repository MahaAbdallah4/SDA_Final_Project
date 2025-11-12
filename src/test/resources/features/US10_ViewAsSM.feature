@US10
Feature: US10 - Product Catalog

  Background:
    Given the user is logged in as a Store Manager

  @UI
  Scenario: TC10-01 Validate catalog view for store manager
    When the user goes to the Menu and clicks Products
    Then all products should be displayed with correct name, stock, and price columns
  @API
  Scenario: TC10-02 Validate catalog data via API
    Given the API endpoint "/api/products" is accessible
    When the user sends a GET request as a Manager
    Then the API should return all products with correct details
