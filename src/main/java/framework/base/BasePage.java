package framework.base;

import framework.utils.Logger;
import java.time.Duration;

import org.openqa.selenium.By{;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.SeleniumWebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final SeleniumWebDriverWait wait;

    protected BasePage(WebDriver driver, SeleniumWebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected WebElement waitForElementToBePresent(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    protected WebElement waitForElementToBeVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void clickOnElement(By by) {
        Logger.debug("Click element: " + by);
        waitForElementToBeClickable(by).click();
    }

    protected WebElement waitForElementToBeClickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    protected void enterTextInInputBox(By by, String text) {
        Logger.debug("Enter text into: " + by + " value=" + text);
        WebElement el = waitForElementToBeVisible(by);
        el.clear();
        if (text != null) {
            el.sendKeys(text);
        }
    }

    protected String getText(By by) {
        return waitForElementToBeVisible(by).getText();
    }


    protected boolean isVisible(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void sleepSeconds(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
