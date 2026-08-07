package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.LoginPage;
import framework.utils.Config;
import io.cucumber.java.en.Then;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgotPasswordResetAssertionsSteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

    private void init() {
        driver = DriverManager.getDriver();
        if (driver == null) throw new IllegalStateException("WebDriver is null. Check hooks.");
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds()));
        loginPage = new LoginPage(driver, wait);
    }

    @Then("the Email field should be empty")
    public void the_email_field_should_be_empty() {
        init();
        Assertions.assertThat(loginPage.getForgotPasswordEmailValue()).isEmpty();
    }


    @Then("the Forgot Password alert container should be empty")
    public void the_fp_alert_container_should_be_empty() {
        init();
        Assertions.assertThat(loginPage.isForgotPasswordAlertEmpty()).isTrue();
    }
}
