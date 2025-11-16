package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import com.github.javafaker.Faker;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RegistrationSteps {

    AllPages pages = new AllPages();
    public static String email;
    public static String fullName;

    @When("user clicks registration link")
    public void user_clicks_registration_link() {
        pages.getLoginPage().clickRegisterLink();
    }

    @And("user enters email for sign up {string}")
    public void userEntersEmailForSignUp(String email) {
        RegistrationSteps.email = Faker.instance().internet().emailAddress();
        if (email.equals("faker")) {
            pages.getRegistrationPage().enterEmail(RegistrationSteps.email);
        } else {
            pages.getRegistrationPage().enterEmail(email);
        }
    }

    @And("user enters full name for sign up {string}")
    public void userEntersFullNameForSignUp(String fullName) {
        RegistrationSteps.fullName = fullName;
        pages.getRegistrationPage().enterName(fullName);
    }


    @And("user enters password for sign up")
    public void userEntersPasswordForSignUp() {
        pages.getRegistrationPage().enterPassword(ConfigReader.getDefaultPassword());
    }

    @And("user enters confirm password for sign up")
    public void userEntersConfirmPasswordForSignUp() {
        pages.getRegistrationPage().enterPasswordConfirmation(ConfigReader.getDefaultPassword());
    }

    @And("user clicks the sing up button")
    public void userClicksTheSingUpButton() {
        pages.getRegistrationPage().clickSignUp();
    }

    @Then("user should see success message for registration")
    public void userShouldSeeSuccessMessageForRegistration() {
        //This is a bug! It is already reported!!!
        assert false;
    }

    @Then("user should see invalid email error message")
    public void userShouldSeeInvalidEmailErrorMessage() {
        pages.getRegistrationPage().validateInvalidEmail();
    }

    @Then("User should see: The name field is required. error message")
    public void userShouldSeeTheNameFieldIsRequiredErrorMessage() {
        pages.getRegistrationPage().validateNameFieldRequired();
    }

    @Then("user should see: invalid name message error")
    public void userShouldSeeInvalidNameMessageError() {
        //This is a bug!, there is no validation for invalid name, should not accept invalid name
        assert false;
    }

    @And("user enters short password for sign up {string}")
    public void userEntersShortPasswordForSignUp(String password) {
        pages.getRegistrationPage().enterPassword(password);
    }

    @And("user enters confirm password for sign up {string}")
    public void userEntersConfirmPasswordForSignUp(String password) {
        pages.getRegistrationPage().enterPasswordConfirmation(password);
    }

    @Then("user should see: The password field must be at least {int} characters. error message")
    public void userShouldSeeThePasswordFieldMustBeAtLeastCharactersErrorMessage(int atLeast6) {
        pages.getRegistrationPage().validatePasswordTooShort();
    }

    @Then("user should see: The password field confirmation does not match. error message")
    public void userShouldSeeThePasswordFieldConfirmationDoesNotMatchErrorMessage() {
        pages.getRegistrationPage().validatePasswordNotMatch();
    }

    @Given("user goes to homepage")
    public void userGoesToHomepage() {
        Driver.getDriver().get(ConfigReader.getBaseUrl());
    }
}
