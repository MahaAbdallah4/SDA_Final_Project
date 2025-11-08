Feature: Update Store Details
  As an Admin,
  I want to update store details,
  So that I can ensure that the information is current and accurate.

  #TC_US20_001 – Validate updating store details with valid inputs
  @AdminEditStore
  Scenario: TC_US20_001 Validate updating store details with valid inputs
    Given Admin is logged in and store exists
    When Admin navigate to the store list
    And Admin select the store to edit
    And Admin enter the store name as "My New Store"
    And Admin enter the store description as "A great place to shop"
    And Admin enter the store location as "123 Example St."
    And Admin select the admins "Store Manager"
    Then store details should be updated successfully

  #TC_US20_002 – Validate changes reflect in the store list
  @AdminConfirmEditStore
  Scenario: TC_US20_002 Validate changes reflect in the store list
    Given the store has been updated
    When Admin navigate to the store list
    Then updated store details should be displayed in the list

  #TC_US20_003 – Validate error for invalid inputs
  @AdminInvalidEditStore
  Scenario: TC_US20_003 Validate error for invalid inputs
    Given Admin is logged in and store exists
    When Admin navigate to the store list
    And Admin select the store to edit
    And Admin clear the required fields
    Then an error message for invalid or missing inputs should appear