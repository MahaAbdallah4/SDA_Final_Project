@US09
Feature: US09 - Cart Confirmation

  Background:
    Given user is logged in as a customer

  @UI
  Scenario: TC09-01 Validate confirmation flow
    Given user clicks the "Add to Cart" button for product "E-Book Reader"
    When user clicks “Confirm Cart”
    Then the page should display
      """
      Success!
      Your order has been received successfully.
      """

  @API
  Scenario: TC09-02 Validate confirmation via API
    Given the API endpoint "/api/cart/confirm" is active
    When user sends a POST request to confirm the cart for user ID 601
    Then the API response should return status “confirmed”
