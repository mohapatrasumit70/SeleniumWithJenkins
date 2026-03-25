package utils;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.ConfigReader;

import java.time.Duration;
import java.util.NoSuchElementException;

/**
 * WaitUtil - Advanced wait strategies including FluentWait
 */
public class WaitUtil {

    private WaitUtil() {}

    /**
     * Create a FluentWait with custom polling and ignored exceptions
     */
    public static FluentWait<WebDriver> fluentWait(WebDriver driver, int timeoutSec, int pollMillis) {
        return new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(timeoutSec))
            .pollingEvery(Duration.ofMillis(pollMillis))
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Wait for element to be visible using FluentWait
     */
    public static WebElement waitForElement(WebDriver driver, By locator) {
        return fluentWait(driver, ConfigReader.getExplicitWait(), 500)
            .until(d -> d.findElement(locator));
    }

    /**
     * Wait for page to fully load (document.readyState == 'complete')
     */
    public static void waitForPageLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getPageLoadTimeout()))
            .until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd)
                    .executeScript("return document.readyState").equals("complete")
            );
    }

    /**
     * Wait for AJAX calls to complete (jQuery)
     */
    public static void waitForAjax(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()))
            .until((ExpectedCondition<Boolean>) wd -> {
                try {
                    return (Boolean) ((JavascriptExecutor) wd)
                        .executeScript("return jQuery.active == 0");
                } catch (Exception e) {
                    return true; // jQuery not present
                }
            });
    }

    /**
     * Wait until element text changes
     */
    public static boolean waitForTextToBe(WebDriver driver, WebElement element, String expectedText) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()))
            .until(ExpectedConditions.textToBePresentInElement(element, expectedText));
    }

    /**
     * Wait for element count to be at least N
     */
    public static boolean waitForElementCount(WebDriver driver, By locator, int expectedCount) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()))
            .until(d -> d.findElements(locator).size() >= expectedCount);
    }
}
