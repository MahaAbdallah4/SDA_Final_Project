package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

import java.util.List;

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

    @When("user refreshes or opens the product page")
    public void userRefreshesOrOpensTheProductPage() {
        pages.getProductPage().refreshPage();
    }

    @Then("products should load quickly")
    public void productsShouldLoadQuickly() {

        long startTime = System.currentTimeMillis();

        pages.getProductPage().waitForElementToBeVisible(By.xpath("//div[@class='products-grid']"));

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        System.out.println("Page load time: " + loadTime + " ms");

        assertTrue("Products took too long to load: " + loadTime + " ms", loadTime < 1000);

    }

    @And("each product should display correct Name, Price, Description, and Image")
    public void eachProductShouldDisplayCorrectNamePriceDescriptionAndImage() {
        assertTrue("Product image should be displayed",
                pages.getProductPage().isProductImageDisplayed());

        assertTrue("Product name should be displayed",
                pages.getProductPage().isProductNameDisplayed());

        assertTrue("Product description should be displayed",
                pages.getProductPage().isProductDescriptionDisplayed());

        assertTrue("Product price should be displayed",
                pages.getProductPage().isProductPriceDisplayed());
    }

    @And("each product should display valid description without HTML tags")
    public void eachProductShouldDisplayValidDescriptionWithoutHTMLTags() {

        List<String> descriptions = pages.getProductPage().getAllProductDescriptions();

        for (String desc : descriptions) {
            System.out.println("Checking description: " + desc);

            //This is a bug! descriptions contain HTML tags.
            assertTrue("Product description should not contain HTML tags",
                    !desc.matches(".*<[^>]+>.*"));
        }

    }
}
