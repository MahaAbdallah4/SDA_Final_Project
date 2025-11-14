package com.bazaarstores.pages;

import com.bazaarstores.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DeleteProductPage extends BasePage {


    private final By productsMenu = By.xpath("//a[contains(@href, '/products') and contains(., 'Products')]");
    private final By productsTable = By.xpath("//table");
    private final String deleteButtonXpath = "//tr[td[contains(normalize-space(),'%s')]]//button[contains(text(),'Delete')]";
    private final By confirmDeleteButton = By.xpath("//button[contains(text(),'Yes') or contains(text(),'Confirm')]");
    private final By cancelDeleteButton = By.xpath("//button[contains(text(),'No') or contains(text(),'Cancel')]");


    public DeleteProductPage() {
        this.driver = Driver.getDriver();
    }


    public void navigateToProductsPage() {
        driver.get("https://bazaarstores.com/products");
        waitForElementToBeVisible(productsTable);
    }


    public void clickDeleteButton(String productName) {
        By deleteButton = By.xpath(String.format(deleteButtonXpath, productName));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(deleteButton));


        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);


        waitFor(10);


        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }


    public void confirmDeletion() {
        waitForElementToBeClickable(confirmDeleteButton);
        click(confirmDeleteButton);
    }


    public void cancelDeletion() {
        waitForElementToBeClickable(cancelDeleteButton);
        click(cancelDeleteButton);
    }


    public boolean isProductDeleted(String productName) {
        navigateToProductsPage();
        return !findElement(productsTable).getText().contains(productName);
    }


    public boolean isProductPresent(String productName) {
        navigateToProductsPage();
        return findElement(productsTable).getText().contains(productName);
    }
}

