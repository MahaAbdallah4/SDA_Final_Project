@Products
Feature: Manage products in BazaarStore

  Scenario: Successfully edit a product
    Given the store manager is on the "Products" page
    When the store manager clicks edit for product "T-shirt"
    And the store manager updates the product price to "30"
    And the store manager updates the product stock to "17"
    And the store manager clicks Submit
    Then the product should be updated successfully

  Scenario: Try to edit a product with empty name
    Given the store manager is on the "Products" page
    When the store manager clicks edit for product "T-shirt"
    And the store manager updates the product name to ""
    And the store manager clicks Submit
    Then an error message should be displayed for invalid or missing fields
