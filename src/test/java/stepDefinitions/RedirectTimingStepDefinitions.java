package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.DashboardPage;
import framework.pages.LoginPage;
import framework.utils.Config;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RedirectTimingStepDefinitions {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    private void init() {
        driver = DriverManager.getDriver();
        if (driver == null) throw new IllegalStateException("WebDriver is null. Check hooks.");
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getTimeoutSeconds()));
        loginPage = new LoginPage(driver, wait);
        dashboardPage = new DashboardPage(driver, wait);
    }

    @When("the user logs in with valid username {string} and valid password {string}")
    public void login_with_valid_creds(String user, String pass) {
        init();
        loginPage.login(user, pass);
    }

    @Then("the user should see a success message in the alert container")
    public void see_success_in_alert() {
        init();
        Assertions.assertThat(loginPage.isAlertVisible()).isTrue();
    }


    @When("the user waits for {int} second")
    public void wait_one_second(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }

    @Then("the user should be redirected to the Dashboard page {string}")
    public void redirected_to_dashboard(String path) {
        init();
        Assertions.assertThat(dashboardPage.getPath()).isEqualTo(path);
    }
}
