@AdminAddStoreFeature
Feature: Add Store
  As an admin,
  I want to be able to add a new store
  So that I can manage the stores in the system

  Background:
    Given Admin is logged in
    When I navigate to the Store page

 #TC_US19_001- Validate new store addition with valid inputs
  @US19 @Positive @AdminAddStore
  Scenario: Admin adds a new store
    And I navigate to the Add Store page
    And I enter the store name as "My New Store"
    And I enter the store description as "A great place to shop"
    And I enter the store location as "123 Example St."
    And I select the admins "Store Manager"
    When I submit the form
    Then store should be added successfully

#TC_US19_002- Validate error for missing fields
  @US19 @Negative @AdminAddMissingFieldsStore
  Scenario: Admin adds a new store
    And I navigate to the Add Store page
    And I enter the store name as "My New Store"
    And I enter the store description as ""
    And I enter the store location as ""
    And I select the admins "Store Manager"
    When I submit the form
    Then an error message appears for missing required fields

#TC_US19_003- Validate the added store via API
  @US19 @Positive @AdminCheckStoreExistence
  Scenario: TC_US19_003 Validate the existence of a store via API
    And I call the API to retrieve the store list
    Then the store "My New Store" should appear in the API response

#TC_US19_004- Validate duplicate store name (Bug)
  @US19 @Negative @AdminAddDuplicateStore
  Scenario: TC_US19_004 Validate duplicate store name
    And I navigate to the Add Store page
    And I enter the store name as "My New Store"
    And I enter the store description as "A great place to shop"
    And I enter the store location as "123 Example St."
    And I select the admins "Store Manager"
    And I submit the form
    Then an error message indicates store name already exists
