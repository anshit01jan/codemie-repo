package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.LoginPage;
import framework.utils.Config;
import io.cucumber.java.en.Then;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginAlertStepDefinitions {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

    private void init() {
        driver = DriverManager.getDriver();
        if (driver == null) throw new IllegalStateException("WebDriver is null. Check hooks.");
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds()));
        loginPage = new LoginPage(driver, wait);
    }

    @Then("the user should see an error alert displayed in the alert container")
    public void see_error_alert() {
        init();
        try {
            wait.until(d -> loginPage.isAlertVisible());
        } catch (Exception ignored) {
        }
        Assertions.assertThat(loginPage.isAlertVisible()).isTrue();
    }


    @Then("the error alert should no longer be visible")
    public void alert_not_visible() {
        init();
        Assertions.assertThat(loginPage.isAlertVisible()).isFalse();
    }
}
