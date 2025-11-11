package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.*;

public class UsersSteps {

    WebDriver driver = Driver.getDriver();
    AllPages allPages = new AllPages(driver);

    @Given("Admin is logged in")
    public void admin_is_logged_in() {
        driver.get("https://bazaarstores.com/");
        allPages.getLoginPage().login("admin@sda.com", "Password.12345");
    }

    @When("Admin clicks on {string} menu")
    public void admin_clicks_on_menu(String menuName) {
        if(menuName.equalsIgnoreCase("Users")) {
            allPages.getUsersPage().clickUsersMenu();
        }
    }

    @Then("All users should be displayed")
    public void all_users_should_be_displayed() {
        assertTrue(allPages.getUsersPage().getUsersCount() > 0);
    }

    @When("Admin searches for email {string}")
    public void admin_searches_for_email(String email) {
        allPages.getUsersPage().searchUserByEmail(email);
    }

    @Then("User with email {string} should be displayed")
    public void user_with_email_should_be_displayed(String email) {
        assertTrue(allPages.getUsersPage().isUserDisplayed(email));
    }

    @Then("Message {string} should be displayed")
    public void message_should_be_displayed(String message) {
        assertTrue(allPages.getUsersPage().isNoUserMessageDisplayed());
    }
}
