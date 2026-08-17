package framework.drivers;

import framework.utils.Config;
import framework.utils.Logger;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {
    private DriverFactory() {}

    public static WebDriver initDriver(String browserParam) {
        String browser = (browserParam == null || browserParam.isBlank())
                ? Config.get("browser")
                : browserParam;

        if (browser == null || browser.isBlank()) browser = "chrome";
        browser = browser.toLowerCase();

        WebDriver driver;
        switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffox = new FirefoxOptions();
                if (Config.isHeadless()) ffox.addArguments("--headless");
                driver = new FirefoxDriver(ffox);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            }
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (Config.isHeadless()) options.addArguments("--headless=new");
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
            }
            default -> {
                Logger.warn("Unknown browser '" + browser + "'. Falling back to Chrome.");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }
        }

        DriverManager.setDriver(driver);
        driver.manage().window().maximize();
        return driver;
    }
}
