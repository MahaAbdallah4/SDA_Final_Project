package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StoreListPage {

    private WebDriver driver;
    private By storeList = By.xpath("//table[@class='table table-bordered mb-0']//tbody/tr");
    private By confirmationDialog = By.cssSelector(".swal2-popup"); // Selector for the confirmation dialog
    private By confirmButton = By.cssSelector(".swal2-confirm"); // Selector for the confirm button
    private By cancelButton = By.cssSelector(".swal2-cancel"); // Selector for the cancel button

    public StoreListPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isStoreDisplayed(String storeName) {
        List<WebElement> stores = driver.findElements(storeList);
        return stores.stream().anyMatch(store -> store.getText().contains(storeName));
    }

    public void clickEditButton(String storeName) {
        List<WebElement> rows = driver.findElements(storeList);
        for (WebElement row : rows) {
            if (row.findElement(By.xpath(".//td[1]")).getText().equals(storeName)) {
                row.findElement(By.cssSelector(".btn.btn-outline-primary.me-1.no-hover")).click();
                return;
            }
        }
        throw new RuntimeException("Edit button not found for store: " + storeName);
    }

    public void clickDeleteButton(String storeName) {
        List<WebElement> rows = driver.findElements(storeList);
        for (WebElement row : rows) {
            if (row.findElement(By.xpath(".//td[1]")).getText().equals(storeName)) {
                WebElement deleteButton = row.findElement(By.xpath(".//button[contains(@onclick, 'confirmDelete')]"));
                deleteButton.click(); // Click the delete button in the same row
                return;
            }
        }
        throw new RuntimeException("Delete button not found for store: " + storeName);
    }

    public boolean isConfirmationDialogDisplayed() {
        return !driver.findElements(confirmationDialog).isEmpty(); // Check if the dialog is present
    }

    public void confirmDeletion() {
        if (isConfirmationDialogDisplayed()) {
            driver.findElement(confirmButton).click(); // Click the confirm button
        } else {
            throw new RuntimeException("Confirmation dialog is not displayed.");
        }
    }

    public void cancelDeletion() {
        if (isConfirmationDialogDisplayed()) {
            driver.findElement(cancelButton).click(); // Click the cancel button
        } else {
            throw new RuntimeException("Confirmation dialog is not displayed.");
        }
    }
}