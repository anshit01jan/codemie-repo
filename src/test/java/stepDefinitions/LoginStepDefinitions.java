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
import org.openqa.selenium.By;
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
        if (DriverManager.getDriver() == null) {
            throw new IllegalStateException("WebDriver is null. Check Hooks.java for driver init.");
        }
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds())));
        loginPage = new LoginPage(driver, wait);
        dashboardPage = new DashboardPage(driver, wait);
    }

    private String currentPath() {
        return URI.create(driver.getCurrentUrl()).getPath();
    }

    @Given("the user is on the Login page")
    public void user_on_login_page() {
        initPages();
        loginPage.open();
    }
}
