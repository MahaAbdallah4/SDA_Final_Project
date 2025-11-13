package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.DashboardPage;
import com.bazaarstores.pages.LoginPage;
import com.bazaarstores.pages.ProductsPage;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductsSteps {

    WebDriver driver;
    LoginPage loginPage;
    DashboardPage dashboardPage;
    ProductsPage productsPage;
    WebDriverWait wait;

    public ProductsSteps() {
        this.driver = Driver.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        productsPage = new ProductsPage(driver);
    }

    @Given("the store manager is on the {string} page")
    public void store_manager_is_on_page(String pageName) {
        if (loginPage.isLoginPageDisplayed()) {
            dashboardPage = loginPage.login("storemanager@sda.com", "Password.12345");
        }

        switch (pageName.toLowerCase()) {
            case "products":
                productsPage.clickProductsMenu();
                break;
            case "dashboard":
                Assert.assertTrue("Dashboard page not displayed!", dashboardPage.isDashboardPageDisplayed());
                break;
            default:
                throw new RuntimeException("Page name not recognized: " + pageName);
        }
        System.out.println("Navigated to: " + pageName + " page");
    }

    @When("the store manager clicks edit for product {string}")
    public void store_manager_clicks_edit_for_product(String productName) {
        productsPage.clickEditButton(productName);
        System.out.println(" Clicked edit for product: " + productName);
    }

    @When("the store manager updates the product name to {string}")
    public void store_manager_updates_product_name(String name) {
        productsPage.updateProductName(name);
        System.out.println("Updated product name to: " + name);
    }

    @When("the store manager updates the product price to {string}")
    public void store_manager_updates_product_price(String price) {
        productsPage.updateProductPrice(price);
        System.out.println("Updated product price to: " + price);
    }

    @When("the store manager updates the product stock to {string}")
    public void store_manager_updates_product_stock(String stock) {
        productsPage.updateProductStock(stock);
        System.out.println("Updated product stock to: " + stock);
    }

    @When("the store manager clicks Submit")
    public void store_manager_clicks_Submit() {
        productsPage.clickSave();
        System.out.println("Clicked Submit button");
    }

    @Then("the product should be updated successfully")
    public void product_should_be_updated_successfully() {
        try {

            boolean isDisplayed = productsPage.isSuccessMessageDisplayed();
            String successText = productsPage.getSuccessMessageText();

            Assert.assertTrue(" Success message not displayed!", isDisplayed);
            Assert.assertTrue(" Unexpected success message text!",
                    successText.toLowerCase().contains("product updated successfully") ||
                            successText.toLowerCase().contains("success"));

            System.out.println("Success message displayed: " + successText);

        } catch (TimeoutException e) {
            Assert.fail("Success message not displayed within time limit!");
        }
    }
    @Then("an error message should be displayed for invalid or missing fields")
    public void an_error_message_should_be_displayed_for_invalid_or_missing_fields() {
        try {

            String errorText = productsPage.getErrorMessageText();

            if (!errorText.isEmpty()) {
                System.out.println("Error message displayed: " + errorText);
                Assert.assertFalse("Error message should not be empty", errorText.isEmpty());
            } else {

                String validationMsg = productsPage.getProductValidationMessage(
                        By.cssSelector("input[name='name']")
                );

                System.out.println("Browser Validation Message: " + validationMsg);
                Assert.assertFalse("Validation message should not be empty", validationMsg.isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Failed to capture validation or error message: " + e.getMessage());
        }
    }
}


