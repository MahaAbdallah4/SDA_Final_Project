package com.bazaarstores.pages;

import com.bazaarstores.utilities.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class ViewManagerPage {

    public ViewManagerPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

    // Burger menu (for mobile / sidebar toggle)
    @FindBy(css = "a.burger-btn")
    public WebElement burgerMenuButton;



    // Product table
    @FindBy(css = "table.table-bordered")
    public WebElement productsTable;


    @FindBy(css = "a.burger-btn")
    public WebElement burgerMenuBtn;

    @FindBy(xpath = "//a[contains(@href, '/products') and contains(@class,'sidebar-link')]")
    public WebElement productsLink;

    @FindBy(xpath = "//table//tr")
    public List<WebElement> productRows;

    @FindBy(xpath = "//table//th")
    public List<WebElement> tableHeaders;

    public void clickMenu() {
        try {
            // if sidebar is hidden (small screen)
            wait.until(ExpectedConditions.visibilityOf(burgerMenuBtn));
            if (burgerMenuBtn.isDisplayed()) {
                burgerMenuBtn.click();
                System.out.println("Burger menu clicked.");
            } else {
                System.out.println("Burger menu already open.");
            }
        } catch (Exception e) {
            System.out.println("Burger menu not found or already open.");
        }
    }

    public void clickProductsLink() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(productsLink));
            productsLink.click();
            System.out.println("Clicked 'Products' link.");
        } catch (TimeoutException e) {
            throw new TimeoutException("Products link not clickable — ensure sidebar is open or locator is correct.");
        }
    }

    public boolean verifyProductsDisplayed() {
        wait.until(ExpectedConditions.visibilityOfAllElements(productRows));
        return productRows.size() > 0;
    }

    public boolean verifyTableHeaders() {
        String[] expected = {"NAME", "PRICE", "STOCK"};
        for (String header : expected) {
            boolean found = tableHeaders.stream().anyMatch(h -> h.getText().equalsIgnoreCase(header));
            if (!found) return false;
        }
        return true;
    }




    public boolean isProductTableDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(productsTable));
            return productsTable.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyProductTableHeaders() {
        String[] expectedHeaders = {"NAME", "PRICE", "STOCK"};
        for (String header : expectedHeaders) {
            boolean found = tableHeaders.stream()
                    .anyMatch(th -> th.getText().trim().equalsIgnoreCase(header));
            if (!found) {
                System.out.println(" Missing column: " + header);
                return false;
            }
        }
        return true;
    }
}
