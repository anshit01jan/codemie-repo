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
import org.openqa.selenium.support.ui.SeleniumWebDriverWait;

import java.net.URI;
import java.time.Duration;

public class LoginSteps {

    private WebDriver driver;
    private SeleniumWebDriverWait wait;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    private String username;
    private String password;

    private void initPages() {
        driver = DriverManager.getDriver();
        wait = new SeleniumWebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds())));
        loginPage = new LoginPage(driver, wait);
        dashboardPage = new DashboardPage(driver, wait);
    }

    OG