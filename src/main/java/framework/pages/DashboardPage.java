package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage extends BasePage {
    public DashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String getPath() {
        return java.net.URI.create(driver.getCurrentUrl()).getPath();
    }
}
