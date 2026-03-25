package tests;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Base.BaseTest;
import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.DashboardPage;
import Pages.LoginPage;
import utils.TestDataProvider;

/**
 * CheckoutTest - End-to-end checkout flow tests
 */
public class CheckoutTest extends BaseTest {

    private DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndAddProductsToCart() {
        dashboardPage = new LoginPage(driver).login("standard_user", "secret_sauce");
        dashboardPage.addBackpackToCart();
        dashboardPage.addBikeLightToCart();
        Assert.assertEquals(dashboardPage.getCartCount(), 2, "Pre-condition: 2 items must be in cart");
    }

    @Test(priority = 1, description = "Verify cart shows correct items")
    public void testCartContainsAddedItems() {
        CartPage cartPage = dashboardPage.clickCart();

        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");
        Assert.assertTrue(
            cartPage.getCartItemNames().contains("Sauce Labs Backpack"),
            "Cart should contain Sauce Labs Backpack"
        );
        Assert.assertTrue(
            cartPage.getCartItemNames().contains("Sauce Labs Bike Light"),
            "Cart should contain Sauce Labs Bike Light"
        );
    }

    @Test(priority = 2, description = "Verify complete checkout flow",
          dataProvider = "checkoutData", dataProviderClass = TestDataProvider.class)
    public void testCompleteCheckout(String firstName, String lastName, String zip) {
        CartPage cartPage = dashboardPage.clickCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        checkoutPage.completeCheckout(firstName, lastName, zip);

        Assert.assertTrue(checkoutPage.isOrderConfirmed(),
            "Order confirmation should be displayed");
        Assert.assertEquals(checkoutPage.getOrderConfirmationMessage(),
            "Thank you for your order!",
            "Confirmation message should be 'Thank you for your order!'");

        log.info("Checkout completed for: {} {} {}", firstName, lastName, zip);
    }

    @Test(priority = 3, description = "Verify checkout fails without first name")
    public void testCheckoutWithMissingFirstName() {
        CartPage cartPage = dashboardPage.clickCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillCheckoutInfo("", "Doe", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(
            checkoutPage.getErrorMessage().contains("First Name is required"),
            "Should show 'First Name is required' error"
        );
    }

    @Test(priority = 4, description = "Verify checkout fails without zip code")
    public void testCheckoutWithMissingZip() {
        CartPage cartPage = dashboardPage.clickCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillCheckoutInfo("John", "Doe", "");
        checkoutPage.clickContinue();

        Assert.assertTrue(
            checkoutPage.getErrorMessage().contains("Postal Code is required"),
            "Should show 'Postal Code is required' error"
        );
    }

    @Test(priority = 5, description = "Verify continue shopping from cart")
    public void testContinueShoppingFromCart() {
        CartPage cartPage = dashboardPage.clickCart();
        DashboardPage backToDashboard = cartPage.continueShopping();

        Assert.assertTrue(backToDashboard.isDashboardLoaded(),
            "Should return to products page after 'Continue Shopping'");
    }

    @Test(priority = 6, description = "Verify order total is displayed during checkout")
    public void testOrderTotalDisplayed() {
        CartPage cartPage = dashboardPage.clickCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();
        String total = checkoutPage.getOrderTotal();
        Assert.assertNotNull(total, "Order total should be displayed");
        Assert.assertFalse(total.isEmpty(), "Order total should not be empty");
        log.info("Order total: {}", total);
    }
}
