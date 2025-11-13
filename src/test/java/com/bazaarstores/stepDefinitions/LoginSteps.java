package com.bazaarstores.stepDefinitions;


import com.bazaarstores.pages.AllPages;
import com.bazaarstores.pages.DashboardPage;
import com.bazaarstores.utilities.ApiUtil;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class LoginSteps {

    AllPages allPages = new AllPages();

    private DashboardPage dashboardPage;


    @When("user enters valid customer credentials")
    public void user_enters_valid_customer_credentials() {
        allPages.getLoginPage()
                .enterEmail(ConfigReader.getCustomerEmail())
                .enterPassword(ConfigReader.getDefaultPassword());
    }

    @When("user enters email {string} and password {string}")
    public void user_enters_email_and_password(String email, String password) {
        allPages.getLoginPage()
                .enterEmail(email)
                .enterPassword(password);
    }

    @When("user clicks login button")
    public void user_clicks_login_button() {
        allPages.getLoginPage().clickLoginButton();
    }

    @Then("user should be logged in successfully")
    public void user_should_be_logged_in_successfully() {
        Assert.assertTrue("Dashboard should be displayed",
                allPages.getDashboardPage().isDashboardPageDisplayed());
    }

    @Then("admin should be logged in successfully")
    public void admin_should_be_logged_in_successfully() {
        Assert.assertTrue("Profile visit chart should be displayed",
                allPages.getDashboardPage().isProfileVisitChartDisplayed());
    }

    @Then("user should see error message")
    public void user_should_see_error_message() {
        Assert.assertTrue("Error message should be displayed",
                allPages.getLoginPage().isErrorMessageDisplayed());
    }

    @Then("^user should see error message \"([^\"]*)\"$")
    public void user_should_see_error_message(String expectedErrorMessage) {
        WebElement emailInput = Driver.getDriver().findElement(By.name("email")); // Adjust the locator as needed
        WebElement submitButton = Driver.getDriver().findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.attributeToBe(emailInput, "validationMessage", expectedErrorMessage));
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        String actualErrorMessage = (String) js.executeScript("return arguments[0].validationMessage;", emailInput);
        System.out.println("Actual validation message: " + actualErrorMessage);
        Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Then("user should remain on login page")
    public void user_should_remain_on_login_page() {
        Assert.assertTrue("Should remain on login page",
                allPages.getLoginPage().isLoginPageDisplayed());
    }

    // API Step - Example of using REST Assured
    @When("user logs in via API with valid credentials")
    public void user_logs_in_via_api_with_valid_credentials() {
        String token = ApiUtil.loginAndGetToken(
                ConfigReader.getCustomerEmail(),
                ConfigReader.getDefaultPassword()
        );
        Assert.assertNotNull("Token should not be null", token);
    }

    @Then("API should return success status code")
    public void api_should_return_success_status_code() {
        Response response = ApiUtil.get("/me");
        response.prettyPrint();
        ApiUtil.verifyStatusCode(response, 200);
    }

    @Then("user should see empty {string} error message")
    public void userShouldSeeEmptyErrorMessage(String field) {
        allPages.getLoginPage().isValidationMessageDisplayed(field);
    }

    @Given("user goes to homepage")
    public void userGoesToHomepage() {
        Driver.getDriver().get(ConfigReader.getBaseUrl());
    }
}