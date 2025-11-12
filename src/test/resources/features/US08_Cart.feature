@US08
Feature: US08 - Cart functionality

  Background:
    Given user is logged in as a customer

  @UI
  Scenario: TC08-01 Validate cart icon hover shows “View Cart”
    Given  user opens a product named "Flower"
    And user clicks the "Add to Cart" button for product "Flower"
    When user hovers over the cart icon in the header
    Then “View Cart” should appear
    And clicking it should navigate to the cart page

  @UI
  Scenario: TC08-02 Validate cart contents and total price
    Given the user has added multiple products including
      | Flower |
      | Jeans  |

    Then all added products should appear with correct prices
      | Product Name | Price   |
      | Flower       | $400.00 |
      | Jeans        | $400.00 |


    And the total price should be accurate

  @UI
  Scenario: TC08-03 Validate removing item from cart

    Given user clicks the "Add to Cart" button for product "Flower"
    And user clicks Remove on the product "Flower"
    Then the product "Flower" should be removed from the cart
    And the total price should update instantly

  @UI
  Scenario: TC08-04 Empty cart view
    Given the user has no products added to the cart
    When user goes to the Cart icon
    Then the page should display “Your cart is empty”
