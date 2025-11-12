@US07
Feature: US07 - Favorites functionality

  Background:
    Given user is logged in as a customer

  @UI
  Scenario: TC07-01 Validate marking product as favorite

    When user clicks the heart icon on the product "Flower"
    Then product "Flower" should be added to "My Favorites"
    And the heart icon should turn filled with red

  @UI
  Scenario: TC07-02 Validate removing from favorites
  Given user clicks the heart icon on the product "Jeans"
    When user goes to "My Favorites"
    And clicks the heart icon again on product "Jeans"
    And product "Jeans" should be removed from favorites
    Then the product "Jeans" should no longer appear in My Favorites


  @API
  Scenario: TC07-03 Validate favorites list via API
    Given the API endpoint "/favorites" is active
    When user sends GET request for user ID 355
    Then the added product "Flower" should appear in the API response
    And removal of the product "Jeans" should update the favorites list correctly for user ID 49
