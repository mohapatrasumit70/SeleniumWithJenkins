package Base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import Listeners.ExtentReportListener;
import config.ConfigReader;
import utils.ScreenshotUtil;

/**
 * BaseTest - Parent class for all test classes
 * Handles driver lifecycle, reporting, and screenshot on failure
 */
@Listeners(ExtentReportListener.class)
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info("======= TEST SUITE STARTED =======");
        ExtentReportListener.initReport();
    }
    


    @BeforeMethod(alwaysRun = true)
    public void setUp() {                          // no parameter needed
        String targetBrowser = ConfigReader.getBrowser();  // always reads from config

        log.info("Setting up driver for browser: {}", targetBrowser);
        DriverFactory.initDriver(targetBrowser);
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getBaseUrl());
        log.info("Navigated to: {}", ConfigReader.getBaseUrl());
    }

    
    
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("TEST FAILED: {}", result.getName());
            if (ConfigReader.isScreenshotOnFailure()) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(
                    driver, result.getName()
                );
                log.info("Screenshot saved: {}", screenshotPath);
                ExtentReportListener.attachScreenshot(screenshotPath);
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("TEST PASSED: {}", result.getName());
        } else {
            log.warn("TEST SKIPPED: {}", result.getName());
        }
        DriverFactory.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ExtentReportListener.flushReport();
        log.info("======= TEST SUITE COMPLETED =======");
    }
}
