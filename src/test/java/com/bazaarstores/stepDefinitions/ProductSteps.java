package com.bazaarstores.stepDefinitions;

import com.bazaarstores.pages.AllPages;
import com.bazaarstores.pages.BasePage;
import com.bazaarstores.pages.ProductPage;
import com.bazaarstores.utilities.ApiUtilities;
import com.bazaarstores.utilities.ConfigReader;
import com.bazaarstores.utilities.Driver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class ProductSteps {


    ProductPage productPage = new ProductPage();
    AllPages allPages = new AllPages();
    Response response;


    //UI


    @Given("user is logged in as a customer")
    public void user_is_logged_in_as_a_customer() {
        Driver.getDriver().get(ConfigReader.getBaseUrl());
        allPages.getLoginPage()
                .enterEmail(ConfigReader.getCustomerEmail())
                .enterPassword(ConfigReader.getDefaultPassword())
                .clickLoginButton();

    }

    @When("user is on the home page")
    public void userIsOnTheHomePage() {
        productPage.clickHomepage();
        Assert.assertTrue(" Home page not loaded successfully", productPage.isHomePageDisplayed());
        System.out.println(" User is successfully on the home page.");
    }

    @Then("product detail shows Name, Price, Description, and Image")
    public void productDetailShowsNamePriceDescriptionAndImage() {
        String name = productPage.getProductName();
        String price = productPage.getProductPrice();
        String description = productPage.getProductDescription();
        boolean imageVisible = productPage.isProductImageDisplayed();

        Assert.assertNotNull(" Product name is missing!", name);
        Assert.assertFalse(" Product name is empty!", name.trim().isEmpty());

        Assert.assertNotNull(" Product price is missing!", price);
        Assert.assertTrue("Product price is empty!", price.length() > 0);

        Assert.assertNotNull(" Product description is missing!", description);
        Assert.assertTrue(" Product description is empty!", description.length() > 0);

        Assert.assertTrue(" Product image is not visible!", imageVisible);

        System.out.println(" Product Name: " + name);
        System.out.println(" Product Price: " + price);
        System.out.println(" Product Description: " + description);
        System.out.println(" Product image is displayed: " + imageVisible);
    }


    //API

    @When("user opens product with ID {int}")
    public void user_opens_product_with_id(Integer productId) {
        response = given()
                .spec(ApiUtilities.spec())
                .when()
                .get("/products/" + productId);
        response.prettyPrint();
    }

    @Then("API returns correct product name and price")
    public void api_returns_correct_product_name_and_price() {
        response.then().statusCode(200);
        String name = response.jsonPath().getString("name");
        String price = response.jsonPath().getString("price");

        System.out.println("API Product Name: " + name);
        System.out.println("API Product Price: " + price);

        Assert.assertEquals("Product name mismatch!", "E-Book Reader", name);

        Assert.assertEquals("Product price mismatch!", "300.00", price);

    }


    @When("user opens a product named {string}")
    public void user_opens_a_product_named(String productName) {
        try {
            productPage.searchProduct(productName);
            productPage.openProductPage(productName);
        } catch (Exception e) {
            System.out.println("Product not found: " + productName);
        }
    }

    @Then("system should display placeholders for missing details")
    public void system_should_display_placeholders_for_missing_details() {
        boolean hasPlaceholderName = productPage.isElementDisplayed(By.xpath("//*[text()='Unknown Product']"));
        boolean hasPlaceholderImage = productPage.isElementDisplayed(By.xpath("//img[contains(@src,'placeholder')]"));

        Assert.assertTrue("Missing placeholder for name or image!", hasPlaceholderName || hasPlaceholderImage);
    }


    // ---------------US6---------------
    @Given("user is on the product detail page for {string}")
    public void userIsOnTheProductDetailPageFor(String productName) {
        boolean inCart = productPage.isProductInCart(productName);
        Assert.assertTrue("Product was not added to the cart!", inCart);
    }

    @When("user clicks the {string} button for product {string}")
    public void userClicksTheButtonForProduct(String buttonName, String productName) {
        if (buttonName.equalsIgnoreCase("Add to Cart")) {
            productPage.addToCartAndWait(productName);
        } else {
            throw new RuntimeException("Button not implemented: " + buttonName);
        }
    }

    @Then("product {string} should be added successfully and cart count should update")
    public void productShouldBeAddedSuccessfullyAndCartCountShouldUpdate(String productName) {

        boolean inCart = productPage.isProductaddedInCart(productName);
        Assert.assertTrue("Product was not added to the cart!", inCart);

    }
    //-----------API------------------------

    @When("user sends POST request to add product with ID {int} to cart")
    public void userSendsPOSTRequestToAddProductWithIDToCart(int productId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("product_id", productId);
        payload.put("quantity", 1);

        response = given()
                .spec(ApiUtilities.spec())
                .body(payload)
                .when()
                .post("/cart/add");

        System.out.println("Response Body: ");
        response.prettyPrint();
    }

    @Then("API should return success and product should appear in cart")
    public void apiShouldReturnSuccessAndProductShouldAppearInCart() {
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("message", containsString("added to cart"));

        String productName = response.jsonPath().getString("cart.product_name");
        System.out.println(" Product successfully added: " + productName);
    }

    @Then("API should return error for invalid product ID")
    public void apiShouldReturnErrorForInvalidProductID() {
        int statusCode = response.getStatusCode();
        Assert.assertTrue("Expected status 400, 404 or 500 but got " + statusCode,
                statusCode == 400 || statusCode == 404 || statusCode == 500);

        String responseBody = response.asString();
        System.out.println("Error Response: " + responseBody);

        Assert.assertTrue("Expected error message not found!",
                responseBody.contains("Failed to add product")
                        || responseBody.contains("Invalid product")
                        || responseBody.contains("not found"));
    }


    //----------US07-------------------------------

    @When("user clicks the heart icon on the product {string}")
    public void userClicksTheHeartIconOnTheProduct(String productName) {
        productPage.hoverOverProductCard(productName);
        productPage.clickFavoriteIcon(productName);
    }


    @Then("product {string} should be added to {string}")
    public void productShouldBeAddedTo(String productName, String pageName) throws InterruptedException {
        productPage.openWishlist();
        Thread.sleep(5000);
        // Verify product exists in My Favorites
        By productInFavorites = By.xpath("//div[contains(@class,'product-card')]//h3[normalize-space(text())='" + productName + "']");
        Assert.assertTrue("Product " + productName + " not found in " + pageName + "!", productPage.isElementDisplayed(productInFavorites));
    }

    @And("the heart icon should turn filled with red")
    public void theHeartIconShouldTurnFilledWithRed() {
        WebElement icon = Driver.getDriver().findElement(By.cssSelector(".favorite-icon i.fas.fa-heart"));
        String classAttr = icon.getAttribute("class");
        Assert.assertTrue("Heart icon is not filled with red!", classAttr.contains("fas"));
    }


    @When("user goes to {string}")
    public void userGoesTo(String pageName) {
        productPage.openWishlist();
    }

    @And("clicks the heart icon again on product {string}")
    public void clicksTheHeartIconAgainOnProduct(String productName) {
        productPage.hoverOverProductCard(productName);
        productPage.clickFavoriteIcon(productName);
    }


    @And("product {string} should be removed from favorites")
    public void productShouldBeRemovedFromFavorites(String productName) {
        By productInWishlist = By.xpath("//div[contains(@class,'wishlist-item')]//h3[normalize-space(text())='" + productName + "']");
        Assert.assertFalse(productName + " was not removed from favorites!", productPage.isElementDisplayed(productInWishlist));
    }

    @Then("the product {string} should no longer appear in My Favorites")
    public void productShouldNoLongerAppearInMyFavorites(String productName) {
        By productCard = By.xpath("//div[contains(@class,'product-card')]//h3[normalize-space(text())='" + productName + "']/ancestor::div[contains(@class,'product-card')]");

        // Assert that it is NOT displayed
        boolean isPresent = Driver.getDriver().findElements(productCard).size() > 0;
        Assert.assertFalse(productName + " is still present in My Favorites!", isPresent);
    }


//=======================API FOR US07================================


    @Given("the API endpoint {string} is active")
    public void apiEndpointIsActive(String endpoint) {
        response = given()
                .spec(ApiUtilities.spec())
                .when()
                .get(endpoint);
        response.then().statusCode(200);
        System.out.println("API endpoint is active: " + endpoint);
    }

    @When("user adds product ID {int} to favorites for user ID {int}")
    public void userAddsProductToFavorites(int productId, int userId) {
        response = given()
                .spec(ApiUtilities.spec())
                .contentType("application/json")
                .body(Map.of("user_id", userId, "product_id", productId))
                .when()
                .post("/favorites");
        response.then().statusCode(201); // or whatever the API returns
        System.out.println("Added product " + productId + " to favorites for user " + userId);
    }

    @And("user sends GET request for user ID {int}")
    public void userSendsGETRequestForUserID(int userId) {
        response = given()
                .spec(ApiUtilities.spec())
                .queryParam("user_id", userId)
                .when()
                .get("/favorites");
        response.prettyPrint();
    }


    @Then("the added product {string} should appear in the API response")
    public void theAddedProductStringShouldAppearInTheAPIResponse(String productName) {

        response.then().statusCode(200);

        List<Map<String, Object>> favorites = response.jsonPath().getList("$");
        boolean found = false;
        for (Map<String, Object> fav : favorites) {
            Map<String, Object> product = (Map<String, Object>) fav.get("product");
            if (product != null && productName.equals(product.get("name"))) {
                found = true;
                break;
            }
        }

        Assert.assertTrue("Product '" + productName + "' not found in API response!", found);
        System.out.println("Product is present in API response: " + productName);
    }


    @And("removal of the product {string} should update the favorites list correctly for user ID {int}")
    public void removalOfProductShouldUpdateFavorites(String productName, int userId) {
        // Send GET again after removal
        response = given()
                .spec(ApiUtilities.spec())
                .queryParam("user_id", userId)
                .when()
                .get("/api/favorites");
        response.prettyPrint();

        List<String> productNames = response.jsonPath().getList("products.name");
        Assert.assertFalse("Removed product '" + productName + "' still present in API response!", productNames.contains(productName));
        System.out.println(" Removed product is no longer present in API response: " + productName);
    }


    //=================US08=====================


    @When("user hovers over the cart icon in the header")
    public void userHoversOverTheCartIconInTheHeader() {
        productPage.hoverOverCartIcon();
    }

    @Then("“View Cart” should appear")
    public void viewCartShouldAppear() {
        Assert.assertTrue(
                "“View Cart” button not visible after hover!",
                productPage.isViewCartVisibleAfterHover()
        );
    }

    @And("clicking it should navigate to the cart page")
    public void clickingItShouldNavigateToTheCartPage() {
        productPage.clickViewCart();
        Assert.assertTrue(
                "User not navigated to Cart page!",
                Driver.getDriver().getCurrentUrl().contains("/cart")
        );
    }


    // ------------------ MULTIPLE PRODUCT CART ------------------
    @Given("the user has added multiple products including")
    public void theUserHasAddedMultipleProductsIncluding(io.cucumber.datatable.DataTable dataTable) {
        List<String> products = dataTable.asList(String.class);

        for (String productName : products) {
            boolean added = false;
            int attempts = 0;

            while (!added && attempts < 5) {
                try {
                    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));

                    // Locate product card dynamically
                    WebElement productCard = wait.until(driver -> driver.findElement(By.xpath(
                            "//div[contains(@class,'product-card')]//h3[normalize-space(text())='" + productName + "']/ancestor::div[contains(@class,'product-card')]"
                    )));

                    // Scroll into view
                    ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView({block:'center'});", productCard);

                    // Wait for temporary overlays like toast/modal to disappear
                    wait.until(ExpectedConditions.invisibilityOfAllElements(
                            Driver.getDriver().findElements(By.cssSelector(".toast, .modal, .overlay"))
                    ));

                    // Click add-to-cart button
                    WebElement addBtn = productCard.findElement(By.cssSelector("button.add-to-cart"));
                    wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();

                    // Hover over cart icon to refresh mini-cart
                    productPage.hoverOverCartIcon();

                    // Verify product in mini-cart dynamically
                    added = Driver.getDriver().findElements(By.xpath("//div[contains(@class,'cart-item-name') and text()='" + productName + "']")).size() > 0;

                    if (!added) Thread.sleep(200);

                } catch (StaleElementReferenceException | ElementClickInterceptedException | TimeoutException e) {
                    System.out.println("Retrying " + productName + " due to: " + e.getMessage());
                } catch (InterruptedException ignored) {
                }

                attempts++;
            }

            Assert.assertTrue("Product " + productName + " was not added to the cart after retries!", added);
        }
    }


    @Then("all added products should appear with correct prices")
    public void allAddedProductsShouldAppearWithCorrectPrices(io.cucumber.datatable.DataTable dataTable) throws
            InterruptedException {

        productPage.hoverOverCartIcon();
        Thread.sleep(1500);
        List<Map<String, String>> expectedProducts = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> expected : expectedProducts) {
            String productName = expected.get("Product Name");
            String expectedPrice = expected.get("Price");

            // get actual price from cart and remove any "Price:" prefix
            String actualPrice = productPage.getProductPriceFromCart(productName);
            if (actualPrice.toLowerCase().startsWith("price:")) {
                actualPrice = actualPrice.substring(6).trim(); // remove "Price:" and trim
            }

            Assert.assertTrue("Product name " + productName + " not found in cart!",
                    productPage.isProductInCart(productName));
            Assert.assertEquals("Price mismatch for product " + productName, expectedPrice, actualPrice);
        }
    }


    @Then("the total price should be accurate")
    public void theTotalPriceShouldBeAccurate() {
        // Hover over cart icon to reveal subtotal
        productPage.hoverOverCartIcon();

        // Get subtotal amount (second span inside cart-subtotal)
        By subtotalLocator = By.cssSelector("div.cart-subtotal span:last-child");
        String subtotalText = productPage.getText(subtotalLocator).replace("Price:", "").trim();

        // Compute expected total from products in cart
        double expectedTotal = 0.0;
        List<String> products = productPage.getAllCartProductNames();
        for (String product : products) {
            String priceText = productPage.getProductPriceFromCart(product).replace("Price:", "").replace("$", "").trim();
            expectedTotal += Double.parseDouble(priceText);
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        String expectedTotalStr = formatter.format(expectedTotal);


        Assert.assertEquals("Cart total is invalid or missing!", expectedTotalStr, subtotalText);
    }


    // ------------------ REMOVE PRODUCT ------------------


    @When("user clicks Remove on the product {string}")
    public void userClicksRemoveOnTheProduct(String productName) {
        int attempts = 0;
        boolean clicked = false;
        productPage.hoverOverCartIcon();
        while (attempts < 5 && !clicked) {
            try {
                // Find the cart item container for this product
                By cartItem = By.xpath("//div[contains(@class,'cart-item')]//*[normalize-space(text())='"
                        + productName + "']/ancestor::div[contains(@class,'cart-item')]");

                WebElement item = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(cartItem));

                // Find the remove button inside this container
                WebElement removeBtn = item.findElement(By.cssSelector("button.remove-item"));

                // Scroll and click
                ((JavascriptExecutor) Driver.getDriver())
                        .executeScript("arguments[0].scrollIntoView(true);", removeBtn);
                removeBtn.click();

                clicked = true;

                // wait briefly for DOM to update
                Thread.sleep(500);

            } catch (StaleElementReferenceException | TimeoutException e) {
                attempts++;
            } catch (InterruptedException ignored) {
            }
        }

        if (!clicked) {
            Assert.fail("Failed to click Remove button for product: " + productName);
        }
    }


    @Then("the product {string} should be removed from the cart")
    public void theProductShouldBeRemovedfromthecart(String productName) {
        // Retry a few times if DOM is slow
        boolean removed = false;
        int attempts = 0;
        while (attempts < 5) {
            if (!productPage.isProductInCart(productName)) {
                removed = true;
                break;
            }
            attempts++;
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }
        Assert.assertTrue("Product still found in cart after removal!", removed);
    }


    @And("the total price should update instantly")
    public void theTotalPriceShouldUpdateInstantly() {
        // Hover over cart to make subtotal visible
        productPage.hoverOverCartIcon();

        // Retry mechanism to handle DOM updates after removal
        String subtotalText = "";
        int attempts = 0;
        while (attempts < 5) {
            if (productPage.isCartSubtotalDisplayed()) {
                subtotalText = productPage.getCartSubtotal().replace("Price:", "").trim();
                break;
            }
            attempts++;
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }

        if (subtotalText.isEmpty()) {
            Assert.fail("Cart subtotal is missing or not visible!");
        }

        // Recalculate expected total from current cart products
        List<String> products = productPage.getAllCartProductNames();
        double expectedTotal = 0.0;

        for (String product : products) {
            // Ensure we fetch the latest price from the cart after removal
            String priceText = productPage.getProductPriceFromCart(product)
                    .replace("Price:", "")
                    .replace("$", "")
                    .trim();
            expectedTotal += Double.parseDouble(priceText);
        }

        String expectedTotalStr = "$" + String.format("%.2f", expectedTotal);

        // Compare actual subtotal vs recalculated total
        Assert.assertEquals("Cart subtotal did not update correctly after removal!", expectedTotalStr, subtotalText);
    }


    // ------------------ EMPTY CART ------------------
    @Given("the user has no products added to the cart")
    public void theUserHasNoProductsAddedToTheCart() {
        productPage.hoverOverCartIcon();
        productPage.clearAllCartItems();
        Assert.assertEquals("Cart is not empty!", 0, productPage.getCartCount());
    }

    @When("user goes to the Cart icon")
    public void userGoesToTheCartIcon() {
        productPage.hoverOverCartIcon();
    }

    @Then("the page should display “Your cart is empty”")
    public void thePageShouldDisplayYourCartIsEmpty() {


        Assert.assertTrue(
                "Empty cart message not displayed!",
                productPage.isElementDisplayed(productPage.byXpathContainsText("Your cart is empty"))
        );
    }


    //==================================US09========================


    @When("user clicks “Confirm Cart”")
    public void userClicksConfirmCart() throws InterruptedException {
        productPage.hoverOverCartIcon();
        By viewCartButton = By.xpath("//*[contains(text(),'View Cart') or contains(@href,'/cart')]");
        productPage.click(viewCartButton);
        System.out.println(" Clicked 'View Cart' using productPage.");
        By confirmCartButton = By.xpath("//button[contains(text(),'Confirm Cart') or @id='confirm-cart']");
        productPage.click(confirmCartButton);
        System.out.println(" Clicked 'Confirm Cart' on the confirmation page.");
    }

    @Then("the page should display")
    public void thePageShouldDisplay(String expectedMessage) {
        // Wait for both SweetAlert elements to appear
        By titleLocator = By.cssSelector(".swal2-title");
        By messageLocator = By.cssSelector("#swal2-html-container");


        // Get both texts
        String title = productPage.getText(titleLocator);
        String body = productPage.getText(messageLocator);

        // Combine them with a newline to match expected format
        String actualMessage = title.trim() + "\n" + body.trim();

        // Normalize whitespace for both sides
        String expected = expectedMessage.replaceAll("\\s+", " ").trim();
        String actual = actualMessage.replaceAll("\\s+", " ").trim();

        // Assert
        Assert.assertTrue(
                "Expected message not displayed! \nExpected: " + expected + "\nActual: " + actual,
                actual.contains(expected)
        );

        System.out.println(" Verified confirmation message:\n" + actualMessage);
    }

    @When("user sends a POST request to confirm the cart for user ID {int}")
    public void userSendsAPOSTRequestToConfirmTheCartForUserID(int userId) {
        String endpoint = "https://bazaarstores.com/api/cart";

        response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\"user_id\": " + userId + "}")
                .when()
                .post(endpoint);

        System.out.println(" Sent POST request to confirm cart for user ID: " + userId);


    }

    @Then("the API response should return status “confirmed”")
    public void theAPIResponseShouldReturnStatusConfirmed() {
        Assert.assertEquals("API call failed!", 200, response.getStatusCode());
        String status = response.jsonPath().getString("status");
        Assert.assertEquals("Cart not confirmed!", "confirmed", status);
        System.out.println(" API response status: confirmed");

    }
}

