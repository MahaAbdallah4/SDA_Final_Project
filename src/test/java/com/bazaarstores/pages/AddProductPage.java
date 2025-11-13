package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddProductPage extends BasePage {

    private final By productsMenu = By.xpath("//a[contains(@href, '/products') and contains(., 'Products')]");
    private final By addProductButton = By.xpath("//a[contains(@href, '/product/create') or contains(., 'ADD PRODUCT')]");
    private final By nameInput = By.xpath("//input[@name='name' or @placeholder='Product Name']");
    private final By priceInput = By.xpath("//input[@name='price' or @placeholder='Price']");
    private final By stockInput = By.xpath("//input[@name='stock' or @placeholder='Stock']");
    private final By skuInput = By.xpath("//input[@name='sku' or @placeholder='SKU']");
    private final By submitButton = By.xpath("//button[contains(text(), 'Submit') or contains(text(), 'Save')]");
    private final By errorMessage = By.xpath("//*[contains(@class, 'error') or contains(text(), 'required')]");
    private final By successMessage = By.xpath("//*[contains(@class, 'success') or contains(text(), 'added')]");
    private final By productsTable = By.xpath("//table");

    public AddProductPage(WebDriver driver) {
        super(driver);
    }


    public void navigateToAddProduct() {
        click(productsMenu);
        waitFor(2);
        click(addProductButton);
        waitForUrlContains("/product/create");
    }


    public void leaveFieldsEmpty(String name, String price) {

    }
    public void fillAllFields(String name, String price, String stock, String sku) {
        sendKeys(nameInput, name);
        sendKeys(priceInput, price);
        sendKeys(stockInput, stock);
        sendKeys(skuInput, sku);
    }

    public void clickSubmit() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(submitButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);

        try {

            wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }


    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public boolean isSuccessDisplayed() {
        return isDisplayed(successMessage);
    }

    public boolean isProductInCatalog(String productName) {

        navigateToUrl("https://bazaarstores.com/products");


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(productsTable));


        return table.getText().contains(productName);
    }
}
