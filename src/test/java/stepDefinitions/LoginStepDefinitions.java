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

    @When("the user clicks on the \"Forgot Password?\" link")
    public void the_user_clicks_forgot_link() {
        loginPage.clickForgotPassword();
    }

    @Then("the Forgot Password modal should be displayed")
    public void the_fp_modal_should_be_displayed() {
        Assertions.assertThat(loginPage.isForgotPasswordModalVisible()).isTrue();
    }

    @Then("the Forgot Password modal should be displayed with title {string}")
    public void the_fp_modal_title(String title) {
        Assertions.assertThat(loginPage.getForgotPasswordTitle()).isEqualTo(title);
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
        loginPage.login(username, password);
    }

    @When("the user attempts to login 3 times with an invalid password {string}")
    public void the_user_attempts_to_login_3_times(String pass) {
        for (int i = 0; i < 3; i++) {
            loginPage.login("scrum50", pass);
        }
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
        loginPage.login("scrum50", pass);
    }

    @Then("the user should be redirected to the Dashboard page {string}")
    public void the_user_should_be_redirected_to_the_dashboard_page(String path) {
        Assertions.assertThat(dashboardPage.getPath()).isEqualTo(path);
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
        Assertions.assertThat(loginPage.getAlertText()).contains(part);
    }
}
