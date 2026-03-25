package Pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import Base.BasePage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CartPage - Page Object for Shopping Cart
 */
public class CartPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> itemPrices;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    
    public boolean isCartPageLoaded() {
        return isDisplayed(pageTitle);
    }

    public int getCartItemCount() {
        return getElementCount(cartItems);
    }

    public List<String> getCartItemNames() {
        return itemNames.stream()
            .map(e -> getText(e))
            .collect(Collectors.toList());
    }

    public List<String> getCartItemPrices() {
        return itemPrices.stream()
            .map(e -> getText(e))
            .collect(Collectors.toList());
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }

    public DashboardPage continueShopping() {
        click(continueShoppingButton);
        return new DashboardPage(driver);
    }
}
