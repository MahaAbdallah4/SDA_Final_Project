package com.bazaarstores.pages;

import com.bazaarstores.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    // By Locators
    private final By dashboard = By.xpath("//div[@class='products-grid']");
    private final By profileVisitChart = By.xpath("//div[@class='card-body']");
    private final By welcomeMessage = By.cssSelector(".welcome-message, [class*='welcome']");
    private final By profileLink = By.cssSelector("a[href*='profile'], button:contains('Profile')");
    private final By ordersLink = By.cssSelector("a[href*='orders'], button:contains('Orders')");
    private final By productsLink = By.cssSelector("a[href*='products'], button:contains('Products')");
    private final By logoutButton = By.cssSelector("button:contains('Logout'), a:contains('Logout')");
    private final By userName = By.cssSelector(".user-name, [class*='username']");
    private final By toastNotification = By.xpath("//div[contains(@class, 'toastify')]");
    private final By tooltipMessage = By.cssSelector(".tooltip-message");
    private final By addStoreButton = By.cssSelector("button.btn.btn-outline-primary.no-hover.float-start.float-lg-end");
    private final By storeNameInput = By.id("first-name-column");
    private final By iframeLocator = By.id("default_ifr"); // ID of the TinyMCE iframe
    private final By editorBody = By.id("tinymce");
    private final By storeLocationInput = By.id("location-id-column");
    private final By storeAdminsInput = By.id("admin-column");
    private final By submitButton = By.cssSelector("button[type='submit']"); // Adjust based on actual submit button selector
    private final By successMessage = By.className("toast-message");// Adjust based on actual success message ID
    private final By errorMessage = By.xpath("//ul/li[contains(text(), 'The name field is required.') or contains(text(), 'The location field is required.')]");
    private final By storesLink = By.cssSelector("a.sidebar-link[href='https://bazaarstores.com/stores']");
    private final By errorUpdatedMessage = By.xpath("//div[@class='alert alert-danger']//ul/li[contains(text(), 'The name field is required.') or contains(text(), 'The location field is required.') or contains(text(), 'The description field is required.')]");

    // Additional locators for retrieving updated store details
    private final By updatedStoreName = By.xpath("//table[@id='stores-table']//td[contains(@class, 'store-name')]");
    private final By updatedStoreDescription = By.xpath("//table[@id='stores-table']//td[contains(@class, 'store-description')]");
    private final By updatedStoreLocation = By.xpath("//table[@id='stores-table']//td[contains(@class, 'store-location')]");
    private final By updatedStoreAdmins = By.xpath("//table[@id='stores-table']//td[contains(@class, 'store-admins')]");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    // Navigation Methods
    public void clickProfileLink() {
        click(profileLink);
    }

    public void clickOrdersLink() {
        click(ordersLink);
    }

    public void clickProductsLink() {
        click(productsLink);
    }

    public LoginPage clickLogout() {
        click(logoutButton);
        return new LoginPage(driver);
    }

    // Verification Methods
    public boolean isDashboardPageDisplayed() {
        return isDisplayed(dashboard);
    }

    public boolean isWelcomeMessageDisplayed() {
        return isDisplayed(welcomeMessage);
    }

    public String getWelcomeMessageText() {
        return getText(welcomeMessage);
    }

    public String getUserName() {
        return getText(userName);
    }

    public boolean isProfileLinkDisplayed() {
        return isDisplayed(profileLink);
    }

    public boolean isOrdersLinkDisplayed() {
        return isDisplayed(ordersLink);
    }

    public boolean isProductsLinkDisplayed() {
        return isDisplayed(productsLink);
    }

    public boolean isProfileVisitChartDisplayed() {
        return isDisplayed(profileVisitChart);
    }

    public void navigateToAddStore() {
        waitForElementToBeClickable(addStoreButton);
        click(addStoreButton);
    }

    public void submitStoreForm() {
        click(submitButton);
    }

    public boolean isStoreAddedSuccessfully() {
        return isDisplayed(successMessage);
    }

    public boolean isNotStoreAddedSuccessfully() {
        return isDisplayed(errorMessage);
    }

    public boolean isNotStoreUpdatedSuccessfully() {
        return isDisplayed(errorUpdatedMessage);
    }

    public boolean isToastNotificationDisplayed() {
        return Driver.getDriver().findElements(toastNotification).size() > 0;
    }

    public String getToastNotificationText() {
        if (isToastNotificationDisplayed()) {
            return Driver.getDriver().findElement(toastNotification).getText();
        }
        return "No toast notification displayed";
    }

    public boolean waitForToastToBeVisible() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(toastNotification));
            return true;
        } catch (TimeoutException e) {
            System.out.println("Toast notification did not appear within the timeout.");
            return false;
        }
    }

    public void enterStoreName(String name) {
        sendKeys(storeNameInput, name);
    }

    public void enterStoreDescriptionWithClick(String description) {
        Driver.getDriver().switchTo().frame(Driver.getDriver().findElement(iframeLocator));
        Driver.getDriver().findElement(editorBody).click();
        Driver.getDriver().findElement(editorBody).sendKeys(description);
        Driver.getDriver().switchTo().defaultContent();
    }

    public void enterStoreLocation(String location) {
        sendKeys(storeLocationInput, location);
    }

    public void enterStoreAdmins(String admins) {
        wait.until(ExpectedConditions.elementToBeClickable(storeAdminsInput));
        Select adminSelect = new Select(Driver.getDriver().findElement(storeAdminsInput));
        adminSelect.selectByVisibleText(admins);
    }

    // Navigation Methods
    public void navigateToStores() {
        click(storesLink);
    }

    // Getting Updated Store Details
    public String getUpdatedStoreName() {
        return getText(updatedStoreName);
    }

    public String getUpdatedStoreDescription() {
        return getText(updatedStoreDescription);
    }

    public String getUpdatedStoreLocation() {
        return getText(updatedStoreLocation);
    }

    public String getUpdatedStoreAdmins() {
        return getText(updatedStoreAdmins);
    }


}
