@Functional
Feature: Redirect timing after login


  @regression
  Scenario: Redirect to Dashboard after 1 second delay (TC-RED-001)
    Given the user is on the Login page
    When the user logs in with valid username "scrum50" and valid password "ScrumPass1"
    Then the user should see a success message in the alert container
    When the user waits for 1 second
    Then the user should be redirected to the Dashboard page "/dashboard"
