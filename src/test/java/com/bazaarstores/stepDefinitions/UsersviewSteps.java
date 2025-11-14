package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.*;

import static org.junit.Assert.*;

public class UsersviewSteps {

    AllPages allPages = new AllPages();

    @When("Admin clicks on {string} menu")
    public void admin_clicks_on_menu(String menuName) {
        if (menuName.equalsIgnoreCase("Users")) {
            allPages.getUsersviewPage().clickUsersMenu();
        }
    }

    @Then("All users should be displayed")
    public void all_users_should_be_displayed() {
        assertTrue("Expected at least one user to be displayed",
                allPages.getUsersviewPage().getUsersCount() > 0);
    }

    @When("Admin searches for email {string}")
    public void admin_searches_for_email(String email) {
        allPages.getUsersviewPage().searchUserByEmail(email);
    }

    @Then("User with email {string} should be displayed")
    public void user_with_email_should_be_displayed(String email) {
        assertTrue("Expected user with email " + email + " to be displayed",
                allPages.getUsersviewPage().isUserDisplayed(email));
    }

    @Then("Message {string} should be displayed")
    public void message_should_be_displayed(String message) {
        assertTrue("Expected message '" + message + "' to be displayed",
                allPages.getUsersviewPage().isNoUserMessageDisplayed());
    }
}
