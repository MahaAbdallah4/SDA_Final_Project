package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class ProductsStep {

    AllPages pages = new AllPages();

    @Given("user is logged in as a customer")
    public void userIsLoggedInAsACustomer() {
        Driver.getDriver().get(ConfigReader.getBaseUrl());
        pages.getLoginPage()
                .enterEmail(ConfigReader.getCustomerEmail())
                .enterPassword(ConfigReader.getDefaultPassword())
                .clickLoginButton();

    }

    @When("user navigates to the customer page")
    public void userNavigatesToTheCustomerPage() {
    }

    @Then("products should be visible on the page")
    public void productsShouldBeVisibleOnThePage() {
        assertTrue("Products should be visible on the customer page",
                pages.getProductPage().isProductsPageDisplayed());
    }
}
