package com.bazaarstores.pages;

import org.openqa.selenium.By;

public class ProductPage extends BasePage {

    private final By products = By.xpath("//div[@class='products-grid']");



    public boolean isProductsPageDisplayed() {
        return isDisplayed(products);
    }

}
