package com.bazaarstores.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

public class BrowseProductPage extends BasePage {

    private final By products = By.xpath("//div[@class='products-grid']");
    private final By productImg = By.xpath("//img[@class='product-image', contains('images')]");
    private final By productName = By.xpath("//h3[@class='product-name']");
    private final By productDescription = By.xpath("//p[@class='product-description']");
    private final By productPrice = By.xpath("//div[@class='product-price']");



    public boolean isProductsPageDisplayed() {
        return isDisplayed(products);
    }

    public boolean isProductImageDisplayed() {
        return isDisplayed(productImg);
    }

    public boolean isProductNameDisplayed() {
        return isDisplayed(productName);
    }

    public boolean isProductDescriptionDisplayed() {
        return isDisplayed(productDescription);
    }

    public boolean isProductPriceDisplayed() {
        return isDisplayed(productPrice);
    }

    public List<String> getAllProductDescriptions() {
        List<WebElement> descriptions = findElements(productDescription);
        return descriptions.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

}
