@Functional @smoke
Feature: Login


  @smoke
  Scenario: Successful login with valid credentials (TC-LOG-001)
    Given the user is on the Login page
    And the user has a valid username "scrum50"
    And the user has a valid password "ScrumPass1"
    When the user clicks on the Login button
    Then the user should be redirected to the Dashboard page "/dashboard"

  @regression
  Scenario: Login with invalid credentials displays error (TC-LOG-002)
    Given the user is on the Login page
    And the user has an invalid username "invalid1"
    And the user has a valid password "ScrumPass1"
    When the user clicks on the Login button
    Then the user should see an error message "Invalid username or password"

  @regression
  Scenario: Account lockout after 3 failed attempts (TC-LOG-003)
    Given the user is on the Login page
    When the user attempts to login 3 times with an invalid password "WrongPass1"
    Then the user should see an error message containing "Locked"
    And the user should not reach the Dashboard page

  @regression
  Scenario: Account unlocks after wait and login succeeds (TC-LOG-004)
    Given the user account "scrum50" is locked due to 3 failed login attempts
    When the user waits for 30 seconds
    And the user logs in with valid password "ScrumPass1"
    Then the user should be redirected to the Dashboard page "/dashboard"
