package stepDefinitions;

import framework.drivers.DriverManager;
import io.cucumber.java.en.Then;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.net.URI;

public class RedirectOverrideSteps {
    @Then("the user should not be redirected to the Dashboard page")
    public void the_user_should_not_be_redirected() {
        WebDriver driver = DriverManager.getDriver();
        String path = URI.create(driver.getCurrentUrl()).getPath();
        Assertions.assertThat(path).isNotEqualTo("/dashboard");
    }
}
