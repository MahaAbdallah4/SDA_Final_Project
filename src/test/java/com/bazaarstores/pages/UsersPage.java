package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.bazaarstores.utilities.Driver;

public class UsersPage {

    public UsersPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//a[@href='https://bazaarstores.com/users/create']")
    public WebElement addUserButton;

    public void clickAddUser() {
        addUserButton.click();
    }


    @FindBy(id = "first-name-column")
    public WebElement nameInput;

    @FindBy(id = "email-id-column")
    public WebElement emailInput;

    @FindBy(id = "role-id-column")
    public WebElement roleSelect;

    @FindBy(id = "password-id-column")
    public WebElement passwordInput;

    @FindBy(xpath = "//button[contains(text(),'Submit')]")
    public WebElement submitButton;

    @FindBy(xpath = "//button[contains(@onclick,'confirmDelete')]")
    public WebElement deleteButton;

    public void fillUserForm(String name, String email, String role, String password) {
        nameInput.clear();
        nameInput.sendKeys(name);
        emailInput.clear();
        emailInput.sendKeys(email);
        roleSelect.sendKeys(role);
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickSubmit() {
        submitButton.click();
    }
}
