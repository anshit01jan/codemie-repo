package hooks;

import com.aventstack.extentreports.ExtentTest;
import framework.drivers.DriverFactory;
import framework.drivers.DriverManager;
import framework.reports.ExtentManager;
import framework.reports.ExtentTestManager;
import framework.utils.Config;
import framework.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.UUID;

public class Hooks {

    @Before(order = 0)
    public void beforeSuite() {
        Config.load();
        ExtentManager.getExtentReports();
    }

    @Before(order = 1)
    public void setup(Scenario scenario) {
        DriverFactory.initDriver(System.getProperty("browser"));
        // Scenario node is created by ExtentCucumberPlugin (TestCaseStarted)
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                String path = ScreenshotUtil.capture("FAILED_" + UUID.randomUUID() + ".png");
                ExtentTest test = ExtentTestManager.getTest();
                if (test != null && path != null) {
                    test.addScreenCaptureFromPath(path);
                }
            }
        } finally {
            DriverManager.quitDriver();
        }
    }

    @After(order = 1000)
    public void afterSuite() {
        ExtentManager.flush();
    }
}
