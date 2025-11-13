@AdminDeleteStoreFeature
Feature: Admin Delete Store Functionality

  Background:
    Given Logged in and store exists

  #TC_US21_001 – Validate delete confirmation dialog appears
  @US21 @ConfirmDelete
  Scenario: TC_US21_001 Validate delete confirmation dialog appears
    When Admin navigates to the store list
    And Admin clicks delete
    Then a confirmation dialog appears

#TC_US21_002 – Validate store is removed upon confirmation
  @US21 @ValidDelete
  Scenario: TC_US21_002 Validate store removed if deletion is confirmed
    When Admin navigates to the store list
    And Admin clicks delete
    And Admin confirms deletion
    Then store is removed and no longer appears in the list

#TC_US21_003 – Validate store remains if deletion is canceled
  @US21 @ValidDeleteCancel
  Scenario: TC_US21_003 Validate store remains if deletion is canceled
    When Admin navigates to the store list
    And Admin clicks delete
    And Admin cancels deletion
    Then store remains in the list after cancellation