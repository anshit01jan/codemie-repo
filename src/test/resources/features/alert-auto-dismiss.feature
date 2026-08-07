@Functional
Feature: Login Alert Behavior

  @regression
  Scenario: Auto-dismiss login error alert (TC-ALRT-001)
    Given the user is on the Login page
    When the user has a valid username "scrum50"
    And the user has an invalid password "WrongPass1"
    And the user clicks on the Login button
    Then the user should see an error alert displayed in the alert container
    When the user waits for 10 seconds
    Then the error alert should no longer be visible
