@Smoke @Logout
  Feature:Logout Feature
    Background:
      Given user is logged in

       #TC001
    @TC01
    Scenario: Verify that the user can click Log out from the dashboard
      When user clicks on the account avatar icon
      And user selects the "Log out" option from the dropdown
      Then user should be logged out successfully

    @TC02
    Scenario: Verify that logging out redirects the user to the login page
      When user clicks on the account avatar icon
      And user selects the "Log out" option from the dropdown
      Then the user should be redirected to the login page

    @TC03
    Scenario: Verify that the session is securely terminated after logout
      When the user performs the logout action
      And the user clicks the browser Back button
      Then the user should not be able to access the dashboard
