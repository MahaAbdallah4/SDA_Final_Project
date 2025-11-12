package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.pages.ProductPage;
import com.bazaarstores.pages.ViewManagerPage;
import com.bazaarstores.utilities.ApiUtil;
import com.bazaarstores.utilities.ApiUtilities;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class StoreManagerView {

    ProductPage productPage = new ProductPage();
    AllPages allPages = new AllPages();
    Response response;

    ViewManagerPage viewManagerPage = new ViewManagerPage();

    // UI Steps

    @Given("the user is logged in as a Store Manager")
    public void theUserIsLoggedInAsAStoreManager() {
        Driver.getDriver().get(ConfigReader.getBaseUrl());
        allPages.getLoginPage()
                .enterEmail(ConfigReader.getStoreManagerEmail())
                .enterPassword(ConfigReader.getDefaultPassword())
                .clickLoginButton();
        System.out.println(" Logged in as Store Manager");
    }
    @When("the user goes to the Menu and clicks Products")
    public void theUserGoesToTheMenuAndClicksProducts() {
        viewManagerPage.clickMenu();
        viewManagerPage.clickProductsLink();
    }

    @Then("all products should be displayed with correct name, stock, and price columns")
    public void allProductsShouldBeDisplayedWithCorrectNameStockAndPriceColumns() {
        Assert.assertTrue("Products not displayed!", viewManagerPage.verifyProductsDisplayed());
        Assert.assertTrue("Headers are incorrect!", viewManagerPage.verifyTableHeaders());
    }





    @Given("the API endpoint {string} is accessible")
    public void theAPIEndpointIsAccessible(String endpoint) {
        response = given().spec(ApiUtilities.spec()).when().get(endpoint);
        response.then().statusCode(200);
        System.out.println("✅ API endpoint accessible: " + endpoint);
    }

    @When("the user sends a GET request as a Manager")
    public void theUserSendsAGETRequestAsAManager() {
        response = given()
                .spec(ApiUtilities.spec())
                .header("Authorization", "Bearer " + ApiUtil.getToken())
                .when()
                .get("/products");
        response.prettyPrint();
    }

    @Then("the API should return all products with correct details")
    public void theAPIShouldReturnAllProductsWithCorrectDetails() {
        response.then().statusCode(200);
        List<String> productNames = response.jsonPath().getList("name");
        Assert.assertFalse("No products returned!", productNames.isEmpty());
        System.out.println(" API returned products: " + productNames.size());
    }}
