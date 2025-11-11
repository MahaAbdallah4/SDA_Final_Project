package com.bazaarstores.pages;

import com.bazaarstores.utilities.Driver;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ProductPage extends BasePage {

    // ---------- Locators ----------
    private By productCards = By.xpath("//div[contains(@class,'product-card')]");
    public String productCardByName = "//div[contains(@class,'product-card')]//h3[normalize-space(text())='%s']/ancestor::div[contains(@class,'product-card')]";
    private By cartCount = By.cssSelector(".cart-count, .cart-badge, [class*='cart-count']");
    private final By addToCartSuccessMessage = By.xpath("//*[contains(@class,\'toast\') or contains(translate(normalize-space(.),\'ABCDEFGHIJKLMNOPQRSTUVWXYZ\',\'abcdefghijklmnopqrstuvwxyz\'),\'added\') or contains(translate(normalize-space(.),\'ABCDEFGHIJKLMNOPQRSTUVWXYZ\',\'abcdefghijklmnopqrstuvwxyz\'),\'success\') or contains(translate(normalize-space(.),\'ABCDEFGHIJKLMNOPQRSTUVWXYZ\',\'abcdefghijklmnopqrstuvwxyz\'),\'added to cart\')]");

    private By productName = By.cssSelector(".product-name");
    private By productPrice = By.cssSelector(".current-price");
    private By productDescription = By.cssSelector(".product-description");
    private By productImage = By.cssSelector(".product-image");

    @FindBy(xpath = "//input[@type='search' or @placeholder='Search']")
    public WebElement searchInput;

    @FindBy(xpath = "//button[contains(@type,'submit') or contains(@class,'search')]")
    public WebElement searchButton;

    @FindBy(xpath = "//span[contains(@class,'cart-count') or @id='cart-count']")
    public WebElement cartCountElement;

    @FindBy(xpath = "//div[contains(@class,'toast') or contains(text(),'added')]")
    public WebElement successToast;

    @FindBy(xpath = "//button[contains(@class,'add-to-cart') or contains(text(),'Add to Cart')]")
    public WebElement addToCartButton;

    @FindBy(xpath = "//button[contains(@class,'wishlist') or contains(@title,'Add to Wishlist')]")
    public WebElement addToWishlistButton;

    @FindBy(xpath = "//a[contains(@href,'wishlist') or contains(@class,'wishlist-link')]")
    public WebElement wishlistIcon;


    // ---------- Navigation ----------
    public void clickHomepage() {
        By homeLink = By.xpath("//a[normalize-space(text())='Home']");
        waitForElementToBeVisible(homeLink);
        click(homeLink);
    }


    public boolean isHomePageDisplayed() {
        String currentUrl = Driver.getDriver().getCurrentUrl();
        return currentUrl.equalsIgnoreCase("https://bazaarstores.com/customer") ||
                currentUrl.equalsIgnoreCase("https://bazaarstores.com/customer/") ||
                currentUrl.equalsIgnoreCase("https://bazaarstores.com/") ||
                Driver.getDriver().getTitle().toLowerCase().contains("bazaar");
    }


    // ---------- Core Product Actions ----------
    public void openProductPage(String productName) {
        By product = By.xpath(String.format(productCardByName, productName));
        waitForElementToBeVisible(product);
        click(product);
    }

    public boolean isProductDisplayed(String productName) {
        By product = By.xpath(String.format("//h3[@class='product-name' and normalize-space(text())='%s']", productName));
        return isElementDisplayed(product);
    }

    public int getTotalProductCount() {
        List<WebElement> products = findElements(productCards);
        return products.size();
    }

    public void searchProduct(String productName) {
        WebElement element = Driver.getDriver().findElement(By.xpath("//h3[normalize-space(text())='" + productName + "']"));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.click();
    }

    public void hoverOverCartIcon() {
        hoverOver(cartCount);
    }

    int previousCartCount;

    public int getPreviousCartCount() {
        return previousCartCount;
    }


    // ---------- Add to Cart / Wishlist ----------
    // ---------- Add to Cart / Wishlist ----------
    public void clickAddToCart(String productName) {
        By addToCartBtn = By.xpath(
                "//div[contains(@class,'product-card')]//h3[contains(normalize-space(.),'" + productName + "')]"
                        + "/ancestor::div[contains(@class,'product-card')]//button[contains(@class,'add-to-cart')]"
        );

        // Wait for the button to exist and scroll into view
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(addToCartBtn));
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView({block:'center'});", button);

        // Wait until clickable and then click
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".toast, .cart-button.view-cart")));

    }

    public void addToCartAndWait(String productName) {
        int previousCount = getCartCount();

        // Click the Add to Cart button
        clickAddToCart(productName);

        // Wait until the product appears in the cart or cart count increases
        try {
            wait.until(driver -> getCartCount() > previousCount || isProductInCart(productName) || isAddToCartMessageVisible());
        } catch (TimeoutException e) {
            System.out.println("Cart update not detected within wait, product may still be added.");
        }
    }


    public void clickFavoriteIcon(String productName) {
        // Locate the favorite div for this product
        By favDiv = By.xpath(
                "//div[contains(@class,'product-card')]//h3[normalize-space(text())='" + productName + "']"
                        + "/ancestor::div[contains(@class,'product-card')]//div[contains(@class,'favorite-icon')]"
        );

        // Scroll into view
        WebElement element = Driver.getDriver().findElement(favDiv);
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", element);

        // Click via JS
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", element);

        // Wait for possible alert modal
        try {
            By alertButton = By.xpath("//button[normalize-space(text())='Cool']");
            WebDriverWait shortWait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(3));
            WebElement coolButton = shortWait.until(ExpectedConditions.elementToBeClickable(alertButton));
            coolButton.click(); // dismiss alert
        } catch (TimeoutException e) {
            // No alert appeared, nothing to do
        }

        // Optional: small pause for UI update
        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {
        }
    }


    // Check if heart icon is filled (active)
    public boolean isHeartIconFilled(String productName) {
        By heartIcon = By.xpath(
                "//div[contains(@class,'product-card')]//h3[normalize-space(text())='" + productName + "']"
                        + "/ancestor::div[contains(@class,'product-card')]//div[contains(@class,'favorite-icon')]/i"
        );

        try {
            WebElement icon = findElement(heartIcon);
            String classes = icon.getAttribute("class"); // e.g., "fas fa-heart"
            return classes.contains("fas"); // 'fas' = filled, 'far' = outline
        } catch (Exception e) {
            return false;
        }
    }


    // Check if heart icon is empty (outline)
    public boolean isHeartIconEmpty(String productName) {
        By heartIcon = By.xpath(
                "//div[contains(@class,'product-card')]//h3[normalize-space(text())='" + productName + "']" +
                        "/ancestor::div[contains(@class,'product-card')]//i[contains(@class,'fa-heart') and contains(@class,'far')]"
        );
        return isElementDisplayed(heartIcon);
    }


    public void hoverOverProductCard(String productName) {
        By productCard = By.xpath(
                "//div[contains(@class,'product-card')]//h3[normalize-space(text())='" + productName + "']" +
                        "/ancestor::div[contains(@class,'product-card')]"
        );
        waitForElementToBeVisible(productCard);
        hoverOver(productCard);
    }

    public void openWishlist() {
        try {
            // If there’s a proper icon, click it
            if (isElementDisplayed(By.xpath("//a[contains(@href,'/favorites')]"))) {
                click(By.xpath("//a[contains(@href,'/favorites')]"));
            } else {
                // fallback: navigate directly
                Driver.getDriver().navigate().to("https://bazaarstores.com/favorites");
            }
        } catch (Exception e) {
            throw new RuntimeException("Wishlist icon not found or not clickable!", e);
        }
    }

    public boolean isProductInFavorites(String productName) {
        // Assuming favorite items have a class 'favorite-item' and name inside 'favorite-item-name'
        By favProduct = By.xpath("//div[contains(@class,'favorite-item')]//div[contains(@class,'favorite-item-name') and normalize-space(text())='" + productName + "']");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(favProduct));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    // ---------- Verifications ----------
