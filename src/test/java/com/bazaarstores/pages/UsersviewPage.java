package com.bazaarstores.pages;

import com.bazaarstores.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class UsersviewPage extends BasePage {


    private final By usersMenu = By.xpath("//span[text()='Users']");
    private final By searchInput = By.xpath("//input[@name='email']");
    private final By searchButton = By.xpath("//button[text()='Search']");
    private final By noUserMessage = By.xpath("//*[contains(text(),'No users found')]");
    private final By usersTableRows = By.xpath("//table//tbody//tr");

    private WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(20));


    public void clickUsersMenu() {
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(usersMenu));

        try {

            new Actions(Driver.getDriver()).moveToElement(menu).click().perform();
        } catch (Exception e) {

            ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", menu);
        }


        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(usersTableRows));
    }


    public void searchUserByEmail(String email) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(email);

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        button.click();


        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(usersTableRows),
                ExpectedConditions.visibilityOfElementLocated(noUserMessage)
        ));
    }


    public boolean isUserDisplayed(String email) {
        List<WebElement> rows = Driver.getDriver().findElements(usersTableRows);
        return rows.stream().anyMatch(row -> row.getText().contains(email));
    }


    public boolean isNoUserMessageDisplayed() {
        return isDisplayed(noUserMessage);
    }


    public int getUsersCount() {
        return Driver.getDriver().findElements(usersTableRows).size();
    }


    public List<String> getAllUserEmails() {
        return Driver.getDriver().findElements(usersTableRows)
                .stream()
                .map(WebElement::getText)
                .toList();
    }
}