package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.DashboardPage;
import com.bazaarstores.pages.EditProductsPage;
import com.bazaarstores.pages.LoginPage;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditProductsSteps {

    WebDriver driver;
    LoginPage loginPage;
    DashboardPage dashboardPage;
    EditProductsPage editproductsPage;
    WebDriverWait wait;

    public EditProductsSteps() {
        this.driver = Driver.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));


        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
        editproductsPage = new EditProductsPage();
    }

    @Given("the store manager is on the {string} page")
    public void store_manager_is_on_page(String pageName) {

        driver.get("https://bazaarstores.com/");


        if (loginPage.isLoginPageDisplayed()) {
            dashboardPage = loginPage.login("storemanager@sda.com", "Password.12345");
        }


        switch (pageName.toLowerCase()) {
            case "products":
                editproductsPage.clickProductsMenu();
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
        editproductsPage.clickEditButton(productName);
        System.out.println("Clicked edit for product: " + productName);
    }

    @When("the store manager updates the product name to {string}")
    public void store_manager_updates_product_name(String name) {
        editproductsPage.updateProductName(name);
        System.out.println("Updated product name to: " + name);
    }

    @When("the store manager updates the product price to {string}")
    public void store_manager_updates_product_price(String price) {
        editproductsPage.updateProductPrice(price);
        System.out.println("Updated product price to: " + price);
    }

    @When("the store manager updates the product stock to {string}")
    public void store_manager_updates_product_stock(String stock) {
        editproductsPage.updateProductStock(stock);
        System.out.println("Updated product stock to: " + stock);
    }

    @When("the store manager clicks Submit")
    public void store_manager_clicks_Submit() {
        editproductsPage.clickSave();
        System.out.println("Clicked Submit button");
    }

    @Then("the product should be updated successfully")
    public void product_should_be_updated_successfully() {
        try {
            boolean isDisplayed = editproductsPage.isSuccessMessageDisplayed();
            String successText = editproductsPage.getSuccessMessageText();

            Assert.assertTrue("Success message not displayed!", isDisplayed);
            Assert.assertTrue("Unexpected success message text!",
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
            String errorText = editproductsPage.getErrorMessageText();

            if (!errorText.isEmpty()) {
                System.out.println("Error message displayed: " + errorText);
                Assert.assertFalse("Error message should not be empty", errorText.isEmpty());
            } else {
                String validationMsg = editproductsPage.getProductValidationMessage(
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

