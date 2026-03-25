package tests;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Base.BaseTest;
import Pages.DashboardPage;
import Pages.LoginPage;
import utils.TestDataProvider;

import java.util.List;

/**
 * ProductTest - Tests for product listing, sorting, and cart operations
 */
public class ProductTest extends BaseTest {

    private DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        dashboardPage = new LoginPage(driver).login("standard_user", "secret_sauce");
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard must be loaded");
    }

    @Test(priority = 1, description = "Verify product count on dashboard")
    public void testProductCountOnDashboard() {
        int productCount = dashboardPage.getProductCount();
        Assert.assertEquals(productCount, 6, "Should display 6 products");
        log.info("Product count verified: {}", productCount);
    }

    @Test(priority = 2, description = "Verify product names are not empty")
    public void testProductNamesNotEmpty() {
        List<String> names = dashboardPage.getAllProductNames();
        Assert.assertFalse(names.isEmpty(), "Product names list should not be empty");
        names.forEach(name -> Assert.assertFalse(name.isEmpty(), "Product name should not be blank"));
        log.info("All {} product names are non-empty", names.size());
    }

    @Test(priority = 3, description = "Verify cart count updates after adding product")
    public void testAddSingleProductToCart() {
        int initialCount = dashboardPage.getCartCount();
        dashboardPage.addBackpackToCart();

        int updatedCount = dashboardPage.getCartCount();
        Assert.assertEquals(updatedCount, initialCount + 1, "Cart count should increase by 1");
    }

    @Test(priority = 4, description = "Verify cart count after adding multiple products")
    public void testAddMultipleProductsToCart() {
        dashboardPage.addBackpackToCart();
        dashboardPage.addBikeLightToCart();

        Assert.assertEquals(dashboardPage.getCartCount(), 2, "Cart should contain 2 items");
    }

    @Test(priority = 5, description = "Verify product sorting - Name A to Z",
          dataProvider = "sortOptions", dataProviderClass = TestDataProvider.class)
    public void testProductSorting(String sortOption) {
        dashboardPage.sortProducts(sortOption);
        List<String> productNames = dashboardPage.getAllProductNames();

        Assert.assertFalse(productNames.isEmpty(), "Products should be listed after sorting by: " + sortOption);
        log.info("Sort '{}' applied - {} products displayed", sortOption, productNames.size());
    }

    @Test(priority = 6, description = "Verify alphabetical ascending sort order")
    public void testSortNameAscending() {
        dashboardPage.sortProducts("Name (A to Z)");
        List<String> names = dashboardPage.getAllProductNames();

        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(
                names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0,
                "Products should be sorted A-Z, but found: " + names.get(i) + " before " + names.get(i + 1)
            );
        }
        log.info("Ascending sort order verified for all {} products", names.size());
    }
}
