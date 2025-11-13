package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductsPage extends BasePage {

    public final By productsMenu = By.xpath("//a[normalize-space()='Products']");
    public final By saveButton = By.xpath("//button[normalize-space()='Submit']");
    public final By productNameInput = By.cssSelector("input[name='name']");
    public final By productPriceInput = By.cssSelector("input[name='price']");
    public final By productStockInput = By.cssSelector("input[name='stock']");


    public final By successMessage = By.xpath("//*[contains(@class,'toast-message') or contains(@class,'toast-progress')][contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'success')]");
    public final By errorMessage = By.xpath("//div[contains(@class,'alert-danger') or contains(@class,'error')]");

    private WebDriverWait waitDriver;

    public ProductsPage(WebDriver driver) {
        super(driver);
        this.waitDriver = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void clickProductsMenu() {
        waitDriver.until(ExpectedConditions.elementToBeClickable(productsMenu)).click();
    }

    public void clickEditButton(String productName) {
        By editBtn = By.xpath("//tr[td[contains(text(),'" + productName + "')]]//a[i[contains(@class,'bi-pencil-square')]]");
        waitDriver.until(ExpectedConditions.elementToBeClickable(editBtn)).click();
        System.out.println("Clicked edit for product: " + productName);
    }

    public void updateProductName(String name) {
        waitDriver.until(ExpectedConditions.visibilityOfElementLocated(productNameInput)).clear();
        sendKeys(productNameInput, name);
        System.out.println("Updated product name to: " + name);
    }

    public void updateProductPrice(String price) {
        waitDriver.until(ExpectedConditions.visibilityOfElementLocated(productPriceInput)).clear();
        sendKeys(productPriceInput, price);
        System.out.println("Updated product price to: " + price);
    }

    public void updateProductStock(String stock) {
        waitDriver.until(ExpectedConditions.visibilityOfElementLocated(productStockInput)).clear();
        sendKeys(productStockInput, stock);
        System.out.println("Updated product stock to: " + stock);
    }

    public void clickSave() {
        try {
            waitDriver.until(ExpectedConditions.elementToBeClickable(saveButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(saveButton));
            driver.findElement(saveButton).click();
            System.out.println("Clicked Submit button");
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            System.out.println("Normal click failed, using JavaScript click as fallback.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(saveButton));
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            waitDriver.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            Thread.sleep(1000);
            boolean displayed = driver.findElement(successMessage).isDisplayed();
            System.out.println("Success message displayed: " + getSuccessMessageText());
            return displayed;
        } catch (Exception e) {
            System.out.println("Success message not found.");
            return false;
        }
    }

    public String getSuccessMessageText() {
        try {
            waitDriver.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            String text = driver.findElement(successMessage).getText().trim();
            System.out.println("Success Toast Message: " + text);
            return text;
        } catch (Exception e) {
            System.out.println("No success toast found.");
            return "";
        }
    }
    public boolean isErrorMessageDisplayed() {
        try {
            waitDriver.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            String errorText = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
            System.out.println("Error message displayed: " + errorText);
            return errorText;
        } catch (Exception e) {
            return "";
        }
    }

    public String getProductValidationMessage(By inputLocator) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return (String) js.executeScript(
                    "return arguments[0].validationMessage;", findElement(inputLocator));
        } catch (Exception e) {
            return "";
        }
    }
}
