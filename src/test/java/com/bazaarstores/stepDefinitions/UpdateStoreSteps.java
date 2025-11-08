package com.bazaarstores.stepDefinitions;

import com.bazaarstores.utilities.ApiUtil;
import com.bazaarstores.utilities.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.containsString;

public class UpdateStoreSteps {
    private Response apiResponse;
    private String selectedStoreId; // Variable to hold the selected store ID

    @Given("Admin is logged in and store exists")
    public void admin_is_logged_in_and_store_exists() {
        String email = ConfigReader.getAdminEmail();
        String password = ConfigReader.getDefaultPassword();
        ApiUtil.loginAndGetToken(email, password);
    }

    @When("Admin navigate to the store list")
    public void navigate_to_store_list() {
        apiResponse = ApiUtil.get("/stores"); // Call API to get store list
    }

    @When("Admin select the store to edit")
    public void admin_select_the_store_to_edit() {
        // Logic to select a specific store from the list
        // Replace with actual logic to retrieve the store ID
        selectedStoreId = "1"; // Example store ID
    }

    @When("Admin enter the store name as {string}")
    public void admin_enter_the_store_name_as(String storeName) {
        String requestBody = String.format("{\"name\":\"%s\"}", storeName);
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
        System.out.println("Update Store Name Request Body: " + requestBody);
    }

    @When("Admin enter the store description as {string}")
    public void admin_enter_the_store_description_as(String description) {
        String requestBody = String.format("{\"description\":\"%s\"}", description);
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
        System.out.println("Update Store Description Request Body: " + requestBody);
    }

    @When("Admin enter the store location as {string}")
    public void admin_enter_the_store_location_as(String location) {
        String requestBody = String.format("{\"location\":\"%s\"}", location);
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
        System.out.println("Update Store Location Request Body: " + requestBody);
    }

    @When("Admin select the admins {string}")
    public void admin_select_the_admins(String adminRole) {
        String requestBody = String.format("{\"admins\":\"%s\"}", adminRole);
        apiResponse = ApiUtil.put("/stores/" + selectedStoreId, requestBody);
        System.out.println("Update Store Admins Request Body: " + requestBody);
    }

    @Then("store details should be updated successfully")
    public void store_details_should_be_updated_successfully() {
        apiResponse.then().assertThat().statusCode(200);
        // Print the response body for debugging
        System.out.println("Response Body: " + apiResponse.getBody().asString());
    }

    @Given("the store has been updated")
    public void the_store_has_been_updated() {
        // This is handled in the previous steps
    }

    @When("Admin navigate to the store list again")
    public void admin_navigate_to_store_list_again() {
        apiResponse = ApiUtil.get("/stores"); // Call API to get updated store list
    }

    @Then("updated store details should be displayed in the list")
    public void updated_store_details_should_be_displayed_in_the_list() {
        // Logic to verify updated store details in the list
        // This may involve checking the response for the updated store
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