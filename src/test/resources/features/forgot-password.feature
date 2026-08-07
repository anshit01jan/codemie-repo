@Functional
Feature: Forgot Password


  @regression
  Scenario: Forgot Password modal displays and can send reset link (TC-FP-001)
    Given the user is on the Login page
    When the user clicks on the "Forgot Password?" link
    Then the Forgot Password modal should be displayed
    And the Forgot Password modal should be displayed with title "Forgot Password"
    When the user enters email "test@example.com"
    And the user clicks on the "Send Reset Link" button
    Then the user should see a success message "Reset link sent"

  @regression
  Scenario: Forgot Password invalid email shows validation error (TC-FP-002)
    Given the user is on the Login page
    When the user clicks on the "Forgot Password?" link
    And the user enters email "invalid-email"
    And the user clicks on the "Send Reset Link" button
    Then the user should see a validation error for the Email field

  @regression
  Scenario: Forgot Password modal resets on reopen (TC-FP-003)
    Given the user is on the Login page
    When the user clicks on the "Forgot Password?" link
    And the user enters email "test@example.com"
    And the user clicks on the "Send Reset Link" button
    And the user closes the Forgot Password modal
    When the user opens the Forgot Password modal again
    Then the Email field should be empty
    And the Forgot Password alert container should be empty