//    public void waitForAddToCartSuccess() {
//        int previousCount = getCartCount();
//        clickAddToCart();
//        wait.until(driver -> getCartCount() > previousCount);
//    }


    public int getCartCount() {
        try {
            String text = wait.until(ExpectedConditions.visibilityOf(cartCountElement)).getText().trim();
            // Keep only digits
            text = text.replaceAll("\\D", "");
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }

    }

    public boolean isAddToCartMessageVisible() {
        try {
            return Driver.getDriver().findElement(addToCartSuccessMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForSweetAlert() {
        By swalAlert = By.cssSelector(".swal2-popup.swal2-toast, .swal2-container");
        wait.until(ExpectedConditions.visibilityOfElementLocated(swalAlert));
    }

    public int waitForCartCountToChange(int previousCount) {
        try {
            wait.until(driver -> getCartCount() > previousCount);
        } catch (TimeoutException e) {
            // ignore
        }
        return getCartCount();
    }


    // ---------- Product Details ----------
    public String getProductName() {
        waitForElementToBeVisible(productName);
        return Driver.getDriver().findElement(productName).getText();
    }

    public String getProductPrice() {
        waitForElementToBeVisible(productPrice);
        return Driver.getDriver().findElement(productPrice).getText();
    }

    public String getProductDescription() {
        waitForElementToBeVisible(productDescription);
        return Driver.getDriver().findElement(productDescription).getText();
    }

    public boolean isProductImageDisplayed() {
        waitForElementToBeVisible(productImage);
        WebElement image = Driver.getDriver().findElement(productImage);
        return image.isDisplayed();
    }

    // ---------- Helper ----------
    public boolean isElementDisplayed(By locator) {
        try {
            return Driver.getDriver().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ProductPage.java
    public boolean isProductVisibleInCart(String productName) {
        By productLocator = By.xpath("//div[contains(@class,'cart-item')]//div[contains(@class,'cart-item-name') and normalize-space(text())='" + productName + "']");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(productLocator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public boolean isCartSubtotalDisplayed() {
        By subtotalLocator = By.cssSelector("div.cart-subtotal");
        return isElementDisplayed(subtotalLocator);
    }

    public String getCartSubtotal() {
        By subtotalLocator = By.cssSelector("div.cart-subtotal span:last-child");
        return getText(subtotalLocator);
    }


    // Get list of all product names in cart
    public List<String> getAllCartProductNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> elements = findElements(By.xpath("//div[contains(@class,'cart-item')]//div[contains(@class,'cart-item-name')]"));

        for (WebElement el : elements) {
            try {
                String name = el.getText().trim();
                // Remove any unwanted prefixes like 'Product Name:'
                if (name.toLowerCase().startsWith("product name")) {
                    name = name.split(":")[1].trim();
                }
                names.add(name);
            } catch (StaleElementReferenceException e) {
                WebElement freshEl = findElement(By.xpath(".//div[contains(@class,'cart-item-name')]"));
                names.add(freshEl.getText().trim());
            }
        }
        return names;
    }


    // Check if product is in cart
    public boolean isProductInCart(String productName) {
        try {
            List<String> cartNames = getAllCartProductNames();
            return cartNames.contains(productName);
        } catch (StaleElementReferenceException e) {
            // Retry once if DOM refreshed
            return getAllCartProductNames().contains(productName);
        }
    }
    public boolean isProductaddedInCart(String productName) {
        try {
            hoverOverCartIcon();

            By cartDropdown = By.cssSelector(".cart-dropdown, .mini-cart, .dropdown-menu");
            By productInCart = By.xpath("//*[contains(@class,'cart')]//*[contains(text(),'" + productName + "')]");

            // Retry finding product a few times (in case cart updates slowly)
            for (int i = 0; i < 5; i++) {
                try {
                    List<WebElement> products = Driver.getDriver().findElements(productInCart);
                    if (!products.isEmpty() && products.get(0).isDisplayed()) {
                        return true;
                    }
                    Thread.sleep(500); // short pause before retry
                } catch (StaleElementReferenceException e) {
                    // Element became stale; retry after re-finding
                    Thread.sleep(500);
                }
            }

            return false;

        } catch (Exception e) {
            System.out.println("⚠️ Product not visible in cart: " + e.getMessage());
            return false;
        }
    }




    // Get product price dynamically by name
    public String getProductPriceFromCart(String productName) {
        By priceLocator = By.xpath(
                "//div[contains(@class,'cart-item')]//div[contains(@class,'cart-item-name') and normalize-space(text())='"
                        + productName + "']/following-sibling::div[contains(@class,'cart-item-price')]"
        );
        waitForElementToBeVisible(priceLocator);
        return getText(priceLocator);
    }


    public void clearAllCartItems() {
        List<WebElement> removeButtons = Driver.getDriver().findElements(By.cssSelector("button.remove-item"));

        // Loop until no remove buttons are left
        while (!removeButtons.isEmpty()) {
            for (WebElement removeBtn : removeButtons) {
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(removeBtn)).click();
                    Thread.sleep(500); // wait a bit for the cart to update
                } catch (StaleElementReferenceException | InterruptedException e) {
                    System.out.println("Retrying removal due to: " + e.getMessage());
                }
            }
            // Refresh the list after each pass
            removeButtons = Driver.getDriver().findElements(By.cssSelector("button.remove-item"));
        }
    }



    // Hover over cart and check for "View Cart" visibility
    public boolean isViewCartVisibleAfterHover() {
        // Hover over cart icon
        hoverOver(By.cssSelector(".cart-icon, .header-cart")); // adjust selector as per your header

        // Wait up to 5 seconds for the "View Cart" button to appear
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(byXpathContainsTextIgnoreCase("View Cart")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickViewCart() {
        By viewCartButton = By.cssSelector("a.cart-button.view-cart");
        click(viewCartButton);
        // wait until URL contains 'cart'
        new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10))
                .until(driver -> Driver.getDriver().getCurrentUrl().contains("/cart"));
    }

    public boolean isProductInCartVisible(String productName) {
        By productLocator = By.xpath(
                "//div[contains(@class,'cart-subtotal')]/following-sibling::div//div[contains(@class,'cart-item-name') and normalize-space(text())='" + productName + "']"
        );

        try {
            // Wait up to 5 seconds for the item to appear in the mini-cart
            new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(productLocator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


}

