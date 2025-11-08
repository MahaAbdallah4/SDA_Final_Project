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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.containsString;

public class UpdateStoreSteps {
    private AllPages pages = new AllPages();
    private Response apiResponse;
    private String selectedStoreId;
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
        apiResponse = ApiUtil.get("/stores");
        apiResponse.then().assertThat().statusCode(200);
        dashboardPage = pages.getDashboardPage();
        dashboardPage.navigateToStores();
    }

    @When("Admin select the store to edit")
    public void admin_select_the_store_to_edit() {
        // Retrieve the store name from the API response
        String storeName = apiResponse.jsonPath().getString("[0].name"); // Adjust the path based on your response structure

        // Check if the store name is retrieved successfully
        if (storeName == null) {
            throw new RuntimeException("No store name found in the response.");
        }

        // Log the selected store name
        System.out.println("Selected Store Name: " + storeName);

        // Click the edit button for the selected store
        StoreListPage storeListPage = new StoreListPage(Driver.getDriver());
        storeListPage.clickEditButton(storeName);
    }

    @When("Admin enter the store name as {string}")
    public void admin_enter_the_store_name_as(String storeName) {
        dashboardPage.enterStoreName(storeName); // Enter store name
        String requestBody = String.format("{\"name\":\"%s\"}", storeName);
        System.out.println("Update Store Name Request Body: " + requestBody); // Log request body
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
    }

    @When("Admin enter the store description as {string}")
    public void admin_enter_the_store_description_as(String description) {
        dashboardPage.enterStoreDescriptionWithClick(description); // Enter store description
        String requestBody = String.format("{\"description\":\"%s\"}", description);
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
        System.out.println("Update Store Description Request Body: " + requestBody);
    }

    @When("Admin enter the store location as {string}")
    public void admin_enter_the_store_location_as(String location) {
        dashboardPage.enterStoreLocation(location); // Enter store location
        String requestBody = String.format("{\"location\":\"%s\"}", location);
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
        System.out.println("Update Store Location Request Body: " + requestBody);
    }

    @When("Admin select the admins {string}")
    public void admin_select_the_admins(String adminRole) {
        dashboardPage.enterStoreAdmins(adminRole); // Enter the admins
        String requestBody = String.format("{\"admins\":\"%s\"}", adminRole);
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
        System.out.println("Update Store Admins Request Body: " + requestBody);
    }

    @Then("store details should be updated successfully")
    public void store_details_should_be_updated_successfully() {
        // Check the API response status code
        if (apiResponse.getStatusCode() != 200) {
            System.out.println("Response Body: " + apiResponse.getBody().asString());
        }
        apiResponse.then().assertThat().statusCode(200);
        Assert.assertTrue("Store was not added successfully", dashboardPage.isStoreAddedSuccessfully());
    }

    @When("Admin clear the required fields")
    public void admin_clear_required_fields() {
        String requestBody = "{\"name\":\"\", \"description\":\"\", \"location\":\"\"}";
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
    }

    @Then("an error message for invalid or missing inputs should appear")
    public void error_message_for_invalid_or_missing_inputs_should_appear() {
        apiResponse.then().assertThat().statusCode(400);
        apiResponse.then().body("error", containsString("Invalid or missing inputs"));
    }
}