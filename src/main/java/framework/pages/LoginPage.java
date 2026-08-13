package framework.pages;

import framework.base.BasePage;
import framework.utils.environment.Env;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    private final By fpAlertContainer = By.cssSelector("#forgotPasswordModal #fpAlertContainer");
    private final By fpCloseBtn = By.cssSelector("#forgotPasswordModal .btn-close");
    private final By fpEmailInvalidFeedback = By.cssSelector("#forgotPasswordModal .invalid-feedback");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void open() {
        driver.get(Env.baseUrl() + "/login");
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
        return getText(alertContainer);
    }

    public boolean isAlertVisible() {
        return isVisible(alertContainer);
    }

    public void clickForgotPassword() {
        clickOnElement(forgotPasswordLink);
    }

    public boolean isForgotPasswordModalVisible() {
        return isVisible(forgotPasswordModal);
    }

    public String getForgotPasswordTitle() {
        return getText(fpModalTitle);
    }

    public void enterForgotPasswordEmail(String email) {
        enterTextInInputBox(fpEmailInput, email);
    }

    public void clickSendResetLink() {
        clickOnElement(sendResetLinkBtn);
    }

    public String getForgotPasswordAlertText() {
        return getText(fpAlertContainer);
    }

    public boolean isForgotPasswordAlertEmpty() {
        String t = getForgotPasswordAlertText();
        return t == null || t.trim().isEmpty();
    }

    public String getForgotPasswordEmailValue() {
        return driver.findElement(fpEmailInput).getAttribute("value");
    }

    public boolean isForgotPasswordEmailValidationErrorVisible() {
        return isVisible(fpEmailInvalidFeedback);
    }

    public void closeForgotPasswordModal() {
        clickOnElement(fpCloseBtn);
        // allow modal close animation to finish
        sleepSeconds(1);
    }
}
