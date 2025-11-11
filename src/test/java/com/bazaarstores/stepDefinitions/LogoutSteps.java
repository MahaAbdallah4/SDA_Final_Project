package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class LogoutSteps {

    AllPages pages = new AllPages();

    @Given("user is logged in")
    public void userIsLoggedIn() {
        Driver.getDriver().get(ConfigReader.getBaseUrl());
        pages.getLoginPage()
                .enterEmail(ConfigReader.getCustomerEmail())
                .enterPassword(ConfigReader.getDefaultPassword())
                .clickLoginButton();

    }

    @When("user clicks on the account avatar icon")
    public void userClicksOnTheAccountAvatarIcon() {
        pages.getDashboardPage().clickProfileIcon();
    }

    @And("user selects the {string} option from the dropdown")
    public void userSelectsTheOptionFromTheDropdown(String arg0) {
        pages.getDashboardPage().clickLogoutButton();
    }

    @Then("user should be logged out successfully")
    public void userShouldBeLoggedOutSuccessfully() {
        assertTrue("Login page should be displayed after logout",
                pages.getLoginPage().isLoginPageDisplayed());
    }

    @Then("the user should be redirected to the login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        assertTrue("User should be redirected to the login page after logout",
                pages.getLoginPage().isLoginPageDisplayed());
    }

}
