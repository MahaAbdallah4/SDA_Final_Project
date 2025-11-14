package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DeleteProductSteps {

    private WebDriver driver = Driver.getDriver();
    private AllPages allPages = new AllPages();
    private final By productsMenu = By.xpath("//a[contains(@href, '/products') and contains(., 'Products')]");

    @Given("the store manager is logged in")
    public void theStoreManagerIsLoggedIn() {
        driver.get("https://bazaarstores.com/");
        allPages.getLoginPage().enterEmail("storemanager@sda.com");
        allPages.getLoginPage().enterPassword("Password.12345");
        allPages.getLoginPage().clickLoginButton();
        allPages.getLoginPage().waitFor(2);
        allPages.getLoginPage().click(productsMenu);

        Assert.assertTrue("Products page should be displayed",
                driver.getCurrentUrl().contains("/products"));
    }

    @Given("the product {string} exists in the catalog")
    public void theProductExistsInTheCatalog(String productName) {
        By productLocator = By.xpath("//table//td[normalize-space()='" + productName + "']");
        Assert.assertTrue("Product should exist in the catalog",
                driver.findElements(productLocator).size() > 0);
    }

    @When("the store manager clicks the delete button for {string}")
    public void theStoreManagerClicksTheDeleteButtonFor(String productName) {
        // تعديل XPath لاستهداف زر الحذف داخل الصف الصحيح
        By deleteBtn = By.xpath("//tr[td[normalize-space()='" + productName + "']]//button[i[contains(@class,'bi-trash3')]]");

        WebElement deleteButton = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(deleteBtn));


        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", deleteButton);
        deleteButton.click();
    }

    @And("confirms the deletion")
    public void confirmsTheDeletion() {
        By confirmDeleteBtn = By.xpath("//button[contains(@class,'swal2-confirm') and text()='Yes, delete it!']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn))
                .click();
    }

    @And("cancels the deletion")
    public void cancelsTheDeletion() {
        By cancelDeleteBtn = By.xpath("//button[contains(@class,'swal2-cancel')]");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(cancelDeleteBtn))
                .click();
    }

    @Then("{string} should no longer appear in the product catalog")
    public void shouldNoLongerAppearInTheProductCatalog(String productName) {
        By productLocator = By.xpath("//table//td[normalize-space()='" + productName + "']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOfElementLocated(productLocator));
        Assert.assertTrue("Product should be deleted",
                driver.findElements(productLocator).isEmpty());
    }

    @Then("{string} should still appear in the product catalog")
    public void shouldStillAppearInTheProductCatalog(String productName) {
        By productLocator = By.xpath("//table//td[normalize-space()='" + productName + "']");
        Assert.assertTrue("Product should still exist",
                driver.findElements(productLocator).size() > 0);
    }

    @Then("a success notification should be displayed")
    public void aSuccessNotificationShouldBeDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            By toastContainer = By.cssSelector("div.toast");
            List<WebElement> toasts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(toastContainer));

            boolean foundSuccess = false;

            for (WebElement toast : toasts) {
                String title = toast.findElement(By.cssSelector(".toast-title")).getText().trim();
                String message = toast.findElement(By.cssSelector(".toast-message")).getText().trim();
                if (title.equalsIgnoreCase("Success") && message.contains("Product deleted successfully")) {
                    foundSuccess = true;

                    Assert.assertTrue("Success notification should be visible", toast.isDisplayed());

                    wait.until(ExpectedConditions.invisibilityOf(toast));
                    break;
                }
            }

            if (!foundSuccess) {
                Assert.fail("Success notification not found with expected text.");
            }

        } catch (TimeoutException e) {
            Assert.fail("Success notification should be visible, but was not found or disappeared too quickly.");
        }
    }
}
