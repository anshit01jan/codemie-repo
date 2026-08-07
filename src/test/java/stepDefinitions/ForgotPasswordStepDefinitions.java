package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.LoginPage;
import framework.utils.Config;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgotPasswordStepDefinitions {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

    private void init() {
        driver = DriverManager.getDriver();
        if (driver == null) throw new IllegalStateException("WebDriver is null. Check hooks.");
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds())));
        loginPage = new LoginPage(driver, wait);
    }

    @When("the user clicks on the \"Forgot Password?\" link")
    public void click_forgot_link() {
        init();
        loginPage.clickForgotPassword();
    }


    @Then("the Forgot Password modal should be displayed")
    public void fp_modal_visible() {
        init();
        Assertions.assertThat(loginPage.isForgotPasswordModalVisible()).isTrue();
    }
}
