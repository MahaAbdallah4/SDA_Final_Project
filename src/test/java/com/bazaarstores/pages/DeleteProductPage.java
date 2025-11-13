package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DeleteProductPage extends BasePage {

    public DeleteProductPage(WebDriver driver) {
        super(driver);

    }

    private final By productRowByName(String productName) {
        return By.xpath("//tr[td[text()='" + productName + "']]");
    }

    private final By deleteButtonByProduct(String productName) {
        return By.xpath("//tr[td[text()='" + productName + "']]//button[contains(text(),'Delete')]");
    }

    private final By successMessage = By.cssSelector(".toast-message.success");


    public DeleteProductPage clickDeleteButton(String productName) {
        click(deleteButtonByProduct(productName));
        return this;
    }


    public DeleteProductPage confirmDeletion() {
        acceptAlert();
        return this;
    }

    public DeleteProductPage cancelDeletion() {
        dismissAlert();
        return this;
    }


    public boolean isProductPresent(String productName) {
        return isDisplayed(productRowByName(productName));
    }

    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }

    public String getSuccessMessageText() {
        return getText(successMessage);
    }
}
