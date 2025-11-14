@DeleteProduct
Feature: Delete a product from the catalog as a store manager

  Scenario: Successfully delete a product from the catalog
    Given the store manager is logged in
    And the product "bag" exists in the catalog
    When the store manager clicks the delete button for "bag"
    And confirms the deletion
    Then "bag" should no longer appear in the product catalog




  Scenario: Cancel deleting a product from the catalog
    Given the store manager is logged in
    And the product "Laptop" exists in the catalog
    When the store manager clicks the delete button for "Laptop"
    And cancels the deletion
    Then "Laptop" should still appear in the product catalog
