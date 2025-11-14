package com.bazaarstores.pages;

import org.openqa.selenium.WebDriver;

public class AllPages {

    private WebDriver driver;

    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private DashboardPage dashboardPage;
    private UsersPage usersPage;
    private StoresPage storesPage;
    private BrowseProductPage productPage;
    private AddProductPage addProductPage;
    private EditProductsPage editProductsPage;
    private DeleteProductPage deleteProductPage;
    private UsersviewPage usersviewPage;






    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    public RegistrationPage getRegistrationPage() {
        if (registrationPage == null) {
            registrationPage = new RegistrationPage();
        }
        return registrationPage;
    }

    public DashboardPage getDashboardPage() {
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        return dashboardPage;
    }

    public UsersPage getUsersPage() {

        if (usersPage == null){
            usersPage = new UsersPage();
        }
        return usersPage;
    }

    public StoresPage getStoresPage() {
        if (storesPage == null) storesPage = new StoresPage();
        return storesPage;
    }

    public BrowseProductPage getProductPage() {
        if (productPage == null) {
            productPage = new BrowseProductPage();
        }
        return productPage;
    }
    public AddProductPage getAddProductPage() {
        if (addProductPage == null) {
            addProductPage = new AddProductPage();
        }
        return addProductPage;
    }
    public EditProductsPage getEditProductsPage(){
        if (editProductsPage == null){
            editProductsPage = new EditProductsPage();
        }
        return editProductsPage;
    }
    public DeleteProductPage getDeleteProductPage() {
        if (deleteProductPage == null) {
            deleteProductPage = new DeleteProductPage();
        }
        return deleteProductPage;
    }
    public UsersviewPage getUsersviewPage(){
   if (usersviewPage == null){
       usersviewPage = new UsersviewPage();
   }
   return usersviewPage;
    }

}