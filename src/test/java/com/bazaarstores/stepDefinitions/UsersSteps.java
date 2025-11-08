package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.ApiUtil;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.bazaarstores.utilities.ApiUtil.verifyStatusCode;

public class UsersSteps {

    AllPages allPages = new AllPages();
    Response response;

    @Given("admin is logged in on the Dashboard")
    public void admin_is_logged_in_on_the_dashboard() {
        Driver.getDriver().get(ConfigReader.getBaseUrl());
        allPages.getLoginPage()
                .enterEmail(ConfigReader.getAdminEmail())
                .enterPassword(ConfigReader.getDefaultPassword())
                .clickLoginButton();
        Assert.assertTrue(allPages.getDashboardPage().isDashboardPageDisplayed());
    }

    @When("admin navigates to {string} page")
    public void admin_navigates_to_page(String pageName) {
        allPages.getDashboardPage().navigateToPage(pageName);
    }

    @When("admin clicks {string}")
    public void admin_clicks(String buttonName) {
        if (buttonName.equalsIgnoreCase("Add User")) {
            allPages.getUsersPage().clickAddUser();
        } else {
            Driver.getDriver().findElement(By.xpath("//button[contains(text(),'" + buttonName + "')]")).click();
        }
    }

    @When("admin fills user form with:")
    public void admin_fills_user_form_with(DataTable dataTable) {
        List<Map<String, String>> users = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> user : users) {

            WebElement name = Driver.getDriver().findElement(By.id("first-name-column"));
            WebElement email = Driver.getDriver().findElement(By.id("email-id-column"));
            WebElement role = Driver.getDriver().findElement(By.id("role-id-column"));
            WebElement password = Driver.getDriver().findElement(By.id("password-id-column"));
            WebElement passwordConfirmation = Driver.getDriver().findElement(By.name("password_confirmation"));

            name.clear();
            name.sendKeys(user.get("name"));

            email.clear();
            email.sendKeys(user.get("email"));

            Select select = new Select(role);
            select.selectByValue(user.get("role").toLowerCase());

            password.clear();
            password.sendKeys(user.get("password"));

            passwordConfirmation.clear();
            passwordConfirmation.sendKeys(user.get("Password Confirmation"));
        }
    }


    @When("admin submits the user form")
    public void admin_submits_the_user_form() {
        Driver.getDriver().findElement(By.xpath("//button[@type='submit' and contains(text(),'Submit')]")).click();
    }

    @Then("system should show success message {string}")
    public void system_should_show_success_message(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

        try {
            WebElement successToast = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'toast-success')]"))
            );
            Assert.assertTrue("Success toast message is not displayed", successToast.isDisplayed());
            System.out.println(" Success message appeared: " + successToast.getText());
        } catch (TimeoutException e) {
            WebElement errorAlert = Driver.getDriver().findElement(By.xpath("//div[contains(@class,'alert-danger')]"));
            System.out.println(" Error appeared: " + errorAlert.getText());
            Assert.fail("Expected success message, but got error: " + errorAlert.getText());
        }

    }

    @Then("the new user {string} should appear in the user list")
    public void the_new_user_should_appear_in_the_user_list(String email) {

        Driver.getDriver().navigate().to("https://bazaarstores.com/users");

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(20));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table")));

        boolean isUserVisible = wait.until(driver -> {
            List<WebElement> elements = driver.findElements(By.xpath("//table//*[contains(text(),'" + email + "')]"));
            return !elements.isEmpty();
        });

        Assert.assertTrue(" New user should appear in the list but was not found.", isUserVisible);
        System.out.println(" User " + email + " appeared successfully in the list!");
    }


    @When("admin opens edit for user {string}")
    public void admin_opens_edit_for_user(String email) {
        WebElement edit = Driver.getDriver().findElement(
                By.xpath("//td[contains(text(),'" + email + "')]/following-sibling::td//i[contains(@class,'bi-pencil-square')]"));
        edit.click();
    }

    @When("admin updates user fields:")
    public void admin_updates_user_fields(DataTable dataTable) {

        List<String> values = dataTable.asList(String.class);

        String roleValue = values.get(1);

        WebElement role = Driver.getDriver().findElement(By.id("role-id-column"));
        Select select = new Select(role);
        select.selectByValue(roleValue.toLowerCase());
    }

    @When("admin saves the changes")
    public void admin_saves_the_changes() {
        Driver.getDriver().findElement(By.xpath("//button[contains(text(),'Submit')]")).click();
    }

    @Then("user {string} should show:")
    public void user_should_show(String email, DataTable dataTable) {
        Driver.getDriver().navigate().refresh();   // ✅ لتحديث الصفحة بعد الحفظ

        List<String> values = dataTable.asList(String.class);
        String expectedRole = values.get(1);

        String pageSource = Driver.getDriver().getPageSource();

        Assert.assertTrue(pageSource.contains(email));

        Assert.assertTrue(pageSource.contains(expectedRole));

        System.out.println(" Verified that user " + email + " has role: " + expectedRole);
    }


    @When("admin clicks {string} beside user {string}")
    public void admin_clicks_action_beside_user(String action, String email) {
        WebElement userRow = Driver.getDriver().findElement(By.xpath("//td[contains(text(),'" + email + "')]/.."));
        if (action.equalsIgnoreCase("Delete")) {
            userRow.findElement(By.cssSelector("i.bi-trash3")).click();
        } else if (action.equalsIgnoreCase("Edit")) {
            userRow.findElement(By.cssSelector("i.bi-pencil-square")).click();
        }
    }

    @And("admin confirms deletion")
    public void admin_confirms_deletion() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Yes, delete it!')]")));
        confirmButton.click();
    }

    @And("admin cancels deletion")
    public void admin_cancels_deletion() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Cancel')]")));
        cancelButton.click();
    }

    @Then("system should show error message {string}")
    public void system_should_show_error_message(String expectedError) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        WebElement errorToast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.toast-message")));
        String actualError = errorToast.getText().trim();
        System.out.println("Error message appeared: " + actualError);
        Assert.assertEquals("Error message mismatch!", expectedError, actualError);
    }

    @Then("user {string} should remain in the list")
    public void user_should_remain_in_the_list(String email) {
        boolean userVisible = !Driver.getDriver()
                .findElements(By.xpath("//td[contains(text(),'" + email + "')]")).isEmpty();
        Assert.assertTrue("User should remain in the list", userVisible);
    }




    @When("admin gets all stores data via API endpoint {string}")
    public void adminGetsAllStoresDataViaAPIEndpoint(String endpoint) {
        response = ApiUtil.get(endpoint);
        response.prettyPrint();
    }

    @Then("verify the API response status code is {int}")
    public void verifyTheAPIResponseStatusCodeIs(Integer statusCode) {
        verifyStatusCode(response, statusCode);
    }

    @Then("verify store names are returned correctly via API")
    public void verifyStoreNamesAreReturnedCorrectlyViaAPI() {
        JsonPath jsonPath = response.jsonPath();
        List<String> storeNames = jsonPath.getList("data.name");
        Assert.assertNotNull("Store list should not be null", storeNames);
        Assert.assertTrue("Store list should not be empty", storeNames.size() > 0);
        System.out.println(" Total stores returned: " + storeNames.size());
    }

    @Then("verify store data includes {string}, {string}, and {string}")
    public void verifyStoreDataIncludesAnd(String nameField, String idField, String locationField) {
        JsonPath jsonPath = response.jsonPath();
        List<String> names = jsonPath.getList("data.name");
        List<Integer> ids = jsonPath.getList("data.id");
        List<String> locations = jsonPath.getList("data.location");

        Assert.assertEquals("Mismatch between name and id count", names.size(), ids.size());
        Assert.assertEquals("Mismatch between name and location count", names.size(), locations.size());

        System.out.println(" Verified that store data contains: " + nameField + ", " + idField + ", " + locationField);
    }

}