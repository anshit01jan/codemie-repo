package hooks;

import framework.drivers.DriverFactory;
import framework.drivers.DriverManager;
import framework.reports.ExtentManager;
import framework.utils.Config;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before(order = 0)
    public void beforeSuite() {
        Config.load();
        ExtentManager.getExtentReports();
    }

    @Before(order = 1)
    public void setup(Scenario scenario) {
        DriverFactory.initDriver(System.getProperty("browser"));
    }

    @After
    public void tearDown(Scenario scenario) {
        DriverManager.quitDriver();
    }

    @After(order = 1000)
    public void afterSuite() {
        ExtentManager.flush();
    }
}
