package framework.pages;

import framework.base.BasePage;
import framework.utils.environment.Env;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class LoginPage extends BasePage {

    // Basic locators (templates/login.html in codemie-repo)
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginBtn = By.cssSelector("button[type='submit']");
    private final By alertContainer = By.id("alertContainer");
    private final By forgotPasswordLink = By.partialLinkText("Forgot Password");

    // Forgot password modal
    private final By forgotPasswordModal = By.id("forgotPasswordModal");
    private final By fpModalTitle = By.cssSelector("#forgotPasswordModal .modal-title");
    private final By fpEmailInput = By.cssSelector("#forgotPasswordModal input[type='email']");
    private final By sendResetLinkBtn = By.cssSelector("#forgotPasswordModal button[type='submit']");
    // Template uses id "forgotPasswordAlertContainer" inside the modal
    private final By fpAlertContainer = By.cssSelector("#forgotPasswordModal #forgotPasswordAlertContainer");
    private final By fpCloseBtn = By.cssSelector("#forgotPasswordModal .btn-close");
    private final By fpEmailInvalidFeedback = By.cssSelector("#forgotPasswordModal .invalid-feedback");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void open() {
        String baseUrl = Env.baseUrl();
        String normalizedUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        driver.get(normalizedUrl);
    }

    public void enterUsername(String username) {
        enterTextInInputBox(usernameInput, username);
    }

    public void enterPassword(String password) {
        enterTextInInputBox(passwordInput, password);
    }

    public void clickLogin() {
        clickOnElement(loginBtn);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getAlertText() {
            try {
                return getText(alertContainer);
            } catch (Exception e) {
                return "";
            }
    }

    public boolean isAlertVisible() {
        try {
            waitForElementToBeVisible(alertContainer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickForgotPassword() {
        clickOnElement(forgotPasswordLink);
    }

    public boolean isForgotPasswordModalVisible() {
        try {
            waitForElementToBeVisible(forgotPasswordModal);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getForgotPasswordTitle() {
        return getText(fpModalTitle);
    }

    public void enterForgotPasswordEmail(String email) {
        enterTextInInputBox(fpEmailInput, email);
    }

    // Backwards-compatible no-arg overload used by step definitions
    public void clickSendResetLink() {
        clickOnElement(sendResetLinkBtn);
    }

    public String getForgotPasswordAlertText() {
        try {
            return getText(fpAlertContainer);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isForgotPasswordAlertEmpty() {
        String t = getForgotPasswordAlertText();
        return t == null || t.trim().isEmpty();
    }

    public String getForgotPasswordEmailValue() {
        return driver.findElement(fpEmailInput).getAttribute("value");
    }

    public boolean isForgotPasswordEmailValidationErrorVisible() {
        try {
            // First try: explicit invalid-feedback element inside modal
            List<WebElement> els = driver.findElements(org.openqa.selenium.By.cssSelector("#forgotPasswordModal .invalid-feedback, #forgotPasswordModal .text-danger, #forgotPasswordModal .error, #forgotPasswordModal .help-block"));
            for (org.openqa.selenium.WebElement el : els) {
                if (el.isDisplayed() && el.getText() != null && !el.getText().trim().isEmpty()) return true;
            }
        } catch (Exception ignored) {
        }
        try {
            // Fallback: input has 'is-invalid' or 'invalid' class
            String cls = driver.findElement(fpEmailInput).getAttribute("class");
            if (cls != null && (cls.contains("is-invalid") || cls.contains("invalid"))) return true;
        } catch (Exception ignored) {
        }
        try {
            // Fallback: aria-invalid or HTML5 validation message
            String aria = driver.findElement(fpEmailInput).getAttribute("aria-invalid");
            if (aria != null && aria.equalsIgnoreCase("true")) return true;
            Object msg = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return arguments[0].validationMessage;", driver.findElement(fpEmailInput));
            if (msg != null && msg.toString().trim().length() > 0) return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public void closeForgotPasswordModal() {
        clickOnElement(fpCloseBtn);
        // allow modal close animation to finish
        sleepSeconds(1);
    }
}
