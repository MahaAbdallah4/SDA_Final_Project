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
    private String storeId;
    private String storeName;

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

        // Set storeId from the API response
        if (apiResponse.jsonPath().getList("").isEmpty()) {
            throw new RuntimeException("No stores available in the response.");
        }

        // Original line:
        storeId = apiResponse.jsonPath().getString("[0].id");

        // FIX: Add assignment for storeName from the API response
        storeName = apiResponse.jsonPath().getString("[0].name"); // <-- Add this line

        // Now navigate to the UI list page
        dashboardPage.navigateToStores();
    }

    @When("Admin navigates to the store list")
    public void admin_navigates_to_the_store_list() {
        dashboardPage.navigateToStores();
    }

    @When("Admin clicks delete")
    public void admin_clicks_delete() {
        // *** FIX 1: Use the explicit clickDeleteButton method to initiate the action.
        // We will call the delete button click here, and rely on the confirmation
        // dialog steps to handle the dialog.
        storeListPage.clickDeleteButtonOfFirstStore(); // We'll add this new method to StoreListPage
    }

    @Then("a confirmation dialog appears")
    public void a_confirmation_dialog_appears() {
        Assert.assertTrue("Confirmation dialog was not displayed", storeListPage.isConfirmationDialogDisplayed());
    }

    @When("Admin confirms deletion")
    public void admin_confirms_deletion() {
        storeListPage.confirmDeletion();
    }

    @Then("store is removed and no longer appears in the list")
    public void store_is_removed_and_no_longer_appears_in_the_list() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assert.assertFalse("Store is still present in the list", storeListPage.isStoreDisplayedById(storeId));
    }

    @When("Admin cancels deletion")
    public void admin_cancels_deletion() {
        storeListPage.cancelDeletion();
    }

    @Then("store remains in the list after cancellation")
    public void store_remains_in_the_list_after_cancellation() {
        Assert.assertTrue("Expected store to remain, but it has been removed after cancellation.",
                storeListPage.isStoreDisplayed(storeName));
    }
}