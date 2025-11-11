package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class AddProductSteps {

    WebDriver driver = Driver.getDriver();
    AllPages allPages = new AllPages(driver);

    @Given("Store Manager is logged into BazaarStores")
    public void store_manager_is_logged_in() {
        driver.get(ConfigReader.getBaseUrl());
        allPages.getLoginPage().login(
                ConfigReader.getStoreManagerEmail(),
                ConfigReader.getDefaultPassword()
        );
    }

    @When("Store Manager navigates to {string} page")
    public void store_manager_navigates_to_add_product_page(String pageName) {
        allPages.getAddProductPage().navigateToAddProduct();
    }

    @When("leaves {string} and {string} fields empty")
    public void leaves_fields_empty(String name, String price) {
        allPages.getAddProductPage().leaveFieldsEmpty(name, price);
    }

    @When("clicks {string}")
    public void clicks_submit_button(String button) {
        allPages.getAddProductPage().clickSubmit();
    }

    @Then("an error message should appear for missing fields")
    public void error_message_should_appear() {
        Assert.assertTrue("Error message not displayed!", allPages.getAddProductPage().isErrorDisplayed());
    }

    @When("Store Manager fills all fields with valid data")
    public void store_manager_fills_all_fields_with_valid_data(io.cucumber.datatable.DataTable dataTable) {
        var data = dataTable.asMap(String.class, String.class);
        allPages.getAddProductPage().fillAllFields(
                data.get("Name"),
                data.get("Price"),
                data.get("Stock"),
                data.get("SKU")
        );
    }

    @Then("product should appear in the catalog")
    public void product_should_appear_in_the_catalog() {
        Assert.assertTrue("Product not found in catalog!", allPages.getAddProductPage().isProductInCatalog("T-shirt"));
    }
}
