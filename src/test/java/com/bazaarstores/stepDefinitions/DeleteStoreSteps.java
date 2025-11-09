package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.pages.DashboardPage;
import com.bazaarstores.pages.LoginPage;
import com.bazaarstores.pages.StoreListPage;
import com.bazaarstores.utilities.ApiUtil;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

public class DeleteStoreSteps {
    private AllPages pages = new AllPages();
    private StoreListPage storeListPage;
    private DashboardPage dashboardPage;
    private LoginPage loginPage;
    private Response apiResponse;
    private String storeName; // Declare storeName here but initialize it later

    public DeleteStoreSteps() {
        dashboardPage = new DashboardPage();
        storeListPage = new StoreListPage(Driver.getDriver());
    }

    @Given("Logged in and store exists")
    public void logged_in_and_store_exists() {
        String email = ConfigReader.getAdminEmail();
        String password = ConfigReader.getDefaultPassword();
        ApiUtil.loginAndGetToken(email, password);
        loginPage = pages.getLoginPage();
        Driver.getDriver().get("https://bazaarstores.com/login");
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        dashboardPage = loginPage.clickLoginButton();

        // Fetch the list of stores to ensure at least one exists
        apiResponse = ApiUtil.get("/stores");
        Assert.assertEquals(200, apiResponse.getStatusCode());

        // Set storeName from the API response
        if (apiResponse.jsonPath().getList("").isEmpty()) {
            throw new RuntimeException("No stores available in the response.");
        }
        storeName = apiResponse.jsonPath().getString("[0].name"); // Initialize storeName
    }

    @When("Admin navigates to the store list")
    public void admin_navigates_to_the_store_list() {
        dashboardPage.navigateToStores();
    }

    @When("Admin clicks delete")
    public void admin_clicks_delete() {
        storeListPage.clickDeleteButton(storeName); // Pass the store name to delete
    }

    @Then("a confirmation dialog appears")
    public void a_confirmation_dialog_appears() {
        Assert.assertTrue("Confirmation dialog was not displayed", storeListPage.isConfirmationDialogDisplayed());
    }

    @When("Admin confirms deletion")
    public void admin_confirms_deletion() {
        storeListPage.confirmDeletion(); // This will confirm the deletion in the dialog
    }

    @Then("store is removed and no longer appears in the list")
    public void store_is_removed_and_no_longer_appears_in_the_list() {
        // Allow some time for the deletion to process if needed
        try {
            Thread.sleep(1000); // Sleep for 1 second (this is generally not best practice)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // After confirming deletion, check if the store is still displayed
        Assert.assertFalse("Store is still present in the list", storeListPage.isStoreDisplayed(storeName));
    }

    @When("Admin cancels deletion")
    public void admin_cancels_deletion() {
        storeListPage.cancelDeletion();
    }

    @Then("store remains in the list after cancellation")
    public void store_remains_in_the_list_after_cancellation() {
        Assert.assertTrue("Expected store to remain, but it has been removed.", storeListPage.isStoreDisplayed(storeName));
    }
}