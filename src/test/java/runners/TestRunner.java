package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"hooks", "stepDefinitions"},
    plugins = {
    "pretty",
    "html:target/cucumber.html",
    "json:target/cucumber.json",
    "hooks.ExtentCucumberPlugin"
}
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @DataProvider(parallel = true)
    @Override
    public Object[] scenarios() {
        return super.scenarios();
    }
}
