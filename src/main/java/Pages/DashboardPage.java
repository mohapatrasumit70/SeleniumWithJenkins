package Pages;

import Base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * DashboardPage - Page Object for the Products/Dashboard page
 */
public class DashboardPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-backpack']")
    private WebElement addBackpackToCartBtn;

    @FindBy(css = "[data-test='add-to-cart-sauce-labs-bike-light']")
    private WebElement addBikeLightToCartBtn;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(css = "[data-test='product_sort_container']")
    private WebElement sortDropdown;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardLoaded() {
        return waitForVisibility(pageTitle).isDisplayed();
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getProductCount() {
        return getElementCount(productItems);
    }

    public DashboardPage addBackpackToCart() {
        click(addBackpackToCartBtn);
        return this;
    }

    public DashboardPage addBikeLightToCart() {
        click(addBikeLightToCartBtn);
        return this;
    }

    public int getCartCount() {
        try {
            return Integer.parseInt(getText(cartBadge));
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage clickCart() {
        click(cartIcon);
        return new CartPage(driver);
    }

    public DashboardPage sortProducts(String sortOption) {
        selectByVisibleText(sortDropdown, sortOption);
        return this;
    }

    public List<String> getAllProductNames() {
        return productNames.stream()
            .map(e -> getText(e))
            .collect(java.util.stream.Collectors.toList());
    }

    public LoginPage logout() {
        click(menuButton);
        click(logoutLink);
        return new LoginPage(driver);
    }
}
