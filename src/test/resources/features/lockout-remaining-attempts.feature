@Functional@smoke
Feature: Account lockout remaining attempts message


  @smoke
  Scenario: Remaining attempts message after 1st and 2nd failed attempts (TC-LOCKM001)
    Given the user is on the Login page
    And the user has a valid username "scrum50"
    When the user attempts to login with an invalid password "WrongPass1"
    Then the user should see an error message containing "attempt(s) remaining"
    When the user attempts to login again with an invalid password "WrongPass1"
    Then the user should see an error message containing "attempt(s) remaining"
