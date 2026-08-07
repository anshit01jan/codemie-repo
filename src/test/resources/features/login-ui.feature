@Functional
Feature: Login Page UI Validations

  @regression
  Scenario: Verify presence of Username, Password fields and Login button (TC-UI-001)
    Given the user navigates to the Login page
    Then the user should see an input field "Username"
    And the user should see an input field "Password"
    And the user should see a button "Login"
    And the user should see a link "Forgot Password?"
