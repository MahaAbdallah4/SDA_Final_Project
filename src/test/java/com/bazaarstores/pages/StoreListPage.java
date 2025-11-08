package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class StoreListPage {

    private WebDriver driver;
    private By storeList = By.xpath("//table[@class='table table-bordered mb-0']//tbody/tr/td[1]");
    private By editButton = By.cssSelector(".btn.btn-outline-primary.me-1.no-hover");
    private By deleteButton = By.cssSelector(".btn.btn-outline-primary");

    public StoreListPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isStoreDisplayed(String storeName) {
        List<WebElement> stores = driver.findElements(storeList);
        return stores.stream().anyMatch(store -> store.getText().contains(storeName));
    }

    public void clickEditButton(String storeName) {
        List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-bordered mb-0']//tbody/tr"));
        for (WebElement row : rows) {
            if (row.findElement(By.xpath(".//td[1]")).getText().equals(storeName)) {
                row.findElement(editButton).click(); // Click the edit button in the same row
                return;
            }
        }
        throw new RuntimeException("Edit button not found for store: " + storeName);
    }

    public void clickDeleteButton(String storeName) {
        List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-bordered mb-0']//tbody/tr"));
        for (WebElement row : rows) {
            if (row.findElement(By.xpath(".//td[1]")).getText().equals(storeName)) {
                row.findElement(deleteButton).click(); // Click the delete button in the same row
                return;
            }
        }
        throw new RuntimeException("Delete button not found for store: " + storeName);
    }
}