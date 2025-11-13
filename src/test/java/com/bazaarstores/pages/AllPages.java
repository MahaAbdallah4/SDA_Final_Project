package com.bazaarstores.pages;

import org.openqa.selenium.WebDriver;

public class AllPages {
    private WebDriver driver;
    
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private DashboardPage dashboardPage;
    private AddProductPage addProductPage;
    private ProductsPage productsPage;
    private DeleteProductPage deleteProductPage;
    private UsersPage UsersPage;

    public AllPages(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);

        }
        return loginPage;
    }

    public RegistrationPage getRegistrationPage() {
        if (registrationPage == null) {
            registrationPage = new RegistrationPage(driver);
        }
        return registrationPage;
    }

    public DashboardPage getDashboardPage() {
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage(driver);
        }
        return dashboardPage;
    }
    public AddProductPage getAddProductPage() {
        if (addProductPage == null) {
            addProductPage = new AddProductPage(driver);
        }
        return addProductPage;
    }
    public ProductsPage getProductsPage(){
        if (productsPage == null){
            productsPage = new ProductsPage(driver);
        }
        return productsPage;
    }

    public DeleteProductPage getDeleteProductPage() {
        if (deleteProductPage == null) {
            deleteProductPage = new DeleteProductPage(driver);
        }
        return deleteProductPage;
    }
    public  UsersPage getUsersPage(){
        if (UsersPage == null){
            UsersPage = new UsersPage( driver);
        }
        return UsersPage;
    }


}

