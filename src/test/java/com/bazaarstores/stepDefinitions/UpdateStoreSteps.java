package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.pages.DashboardPage;
import com.bazaarstores.pages.LoginPage;
import com.bazaarstores.pages.StoreListPage;
import com.bazaarstores.utilities.ApiUtil;
import com.bazaarstores.utilities.Driver;
import com.bazaarstores.utilities.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;


public class UpdateStoreSteps {
    private AllPages pages = new AllPages();
    private Response apiResponse;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    // Constructor to initialize DashboardPage
    public UpdateStoreSteps() {
        dashboardPage = new DashboardPage();
    }

    @Given("Admin is logged in and store exists")
    public void admin_is_logged_in_and_store_exists() {
        String email = ConfigReader.getAdminEmail();
        String password = ConfigReader.getDefaultPassword();
        ApiUtil.loginAndGetToken(email, password);
        loginPage = pages.getLoginPage();
        Driver.getDriver().get("https://bazaarstores.com/login");
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        dashboardPage = loginPage.clickLoginButton();
        String token = ApiUtil.loginAndGetToken(email, password);
        ApiUtil.setToken(token);
    }

    @When("Admin navigate to the store list")
    public void navigate_to_store_list() {
        dashboardPage = pages.getDashboardPage();
        dashboardPage.navigateToStores();
    }

    @When("Admin select the store to edit")
    public void admin_select_the_store_to_edit() {
        // Check if any stores are available
        if (apiResponse.jsonPath().getList("").isEmpty()) {
            throw new RuntimeException("No stores available in the response.");
        }
        String storeName = apiResponse.jsonPath().getString("[0].name");
        StoreListPage storeListPage = new StoreListPage(Driver.getDriver());
        storeListPage.clickEditButton(storeName);
    }

    @When("Admin enter the store name as {string}")
    public void admin_enter_the_store_name_as(String storeName) {
        dashboardPage.enterStoreName(storeName);
    }

    @When("Admin enter the store description as {string}")
    public void admin_enter_the_store_description_as(String description) {
        dashboardPage.enterStoreDescriptionWithClick(description);
    }

    @When("Admin enter the store location as {string}")
    public void admin_enter_the_store_location_as(String location) {
        dashboardPage.enterStoreLocation(location);
    }

    @When("Admin select the admins {string}")
    public void admin_select_the_admins(String adminRole) {
        dashboardPage.enterStoreAdmins(adminRole);
    }

    @When("Admin submit the form")
    public void admin_submit_the_form() {
        dashboardPage.submitStoreForm();
    }

    @Then("store details should be updated successfully")
    public void store_details_should_be_updated_successfully() {
        Assert.assertTrue("Store was not added successfully", dashboardPage.isStoreAddedSuccessfully());
    }

    @Given("the store has been updated")
    public void the_store_has_been_updated() {
        // This step is already covered by previous steps,
    }


    @Then("updated store details should be displayed in the list")
    public void updated_store_details_should_be_displayed_in_the_list() {
        // Expected values
        String expectedStoreName = "My New Store";
        String expectedStoreDescription = "A great place to shop";
        String expectedStoreAdmins = "Store Manager";
        // Use Selenium to find the updated details in the HTML table
        WebDriver driver = Driver.getDriver();
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean found = false;
        // Iterate through the rows to find a match for the updated store details
        for (WebElement row : rows) {
            String name = row.findElement(By.xpath("./td[1]")).getText(); // NAME column
            String description = row.findElement(By.xpath("./td[2]")).getText(); // DESCRIPTION column
            String admin = row.findElement(By.xpath("./td[3]")).getText(); // ADMIN NAME column

            // Log the retrieved values for debugging
            System.out.println("Found Row - Name: " + name + ", Description: " + description +
                     ", Admin: " + admin);
            // Check for exact matches including description and location
            if (name.equals(expectedStoreName) &&
                    description.equals(expectedStoreDescription) &&
                    admin.equals(expectedStoreAdmins)) {
                found = true;
                break; // Exit loop if match is found
            }
        }
        // Assert that the updated store details were found in the table
        if (!found) {
            // Log expected vs. actual for further debugging
            System.out.println("Expected: [Name: " + expectedStoreName +
                    ", Description: " + expectedStoreDescription +
                    ", Admin: " + expectedStoreAdmins + "]");
            Assert.fail("Updated store details not found in the list.");
        }
    }


    @Then("an error message for invalid or missing inputs should appear")
    public void error_message_for_invalid_or_missing_inputs_should_appear() {
        Assert.assertTrue("Store was not updated successfully", dashboardPage.isNotStoreUpdatedSuccessfully());

    }

    @Given("Admin is logged in and store and store has been updated")
    public void adminIsLoggedInAndStoreAndStoreHasBeenUpdated() {
        String email = ConfigReader.getAdminEmail();
        String password = ConfigReader.getDefaultPassword();
        ApiUtil.loginAndGetToken(email, password);
        loginPage = pages.getLoginPage();
        Driver.getDriver().get("https://bazaarstores.com/login");
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        dashboardPage = loginPage.clickLoginButton();
    }
}