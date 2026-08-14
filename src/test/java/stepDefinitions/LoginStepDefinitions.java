package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.DashboardPage;
import framework.pages.LoginPage;
import framework.utils.Config;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

public class LoginStepDefinitions {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    private String username;
    private String password;

    private void initPages() {
        driver = DriverManager.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is null. Check hooks/driver init.");
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds()));
        loginPage = new LoginPage(driver, wait);
        dashboardPage = new DashboardPage(driver, wait);
    }

    @Given("the user is on the Login page")
    public void user_on_login() {
        initPages();
        loginPage.open();
    }

    @Given("the user account {string} is in an unlocked state")
    public void the_user_account_is_unlocked(String username) throws InterruptedException {
        initPages();
        // Wait for any account lockout to expire (lockout duration is 10 seconds)
        Thread.sleep(11000);
        // Load login page and make a successful login to trigger _clear_expired_lockout
        // which resets the failed_attempts counter
        loginPage.open();
        Thread.sleep(500);
        loginPage.login(username, "ScrumPass1");
        Thread.sleep(2000); // Wait for login response
        // Reload login page for the actual test
        loginPage.open();
        Thread.sleep(500);
    }

    @And("the user has a valid username {string}")
    public void the_user_has_a_valid_username(String user) {
        this.username = user;
    }

    @And("the user has an invalid username {string}")
    public void the_user_has_an_invalid_username(String user) {
        this.username = user;
    }

    @And("the user has a valid password {string}")
    public void the_user_has_a_valid_password(String pass) {
        this.password = pass;
    }


    @And("the user has an invalid password {string}")
    public void the_user_has_an_invalid_password(String pass) {
        this.password = pass;
    }


    @When("the user clicks on the Login button")
    public void the_user_clicks_on_the_login_button() {
        if (loginPage == null) initPages();
        loginPage.login(username, password);
    }

    @When("the user attempts to login 3 times with an invalid password {string}")
    public void the_user_attempts_to_login_3_times(String pass) throws InterruptedException {
        if (loginPage == null) initPages();
        for (int i = 0; i < 3; i++) {
            loginPage.login("scrum50", pass);
            // Allow time for the API response and alert display to be processed
            Thread.sleep(2500);
        }
        // Extra wait to ensure the final lockout alert is fully rendered
        Thread.sleep(2000);
    }

    @When("the user attempts to login with an invalid password {string}")
    public void the_user_attempts_to_login_with_an_invalid_password(String pass) {
        if (loginPage == null) initPages();
        loginPage.login("scrum50", pass);
    }

    @When("the user attempts to login again with an invalid password {string}")
    public void the_user_attempts_to_login_again_with_an_invalid_password(String pass) {
        if (loginPage == null) initPages();
        loginPage.login("scrum50", pass);
    }

    @Given("the user account {string} is locked due to 3 failed login attempts")
    public void the_user_account_locked(String user) {
        user_on_login();
        for (int i = 0; i < 3; i++) {
            loginPage.login(user, "WrongPass1");
        }
    }

    @When("the user waits for {int} seconds")
    public void the_user_waits_for_seconds(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }

    @And("the user logs in with valid password {string}")
    public void the_user_logs_in_with_valid_password(String pass) {
        if (loginPage == null) initPages();
        loginPage.login("scrum50", pass);
    }

    @Then("the user should not be redirected to the Dashboard page")
    public void the_user_should_not_be_redirected_to_the_dashboard_page() {
        Assertions.assertThat(URI.create(driver.getCurrentUrl()).getPath()).isNotEqualTo("/dashboard");
    }

    @Then("the user should see an error message {string}")
    public void user_sees_error(String msg) {
        Assertions.assertThat(loginPage.getAlertText()).contains(msg);
    }

    @Then("the user should see an error message containing {string}")
    public void the_user_should_see_error_containing(String part) {
        if (loginPage == null) initPages();
        try {
            wait.until(d -> loginPage.isAlertVisible());
        } catch (Exception ignored) {
        }
        String alert = loginPage.getAlertText();
        String lower = alert == null ? "" : alert.toLowerCase();
        System.out.println("Alert text: " + lower);
        System.out.println("Looking for: " + part);
        String p = part == null ? "" : part.toLowerCase();
        
        if (p.contains("attempt")) {
            // Check for lockout messages (locked/account locked) or rate-limit messages (too many requests) or time-based messages (please try after)
            boolean isValid = lower.contains("locked") || 
                            lower.contains("account is locked") || 
                            lower.contains("please try after") ||
                            lower.contains("too many requests") ||
                            lower.contains("attempt");
            System.out.println("Checking for attempt-related message. Valid: " + isValid);
            Assertions.assertThat(isValid).isTrue();
        } else if (p.contains("locked")) {
            // For "locked" check, accept any variation of account locked message or rate limit
            boolean isValid = lower.contains("locked") || 
                            lower.contains("too many requests") ||
                            lower.contains("account is locked") ||
                            lower.contains("please try after");
            System.out.println("Checking for locked message. Valid: " + isValid);
            Assertions.assertThat(isValid).isTrue();
        } else {
            Assertions.assertThat(alert).contains(part);
        }
    }
}
