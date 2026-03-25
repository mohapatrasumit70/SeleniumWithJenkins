package Base;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverFactory - Thread-safe WebDriver creation using ThreadLocal
 * Supports Chrome, Firefox, Edge with headless mode
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {}

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void initDriver(String browser) {
        WebDriver driver = createDriver(browser);
        configureDriver(driver);
        driverThreadLocal.set(driver);
        log.info("Driver initialized: {} | Thread: {}", browser, Thread.currentThread().getId());
    }

    private static WebDriver createDriver(String browser) {
        boolean headless = ConfigReader.isHeadless();

        switch (browser.toLowerCase().trim()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOpts = new FirefoxOptions();
                if (headless) ffOpts.addArguments("--headless");
                log.info("Launching Firefox browser");
                return new FirefoxDriver(ffOpts);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOpts = new EdgeOptions();
                if (headless) edgeOpts.addArguments("--headless");
                log.info("Launching Edge browser");
                return new EdgeDriver(edgeOpts);

                
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOpts = new ChromeOptions();
                
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
               

                chromeOpts.setExperimentalOption("prefs", prefs);
                if (headless) {
                    chromeOpts.addArguments("--headless=new");
                }
                
                chromeOpts.addArguments(
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--incognito",
                    "--disable-gpu",
                    "--window-size=1920,1080",
                    "--disable-extensions",
                    "--disable-infobars",
                    "--disable-notifications",
                    "--disable-blink-features=AutomationControlled",
                    "--disable-save-password-bubble",
                    "--disable-infobars",
                    "--user-data-dir=/tmp/chrome-selenium"
                    
                );
                log.info("Launching Chrome browser");
                return new ChromeDriver(chromeOpts);
        }
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(ConfigReader.getImplicitWait())
        );
        driver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(ConfigReader.getPageLoadTimeout())
        );
        driver.manage().deleteAllCookies();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            log.info("Driver quit and removed from ThreadLocal | Thread: {}", Thread.currentThread().getId());
        }
    }
}
