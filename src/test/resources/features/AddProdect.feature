@AddProduct
Feature: Add new product as Store Manager

  Background:
    Given Store Manager is logged into BazaarStores

  Scenario: Validate required field validations
    When Store Manager navigates to "Add Product" page
    And leaves "Name" and "Price" fields empty
    And clicks "Submit"
    Then an error message should appear for missing fields

  Scenario: Validate successful product addition
    When Store Manager navigates to "Add Product" page
    And Store Manager fills all fields with valid data
      | Name  | T_shirt |
      | Price | 30       |
      | Stock | 10      |
      | SKU   | TS023   |
    And clicks "Submit"
    Then product should appear in the catalog
