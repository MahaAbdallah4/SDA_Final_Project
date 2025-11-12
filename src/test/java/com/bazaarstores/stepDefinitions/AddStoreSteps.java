package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.pages.DashboardPage;
import com.bazaarstores.pages.LoginPage;
import com.bazaarstores.utilities.ApiUtil;
import com.bazaarstores.utilities.Driver;
import com.bazaarstores.utilities.ConfigReader;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class AddStoreSteps {
    private AllPages pages = new AllPages();
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private Response apiResponse;

    @Given("Admin is logged in")
    public void admin_is_logged_in() {
        loginPage = pages.getLoginPage();
        Driver.getDriver().get("https://bazaarstores.com/login");
        String email = ConfigReader.getAdminEmail();
        String password = ConfigReader.getDefaultPassword();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        dashboardPage = loginPage.clickLoginButton();
        String token = ApiUtil.loginAndGetToken(email, password);
        ApiUtil.setToken(token);
    }


    @When("I navigate to the Store page")
    public void iNavigateToTheStorePage() {
        dashboardPage = pages.getDashboardPage();
        dashboardPage.navigateToStores();
    }

    @When("I navigate to the Add Store page")
    public void navigate_to_add_store_page() {
        dashboardPage = pages.getDashboardPage();
        dashboardPage.navigateToAddStore();
    }

    @When("I enter the store name as {string}")
    public void i_enter_the_store_name_as(String storeName) {
        dashboardPage.enterStoreName(storeName); // Enter store name
    }

    @When("I enter the store description as {string}")
    public void i_enter_the_store_description_as(String storeDescription) {
        dashboardPage.enterStoreDescriptionWithClick(storeDescription); // Enter store description
    }

    @When("I enter the store location as {string}")
    public void i_enter_the_store_location_as(String storeLocation) {
        dashboardPage.enterStoreLocation(storeLocation); // Enter store location
    }

    @When("I select the admins {string}")
    public void i_select_the_admins(String admins) {
        dashboardPage.enterStoreAdmins(admins); // Enter the admins
    }

    @When("I submit the form")
    public void i_submit_the_form() {
        // Submit the store creation form
        dashboardPage.submitStoreForm();
    }

    @When("I call the API to retrieve the store list")
    public void callApiToRetrieveStoreList() {
        String endpoint = "/stores"; // Ensure this is the correct endpoint
        apiResponse = ApiUtil.get(endpoint); // Call the API to get the store list
        System.out.println("API Response Status: " + apiResponse.getStatusCode());
        System.out.println("API Response Body: " + apiResponse.getBody().asString());
    }

    @Then("store should be added successfully")
    public void store_should_be_added_successfully() {
        Assert.assertTrue("Store was not added successfully", dashboardPage.isStoreAddedSuccessfully());
    }

    @Then("an error message indicates store name already exists")
    public void an_error_message_indicates_store_name_already_exists() {
        Assert.assertTrue("Store was not added successfully", dashboardPage.isNotStoreAddedSuccessfully());
    }

    @Then("an error message appears for missing required fields")
    public void an_error_message_for_invalid_or_missing_inputs_appears() {
        Assert.assertTrue("Error message should be displayed", dashboardPage.isNotStoreAddedSuccessfully());
    }

    @Then("the store {string} should appear in the API response")
    public void verifyStoreExistsInApiResponse(String expectedStoreName) {
        // Check that the response contains the expected store name
        apiResponse.then().assertThat()
                .statusCode(200) // Ensure the response status is OK
                .body(containsString(expectedStoreName)); // Check if the response contains the store name
    }

}