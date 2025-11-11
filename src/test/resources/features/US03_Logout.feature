@Logout
  Feature:Logout Feature
    Background:
      Given user is logged in

       #TC001
    @TC01
    Scenario: Verify that the user can click Log out from the dashboard
      When user clicks on the account avatar icon
      And user selects the "Log out" option from the dropdown
      Then user should be logged out successfully
