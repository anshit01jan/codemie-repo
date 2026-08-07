@Functional@smoke
Feature: Login


  @ smoke
  Scenario: Successful login with valid credentials (TC-LOG-001)
    Given the user is on the Login page
    And the user has a valid username "scrum50"
    And the user has a valid password "ScrumPass1"
    When the user clicks on the Login button
    Then the user should be redirected to the Dashboard page "/dashboard"

  @regression
  Scenario: Login with invalid credentials displays error (TC-LOG-002)
    Given the user is on the Login page
    And the user has a invalid username "invalid1"
    And the user has a valid password "ScrumPass1"
    When the user clicks on the Login button
    Then the user should see an error message "Err
 "
