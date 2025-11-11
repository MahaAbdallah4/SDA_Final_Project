
@US06
Feature: US06 - Add to Cart functionality

  Background:
    Given user is logged in as a customer

  @UI

  Scenario: TC06-01 Validate “Add to Cart” functionality from UI
    When user clicks the "Add to Cart" button for product "E-Book Reader"
    Then product "E-Book Reader" should be added successfully and cart count should update

  @API
  Scenario: TC06-02 Validate “Add to Cart” via API
    When user sends POST request to add product with ID 211 to cart
    Then API should return success and product should appear in cart

  @API
  Scenario: TC06-03 Add invalid product ID to cart
    When user sends POST request to add product with ID 9999 to cart
    Then API should return error for invalid product ID
