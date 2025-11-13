@US05
Feature:US05 - View product details as a customer

  Background:
    Given user is logged in as a customer

  Scenario: TC05-01 - Validate product detail page displays all required information
    When user is on the home page
    Then product detail shows Name, Price, Description, and Image

  Scenario: TC05-02 - Validate product details via API
    When user opens product with ID 211
    Then API returns correct product name and price

  Scenario: TC05-03 - Missing product details display placeholders
    When user opens a product named "TV"
    Then system should display placeholders for missing details
