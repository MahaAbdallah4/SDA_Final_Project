package com.bazaarstores.pages;

import com.bazaarstores.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditProductsPage extends BasePage {

    WebDriverWait waitDriver;


    public final By productsMenu = By.xpath("//a[normalize-space()='Products']");
    public final By saveButton = By.xpath("//button[normalize-space()='Submit']");
    public final By productNameInput = By.cssSelector("input[name='name']");
    public final By productPriceInput = By.cssSelector("input[name='price']");
    public final By productStockInput = By.cssSelector("input[name='stock']");

    public final By successMessage = By.xpath(
            "//*[contains(@class,'toast')][contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'success')]"
    );

    public final By errorMessage = By.xpath("//div[contains(@class,'alert-danger') or contains(@class,'error')]");

    public EditProductsPage() {
        this.driver = Driver.getDriver();
        waitDriver = new WebDriverWait(driver, Duration.ofSeconds(15));
    }


    public void clickProductsMenu() {
        click(productsMenu);
    }

    public void clickEditButton(String productName) {
        By editBtn = By.xpath("//tr[td[contains(text(),'" + productName + "')]]//a[i[contains(@class,'bi-pencil-square')]]");
        click(editBtn);
    }

    public void updateProductName(String name) {
        clearAndType(productNameInput, name);
    }

    public void updateProductPrice(String price) {
        clearAndType(productPriceInput, price);
    }

    public void updateProductStock(String stock) {
        clearAndType(productStockInput, stock);
    }

    public void clickSave() {
        scrollToElement(saveButton);

        try {
            waitForElementToBeClickable(saveButton);
            click(saveButton);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", findElement(saveButton));
        }
    }


    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }

    public String getSuccessMessageText() {
        return getText(successMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessageText() {
        return getText(errorMessage);
    }

    public String getProductValidationMessage(By inputLocator) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return (String) js.executeScript("return arguments[0].validationMessage;", findElement(inputLocator));
        } catch (Exception e) {
            return "";
        }
    }
}

