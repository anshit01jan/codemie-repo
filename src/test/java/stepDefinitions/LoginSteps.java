package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.DashboardPage;
import framework.pages.LoginPage;
import framework.utils.Config;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * NOTE:
 * This class previously duplicated step definitions already implemented in
 * {@link stepDefinitions.LoginStepDefinitions}.
 * To prevent Cucumber DuplicateStepDefinitionException, the step patterns were removed.
 */
public class LoginSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

    private void initPages() {
        if (DriverManager.getDriver() == null) {
            throw new IllegalStateException("WebDriver is null. Check hooks/driver initialization.");
        }
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds()));
        loginPage = new LoginPage(driver, wait);
        new DashboardPage(driver, wait);
    }

    // Helper method kept for potential reuse from other step classes (no annotations).
    public void openLoginPage() {
        initPages();
        loginPage.open();
    }
}
