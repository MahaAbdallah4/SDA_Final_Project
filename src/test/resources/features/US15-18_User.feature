@Regression @Admin
Feature: Admin manages users (Add, Edit, Delete)

  Background:
    Given admin is logged in on the Dashboard

  @AddUser @Smoke
  Scenario: Add a new user as an Admin
    When admin navigates to "Users" page
    And admin clicks "Add User"
    And admin fills user form with:
      | name | email        | role     | password  | Password Confirmation |
      | ema  | emm891@test.com | customer | Pass123   | Pass123               |
    And admin submits the user form
    Then system should show success message "User added successfully"
    And the new user "emm891@test.com" should appear in the user list

  @EditUser
  Scenario: Edit an existing user details
    When admin navigates to "Users" page
    And admin opens edit for user "emm891@test.com"
    And admin updates user fields:
      | role |
      | customer |
    And admin saves the changes
    Then system should show success message "User details updated successfully"
    And user "emm891@test.com" should show:
      | role |
      | customer |

  @DeleteUser
  Scenario: Validate deleting user from list
    When admin navigates to "Users" page
    And admin clicks "Delete" beside user "emm891@test.com"
    And admin confirms deletion
    Then system should show error message "You cant delete a admin role users!"
    And user "emm891@test.com" should remain in the list


  @CancelDeleteUser
  Scenario: Validate canceling delete action
    When admin navigates to "Users" page
    And admin clicks "Delete" beside user "emm891@test.com"
    And admin cancels deletion
    Then user "emm891@test.com" should remain in the list


  @View
  Scenario: Validate view operations for stores with Selenium and API
    When Admin navigates to "Stores" section
    Then all stores should be displayed correctly on the page
    And each store should show correct "Name", "ID", and "Location"
    When Admin validates the stores list through API endpoint "/stores"
    Then the API response status code should be 200
    And the API response should match the UI store data