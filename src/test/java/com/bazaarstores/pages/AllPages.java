package com.bazaarstores.pages;

public class AllPages {

    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private DashboardPage dashboardPage;
    private UsersPage usersPage;
    private StoresPage storesPage;
    private BrowseProductPage productPage;



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

}