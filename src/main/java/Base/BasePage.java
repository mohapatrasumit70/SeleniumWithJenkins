package Base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

//import static com.framework.config.ConfigReader.getExplicitWait;
import static config.ConfigReader.getExplicitWait;

/**
 * BasePage - Parent class for all Page Objects
 * Contains reusable Selenium helper methods with logging
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    private static final Logger log = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getExplicitWait()));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    
    // ============== WAIT METHODS ==============

    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitForInvisibility(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ============== CLICK METHODS ==============

    public void click(WebElement element) {
        waitForClickable(element);
        log.debug("Clicking element: {}", element);
        element.click();
    }

    public void jsClick(WebElement element) {
        log.debug("JS Click on element: {}", element);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    public void doubleClick(WebElement element) {
        waitForClickable(element);
        actions.doubleClick(element).perform();
    }

    public void rightClick(WebElement element) {
        waitForClickable(element);
        actions.contextClick(element).perform();
    }

    // ============== INPUT METHODS ==============

    public void type(WebElement element, String text) {
        waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
        log.debug("Typed '{}' into element", text);
    }

    public void clearAndType(WebElement element, String text) {
        waitForVisibility(element);
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }

    public void pressKey(WebElement element, Keys key) {
        element.sendKeys(key);
    }

    // ============== GET TEXT METHODS ==============

    public String getText(WebElement element) {
        waitForVisibility(element);
        return element.getText().trim();
    }

    public String getAttribute(WebElement element, String attribute) {
        waitForVisibility(element);
        return element.getAttribute(attribute);
    }

    // ============== SELECT DROPDOWN ==============

    public void selectByVisibleText(WebElement element, String text) {
        waitForVisibility(element);
        new Select(element).selectByVisibleText(text);
        log.debug("Selected '{}' from dropdown", text);
    }

    public void selectByValue(WebElement element, String value) {
        waitForVisibility(element);
        new Select(element).selectByValue(value);
    }

    public void selectByIndex(WebElement element, int index) {
        waitForVisibility(element);
        new Select(element).selectByIndex(index);
    }

    public String getSelectedOption(WebElement element) {
        return new Select(element).getFirstSelectedOption().getText();
    }

    // ============== NAVIGATION ==============

    public void navigateTo(String url) {
        driver.get(url);
        log.info("Navigated to: {}", url);
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ============== SCROLL METHODS ==============

    public void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // ============== VISIBILITY CHECKS ==============

    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isSelected(WebElement element) {
        return element.isSelected();
    }

    // ============== ALERT HANDLING ==============

    public String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }

    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        log.debug("Alert accepted");
    }

    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    // ============== FRAME HANDLING ==============

    public void switchToFrame(WebElement frame) {
        driver.switchTo().frame(frame);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // ============== HOVER ==============

    public void hoverOver(WebElement element) {
        waitForVisibility(element);
        actions.moveToElement(element).perform();
        log.debug("Hovered over element");
    }

    // ============== DRAG & DROP ==============

    public void dragAndDrop(WebElement source, WebElement target) {
        actions.dragAndDrop(source, target).perform();
        log.debug("Drag and Drop performed");
    }

    // ============== LIST HELPERS ==============

    public int getElementCount(List<WebElement> elements) {
        return elements.size();
    }

    public boolean isTextPresentInList(List<WebElement> elements, String text) {
        return elements.stream().anyMatch(e -> e.getText().trim().equalsIgnoreCase(text));
    }

    // ============== WAIT FOR CONDITION ==============

    public void waitForUrlContains(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
    }

    public void waitForTitleContains(String title) {
        wait.until(ExpectedConditions.titleContains(title));
    }

    public void hardWait(int seconds) throws InterruptedException {
        log.warn("Using hard wait - consider using explicit waits instead");
        Thread.sleep(seconds * 1000L);
    }
}
