package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UsersPage extends BasePage {

    private final By usersMenu = By.xpath("//span[text()='Users']");
    private final By searchInput = By.xpath("//input[@name='email']");
    private final By searchButton = By.xpath("//button[text()='Search']");
    private final By noUserMessage = By.xpath("//*[contains(text(),'No users found')]");
    private final By usersTableRows = By.xpath("//table//tbody//tr");

    public UsersPage(WebDriver driver) {
        super(driver);
    }

    public void clickUsersMenu() {
        click(usersMenu);
        waitFor(1);
    }

    public void searchUserByEmail(String email) {
        sendKeys(searchInput, email);
        click(searchButton);
        waitFor(1);
    }

    public boolean isUserDisplayed(String email) {
        return findElements(usersTableRows).stream()
                .anyMatch(row -> row.getText().contains(email));
    }

    public boolean isNoUserMessageDisplayed() {
        return isDisplayed(noUserMessage);
    }

    public int getUsersCount() {
        return findElements(usersTableRows).size();
    }
}
