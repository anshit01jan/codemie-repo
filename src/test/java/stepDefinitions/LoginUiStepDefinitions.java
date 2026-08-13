package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.LoginPage;
import framework.utils.Config;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginUiStepDefinitions {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

    private void init() {
        driver = DriverManager.getDriver();
        if (driver == null) throw new IllegalStateException("WebDriver is null. Check hooks.");
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds()));
        loginPage = new LoginPage(driver, wait);
    }

    @Given("the user navigates to the Login page")
    public void user_navigates_to_login() {
        init();
        loginPage.open();
    }

    @Then("the user should see an input field {string}")
    public void user_sees_input_field(String label) {
        if ("Email Address".equalsIgnoreCase(label)) {
            Assertions.assertThat(driver.findElement(org.openqa.selenium.By.cssSelector("#forgotPasswordModal input[type='email']")).isDisplayed()).isTrue();
        } else if ("Username".equalsIgnoreCase(label)) {
            Assertions.assertThat(driver.findElement(org.openqa.selenium.By.id("username")).isDisplayed()).isTrue();
        } else if ("Password".equalsIgnoreCase(label)) {
            Assertions.assertThat(driver.findElement(org.openqa.selenium.By.id("password")).isDisplayed()).isTrue();
        } else {
            throw new IllegalArgumentException("Unsupported input label: " + label);
        }
    }

    @And("the user should see a button {string}")
    public void user_sees_button(String name) {
        if ("Login".equalsIgnoreCase(name)) {
            Assertions.assertThat(driver.findElement(org.openqa.selenium.By.cssSelector("button[type='submit']")).isDisplayed()).isTrue();
        } else if ("Send Reset Link".equalsIgnoreCase(name)) {
            Assertions.assertThat(driver.findElement(org.openqa.selenium.By.cssSelector("#forgotPasswordModal button[type='submit']")).isDisplayed()).isTrue();
        } else {
            throw new IllegalArgumentException("Unsupported button name: " + name);
        }
    }

    @And("the user should see a link {string}")
    public void user_sees_link(String text) {
        Assertions.assertThat(driver.getPageSource()).contains(text);
    }
}
