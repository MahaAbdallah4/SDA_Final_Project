@DeleteProduct
Feature: Delete a product from the catalog as a store manager

  Scenario: Successfully delete a product from the catalog
    Given the store manager is logged in
    And the product "book" exists in the catalog
    When the store manager clicks the delete button for "book"
    And confirms the deletion
    Then "book" should no longer appear in the product catalog
    



  Scenario: Cancel deleting a product from the catalog
    Given the store manager is logged in
    And the product "T-Shirt" exists in the catalog
    When the store manager clicks the delete button for "T-Shirt"
    And cancels the deletion
    Then "T-Shirt" should still appear in the product catalog
