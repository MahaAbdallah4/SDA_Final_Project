package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.ApiUtil;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;
import java.util.List;

public class StoresSteps {

    AllPages pages = new AllPages();
    Response response;
    List<String> uiStores;
    List<String> apiStores;

    @Given("Admin is logged in on the Dashboard")
    public void admin_is_logged_in_on_the_dashboard() {
        Driver.getDriver().get(ConfigReader.getBaseUrl() + "/login");

        pages.getLoginPage().login(
                ConfigReader.getAdminEmail(),
                ConfigReader.getDefaultPassword()
        );
    }

    @When("Admin navigates to {string} section")
    public void admin_navigates_to_section(String sectionName) {
        pages.getDashboardPage().navigateToPage("Store");
    }

    @Then("all stores should be displayed correctly on the page")
    public void all_stores_should_be_displayed_correctly_on_the_page() {
        uiStores = pages.getStoresPage().getStoreNames();
        Assert.assertFalse( "No stores displayed on UI!",uiStores.isEmpty());
    }

    @Then("each store should show correct {string}, {string}, and {string}")
    public void each_store_should_show_correct_and(String name, String id, String location) {
        pages.getStoresPage().validateStoreDetails();
    }

    @When("Admin validates the stores list through API endpoint {string}")
    public void admin_validates_the_stores_list_through_api_endpoint(String endpoint) {
        String token = ApiUtil.loginAndGetToken(
                ConfigReader.getAdminEmail(),
                ConfigReader.getDefaultPassword()
        );
        ApiUtil.setToken(token);
        response = ApiUtil.get(endpoint);

        System.out.println(" Full API Response: " + response.asPrettyString());

        apiStores = response.jsonPath().getList("name");

        System.out.println(" API Store Names found: " + apiStores);
    }



    @Then("the API response status code should be {int}")
    public void the_api_response_status_code_should_be(Integer expectedStatus) {
        ApiUtil.verifyStatusCode(response, expectedStatus);
    }

    @Then("the API response should match the UI store data")
    public void the_api_response_should_match_the_ui_store_data() {
        Assert.assertEquals("UI and API store data do not match!", apiStores, uiStores);
    }
}
