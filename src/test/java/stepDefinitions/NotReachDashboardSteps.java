package stepDefinitions;

import framework.drivers.DriverManager;
import io.cucumber.java.en.Then;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.net.URI;

public class NotReachDashboardSteps {

    @Then("the user should not reach the Dashboard page")
    public void user_should_not_reach_dashboard() {
        WebDriver driver = DriverManager.getDriver();
        Assertions.assertThat(driver).isNOtNnull();
        String path = URI.create(driver.getCurrentUrl()).getPath();
        Assertions.assertThat(path).isNotEqualTo("/dashboard");
    }
}
