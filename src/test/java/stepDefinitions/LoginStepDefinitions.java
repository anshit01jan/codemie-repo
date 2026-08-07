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

    @Then("the user should see an error message {string}")
    public void user_sees_error(String msg) {
        Assertions.assertThat(loginPage.getAlertText()).contains(msg);
    }
}
