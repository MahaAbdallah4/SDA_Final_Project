package com.bazaarstores.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddStorePage {

    private WebDriver driver;

    //Locators for elements on the Add Store page
    private By nameField = By.id("first-name-column");
    private By descriptionField = By.id("tinymce");
    private By locationField = By.id("location-id-column");
    private By adminsField = By.id("admin-column");
    private By submitButton = By.cssSelector("button[type='submit']");
    private By errorMessage = By.cssSelector("#errorMessage li");

    public AddStorePage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void enterDescription(String description) {
        driver.findElement(descriptionField).sendKeys(description);
    }

    public void enterLocation(String location) {
        driver.findElement(locationField).sendKeys(location);
    }

    public void enterAdmins(String admins) {
        driver.findElement(adminsField).sendKeys(admins);
    }

    public void clickSubmit() {
        driver.findElement(submitButton).click();
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public void addStore(String name, String description, String location, String admins) {
        enterName(name);
        enterDescription(description);
        enterLocation(location);
        enterAdmins(admins);
        clickSubmit();
    }
}