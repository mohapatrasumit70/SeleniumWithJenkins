package Pages;

import Base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * CheckoutPage - Page Object for Checkout flow
 */
public class CheckoutPage extends BasePage {

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(className = "complete-header")
    private WebElement orderConfirmation;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillCheckoutInfo(String firstName, String lastName, String zip) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, zip);
        return this;
    }

    public CheckoutPage clickContinue() {
        click(continueButton);
        return this;
    }

    public CheckoutPage clickFinish() {
        click(finishButton);
        return this;
    }

    public String getOrderConfirmationMessage() {
        return getText(orderConfirmation);
    }

    public boolean isOrderConfirmed() {
        return isDisplayed(orderConfirmation);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public String getOrderTotal() {
        return getText(totalLabel);
    }

    /**
     * Complete end-to-end checkout
     */
    public CheckoutPage completeCheckout(String firstName, String lastName, String zip) {
        fillCheckoutInfo(firstName, lastName, zip);
        clickContinue();
        clickFinish();
        return this;
    }
}
