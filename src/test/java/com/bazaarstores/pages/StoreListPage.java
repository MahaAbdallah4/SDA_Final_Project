package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class StoreListPage {

    private WebDriver driver;
    private final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private By storeList = By.xpath("//table[@class='table table-bordered mb-0']//tbody/tr");
    private By confirmationDialog = By.cssSelector(".swal2-popup");
    private By confirmButton = By.cssSelector("[type='button'].swal2-confirm");
    private By cancelButton = By.cssSelector("[type='button'].swal2-cancel");
    private final By storeName = By.xpath("//table[@id='stores-table']//td[contains(@class, 'store-name')]");

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
            WebElement storeNameCell = row.findElement(By.xpath(".//td[1]")); // First <td> for the store name
            if (storeNameCell.getText().equals(storeName)) {
                WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-outline-primary')][1]"));
                editButton.click();
                return;
            }
        }
        throw new RuntimeException("Edit button not found for store: " + storeName);
    }

    public void clickCancelButton() {
        driver.findElement(cancelButton).click();
    }

    public void clickDeleteFirstStore() {
        List<WebElement> rows = driver.findElements(storeList);
        if (rows.isEmpty()) {
            throw new RuntimeException("No stores available to delete.");
        }
        WebElement firstRow = rows.get(0);
        WebElement deleteButton = firstRow.findElement(By.xpath(".//button[i[contains(@class, 'bi-trash3')]]"));
        deleteButton.click();
        confirmDeletion();
    }

    public boolean isStoreDisplayedById(String storeId) {
        List<WebElement> rows = driver.findElements(storeList);
        for (WebElement row : rows) {
            // Get the store ID from the attribute
            String currentStoreId = row.getAttribute("data-store-id"); // Adjusted to use the row directly

            // Check if the store ID matches, ensure currentStoreId is not null
            if (currentStoreId != null && currentStoreId.equals(storeId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isConfirmationDialogDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationDialog));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void confirmDeletion() {
        if (isConfirmationDialogDisplayed()) {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
        } else {
            throw new RuntimeException("Confirmation dialog is not displayed.");
        }
    }

    public void cancelDeletion() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(cancelButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(confirmationDialog));
    }

    public void clickDeleteButtonOfFirstStore() {
        List<WebElement> rows = driver.findElements(storeList);
        if (rows.isEmpty()) {
            throw new RuntimeException("No stores available to delete.");
        }
        WebElement firstRow = rows.get(0);
        WebElement deleteButton = firstRow.findElement(By.xpath(".//button[i[contains(@class, 'bi-trash3')]]"));
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }
    public String getFirstStoreNameFromList() {
        List<WebElement> nameElements = driver.findElements(storeName);
        if (!nameElements.isEmpty()) {
            return nameElements.get(0).getText().trim();
        }
        return null;
    }
}